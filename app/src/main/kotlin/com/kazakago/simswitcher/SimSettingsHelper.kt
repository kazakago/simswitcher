package com.kazakago.simswitcher

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.telephony.SubscriptionManager

object SimSettingsHelper {

    fun launchSimSettings(context: Context, launchBlock: (Intent) -> Unit = { context.startActivity(it) }) {
        val directIntent = Intent().apply {
            component = ComponentName(
                "com.android.settings",
                $$"com.android.settings.Settings$MobileNetworkListActivity"
            )
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (directIntent.resolveActivity(context.packageManager) != null) {
            launchBlock(directIntent)
            return
        }

        val fallbackIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        launchBlock(fallbackIntent)
    }

    sealed interface SimState {
        data class Active(val name: String?) : SimState
        data object Disabled : SimState
        data object Unknown : SimState
    }

    fun getActiveDataSimState(context: Context): SimState {
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return SimState.Unknown
        }
        val subscriptionManager = context.getSystemService(SubscriptionManager::class.java)
        val activeList = subscriptionManager.activeSubscriptionInfoList
        return if (!activeList.isNullOrEmpty()) {
            val defaultDataSubId = SubscriptionManager.getDefaultDataSubscriptionId()
            val activeDataSim = activeList.find { it.subscriptionId == defaultDataSubId }
            if (activeDataSim != null) {
                val name = activeDataSim.displayName?.toString() ?: activeDataSim.carrierName?.toString()
                SimState.Active(name)
            } else {
                SimState.Disabled
            }
        } else {
            SimState.Disabled
        }
    }
}
