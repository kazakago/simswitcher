package com.kazakago.simswitcher

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast

class MainActivity : Activity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hasPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            SimSettingsHelper.launchSimSettings(this)
            finish()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val isGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                Toast.makeText(this, getString(R.string.toast_permission_denied_notice), Toast.LENGTH_SHORT).show()
            }
            SimSettingsHelper.launchSimSettings(this)
            finish()
        }
    }
}
