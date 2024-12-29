package com.example.weathersimulate

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import kotlin.random.Random

class WeatherService : Service() {

    private val data = arrayOf("Rainy", "Sunny", "Cloudy", "Snowy", "Stormy")
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()

        try {
            // Notification setup
            val channelId = "weather_service_channel"
            val channelName = "Weather Simulation Service"

            val manager = getSystemService(NotificationManager::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (manager == null) {
                    stopSelf() // Stop the service if NotificationManager is unavailable
                    return
                }
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
                )
                manager.createNotificationChannel(channel)
            }

            val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId)
                    .setContentTitle("Weather Simulation Running")
                    .setContentText("Fetching the current weather status...")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .setOngoing(true) // Persistent notification
                    .build()
            } else {
                Notification.Builder(this)
                    .setContentTitle("Weather Simulation Running")
                    .setContentText("Fetching the current weather status...")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .setOngoing(true) // Persistent notification
                    .build()
            }

            startForeground(1, notification)

            // Start weather updates
            isRunning = true
            handler.post(object : Runnable {
                override fun run() {
                    if (isRunning) {
                        val randomWeather = getRandomWeather()
                        updateNotification(randomWeather)
                        handler.postDelayed(this, 5000)
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf() // Stop the service if any error occurs
        }
    }

    private fun getRandomWeather(): String {
        return data[Random.nextInt(data.size)]
    }

    private fun updateNotification(currentWeather: String) {
        try {
            val channelId = "weather_service_channel"

            val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, channelId)
                    .setContentTitle("Weather Simulation Running")
                    .setContentText("Current weather: $currentWeather")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .setOngoing(true) // Persistent notification
                    .build()
            } else {
                Notification.Builder(this)
                    .setContentTitle("Weather Simulation Running")
                    .setContentText("Current weather: $currentWeather")
                    .setSmallIcon(android.R.drawable.ic_menu_compass)
                    .setOngoing(true) // Persistent notification
                    .build()
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.notify(1, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // No binding required
    }
}
