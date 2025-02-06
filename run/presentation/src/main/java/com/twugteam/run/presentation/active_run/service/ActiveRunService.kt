package com.twugteam.run.presentation.active_run.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.twugteam.core.presentation.designsystem.R
import com.twugteam.core.presentation.ui.toFormatted
import com.twugteam.run.domain.RunningTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class ActiveRunService : Service() {
    companion object {
        var isNotificationServiceActive = false
        private const val CHANNEL_ID = "Active_Run"
    }

    private val runningTracker by inject<RunningTracker>()
    private var serviceCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val notificationManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    private val baseNotification by lazy {
        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(com.twugteam.core.presentation.designsystem.R.drawable.logo)
            .setContentTitle(getString(R.string.active_run))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startNotificationService(activityClass: Class<*>) {
        if (!isNotificationServiceActive) {
            isNotificationServiceActive = true
            createNotificationChannel()
            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = "runsphere://active_run".toUri()
                // flag: the new activity/screen will not be launched if it is already running at the top of the navigation history back-stack
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                // adding pending intent to intent
                addNextIntentWithParentStack(activityIntent)
                // Request Code --> we don't take care about request code
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
            val notification = baseNotification
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .build()

            // ID --> must not be 0 from official documentation
            startForeground(1, notification)
            updateNotification()
        }
    }

    private fun updateNotification() {
        runningTracker.elapsedTime.onEach { elapsedTime ->
            val notification = baseNotification
                .setContentText(elapsedTime.toFormatted())
                .build()
            notificationManager.notify(1, notification)
        }.launchIn(serviceCoroutineScope)
    }

    fun stopNotificationService() {
        stopSelf()
        isNotificationServiceActive = false
        serviceCoroutineScope.cancel()

        // We need to re-initialized the coroutine scope always once we cancel otherwise it won't
        // be initialized and launched by default when needed for the next time or notification
        serviceCoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.active_run),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}