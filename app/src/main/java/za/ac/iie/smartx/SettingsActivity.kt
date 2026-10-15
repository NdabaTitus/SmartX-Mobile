package za.ac.iie.smartx

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * SettingsActivity
 *
 * Demonstrates the use of SharedPreferences to persist user preferences.
 * Preferences include: dark mode, notifications, and refresh interval.
 */
class SettingsActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "SettingsActivity"
        const val PREFS_NAME = "smartx_prefs"
        const val KEY_DARK_MODE = "dark_mode"
        const val KEY_NOTIFICATIONS = "notifications"
        const val KEY_REFRESH_INTERVAL = "refresh_interval"
    }

    private lateinit var prefs: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_settings)

        // Load preferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val darkModeSwitch = findViewById<Switch>(R.id.darkModeSwitch)
        val notificationsSwitch = findViewById<Switch>(R.id.notificationsSwitch)
        val refreshSpinner = findViewById<Spinner>(R.id.refreshSpinner)
        val saveButton = findViewById<Button>(R.id.saveSettingsButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Set up the refresh interval spinner
        val intervals = arrayOf("5 seconds", "10 seconds", "30 seconds", "60 seconds")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, intervals)
        refreshSpinner.adapter = adapter

        // Populate from saved preferences
        darkModeSwitch.isChecked = prefs.getBoolean(KEY_DARK_MODE, false)
        notificationsSwitch.isChecked = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        val savedInterval = prefs.getString(KEY_REFRESH_INTERVAL, "10 seconds")
        val savedIndex = intervals.indexOf(savedInterval)
        if (savedIndex >= 0) refreshSpinner.setSelection(savedIndex)

        Log.d(TAG, "Loaded preferences: darkMode=${darkModeSwitch.isChecked}, notifications=${notificationsSwitch.isChecked}, interval=$savedInterval")

        // Save button
        saveButton.setOnClickListener {
            prefs.edit()
                .putBoolean(KEY_DARK_MODE, darkModeSwitch.isChecked)
                .putBoolean(KEY_NOTIFICATIONS, notificationsSwitch.isChecked)
                .putString(KEY_REFRESH_INTERVAL, refreshSpinner.selectedItem.toString())
                .apply()

            Log.d(TAG, "Preferences saved")
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }

        // Back button
        backButton.setOnClickListener {
            Log.d(TAG, "Back tapped")
            finish() // Returns to previous activity (MainActivity)
        }
    }
}