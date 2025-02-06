package com.twugteam.run.presentation.active_run.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
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
        private const val ACTION_START = "ACTION_START"
        private const val ACTION_STOP = "ACTION_STOP"
        private const val EXTRA_ACTIVITY_CLASS = "EXTRA_ACTIVITY_CLASS"

        fun createStartIntent(context: Context, activityClass: Class<*>): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_STOP
            }
        }
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val activityClass = intent.getStringExtra(EXTRA_ACTIVITY_CLASS)
                    ?: throw IllegalArgumentException("No Activity Class Provided")
                startNotificationService(Class.forName(activityClass))
            }

            ACTION_STOP -> stopNotificationService()
        }
        return START_STICKY
        /*
        START_STICKY: -->
        Constant to return from onStartCommand(Intent, int, int)  : if this service's process is
        killed while it is started (after returning from onStartCommand(Intent, int, int)  ),
        then leave it in the started state but don't retain this delivered intent. Later the system
         will try to re-create the service. Because it is in the started state, it will guarantee
         to call onStartCommand(Intent, int, int)   after creating the new service instance; if
         there are not any pending start commands to be delivered to the service, it will be called
         with a null intent object, so you must take care to check for this.
         */
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