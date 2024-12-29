package com.example.weathersimulate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var connectionStatusText: TextView
    private lateinit var weatherUpdatesText: TextView
    private val data = arrayOf("Rainy", "Sunny", "Cloudy", "Snowy", "Stormy") // The weather data array

    private val handler = Handler(Looper.getMainLooper())
    private val updateWeatherRunnable = object : Runnable {
        override fun run() {
            // Update connection status and current weather
            updateConnectionStatusAndWeather()
            // Schedule the next update in 5 seconds
            handler.postDelayed(this, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing the TextViews from the layout
        connectionStatusText = findViewById(R.id.connectionStatusText)
        weatherUpdatesText = findViewById(R.id.weatherUpdatesText)

        // Button references
        val startServiceButton: Button = findViewById(R.id.startServiceButton)
        val stopServiceButton: Button = findViewById(R.id.stopServiceButton)
        val sendWeatherAlertButton: Button = findViewById(R.id.sendWeatherAlertButton)

        // Starting the weather simulation
        startServiceButton.setOnClickListener {
            val intent = Intent(this, WeatherService::class.java)
            startService(intent)
            Toast.makeText(this, "Weather simulation started!", Toast.LENGTH_SHORT).show()

            // Start updating weather every 5 seconds
            handler.post(updateWeatherRunnable)
        }

        // Stopping the weather simulation
        stopServiceButton.setOnClickListener {
            val intent = Intent(this, WeatherService::class.java)
            stopService(intent)
            Toast.makeText(this, "Weather simulation stopped!", Toast.LENGTH_SHORT).show()

            // Stop updating weather
            handler.removeCallbacks(updateWeatherRunnable)
        }

        // Sending a weather alert
        sendWeatherAlertButton.setOnClickListener {
            // Choose a random weather condition from the array
            val randomWeather = getRandomWeather()

            // Send a weather alert with the random weather condition
            val intent = Intent("com.example.WEATHER_ALERT").apply {
                putExtra("ALERT_MESSAGE", "Severe $randomWeather Warning!")
            }
            sendBroadcast(intent)

            // Display the weather update on the UI
            weatherUpdatesText.text = "Current weather: $randomWeather"

            // Optionally, update connection status with the same random weather condition
            connectionStatusText.text = "Current status: $randomWeather"
        }
    }

    // Function to get a random weather condition from the data array
    private fun getRandomWeather(): String {
        val randomIndex = Random.nextInt(data.size)
        return data[randomIndex]
    }

    // Function to update connection status and current weather
    private fun updateConnectionStatusAndWeather() {
        // Get a random weather condition
        val randomWeather = getRandomWeather()

        // Update connection status with the weather condition
        connectionStatusText.text = "Current status: $randomWeather"

        // Update the weather updates text
        weatherUpdatesText.text = "Current weather: $randomWeather"
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop updating weather to prevent memory leaks
        handler.removeCallbacks(updateWeatherRunnable)
    }
}
