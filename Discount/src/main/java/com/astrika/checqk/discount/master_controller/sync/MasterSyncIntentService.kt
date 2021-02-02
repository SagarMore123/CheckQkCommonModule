package com.astrika.checqk.discount.master_controller.sync

import android.app.IntentService
import android.content.Intent

class MasterSyncIntentService : IntentService("MasterSyncIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        SyncData.syncData(this)
/*
        if (intent?.getBooleanExtra(Constants.IS_SPLASH_MASTER, false) == true) {
            SyncData.splashSyncData(this)
        } else {
            SyncData.syncData(this)
        }
*/
    }
}