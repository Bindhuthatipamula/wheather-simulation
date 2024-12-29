package com.example.weathersimulate


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class WeatherAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alert = intent.getStringExtra("ALERT_MESSAGE") ?: "Unknown Alert"
        Toast.makeText(context, "Weather Alert: $alert", Toast.LENGTH_SHORT).show()
    }
}
