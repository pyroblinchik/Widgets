package com.example.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.widget.RemoteViews

class FlashlightWidget : AppWidgetProvider() {

    private var TOGGLE_FLASHLIGHT = "toggleFlashlight"
    private val PREF = "enabled"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, FlashlightWidget::class.java)
            intent.action = TOGGLE_FLASHLIGHT
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val views = RemoteViews(context.packageName, R.layout.flashlight_widget)
            views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent!!.action.equals(TOGGLE_FLASHLIGHT)) {
            val prefs = context!!.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            var enabled = prefs.getBoolean(PREF, false)
            enabled = !enabled
            prefs.edit().putBoolean(PREF, enabled).apply()

            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, enabled)
        }
    }
}