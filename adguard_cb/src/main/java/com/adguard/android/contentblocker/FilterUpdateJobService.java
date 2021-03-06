/*
 This file is part of AdGuard Content Blocker (https://github.com/AdguardTeam/ContentBlocker).
 Copyright © 2018 AdGuard Content Blocker. All rights reserved.

 AdGuard Content Blocker is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or (at your option)
 any later version.

 AdGuard Content Blocker is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along with
 AdGuard Content Blocker.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.adguard.android.contentblocker;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

import com.adguard.android.ServiceLocator;
import com.adguard.android.model.FilterList;
import com.adguard.android.service.FilterService;
import com.adguard.android.service.PreferencesService;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class FilterUpdateJobService extends JobService {
    private FilterUpdateTask filterUpdateTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        filterUpdateTask = new FilterUpdateTask();
        filterUpdateTask.execute(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (!filterUpdateTask.isCancelled()) {
            filterUpdateTask.cancel(true);
        }

        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class FilterUpdateTask extends AsyncTask<JobParameters, Void, Boolean> {
        private JobParameters jobParameters;

        @Override
        protected Boolean doInBackground(JobParameters... jobParameters) {
            if (jobParameters != null) {
                this.jobParameters = jobParameters[0];
            }

            ServiceLocator.getInstance(getApplicationContext()).getFilterService().tryUpdateFilters();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            jobFinished(jobParameters, result);
        }
    }
}
