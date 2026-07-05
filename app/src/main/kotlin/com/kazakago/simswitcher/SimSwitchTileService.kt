package com.kazakago.simswitcher

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.telephony.SubscriptionManager

class SimSwitchTileService : TileService() {

    private val subscriptionsChangedListener = object : SubscriptionManager.OnSubscriptionsChangedListener() {
        override fun onSubscriptionsChanged() {
            updateTileState()
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()

        val subscriptionManager = getSystemService(SubscriptionManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            subscriptionManager.addOnSubscriptionsChangedListener(mainExecutor, subscriptionsChangedListener)
        } else {
            @Suppress("DEPRECATION")
            subscriptionManager.addOnSubscriptionsChangedListener(subscriptionsChangedListener)
        }
    }

    override fun onStopListening() {
        super.onStopListening()
        val subscriptionManager = getSystemService(SubscriptionManager::class.java)
        subscriptionManager.removeOnSubscriptionsChangedListener(subscriptionsChangedListener)
    }

    private fun updateTileState() {
        when (val state = SimSettingsHelper.getActiveDataSimState(this)) {
            is SimSettingsHelper.SimState.Active -> {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.subtitle = state.name ?: getString(R.string.tile_subtitle_unknown_sim)
            }

            is SimSettingsHelper.SimState.Disabled -> {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.subtitle = getString(R.string.tile_subtitle_disabled)
            }

            is SimSettingsHelper.SimState.Unknown -> {
                qsTile.state = Tile.STATE_INACTIVE
                qsTile.subtitle = getString(R.string.tile_subtitle_default)
            }
        }
        qsTile.label = getString(R.string.app_name)
        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()
        unlockAndRun {
            val hasPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                SimSettingsHelper.launchSimSettings(this) { intent ->
                    startActivityWithPendingIntent(intent)
                }
            } else {
                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivityWithPendingIntent(intent)
            }
        }
    }

    private fun startActivityWithPendingIntent(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            @SuppressLint("StartActivityAndCollapseDeprecated")
            startActivityAndCollapse(intent)
        }
    }
}
