package za.ac.iie.smartx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * MainActivity
 *
 * The dashboard shown after successful login. Displays the user's email,
 * a system status card, and navigation buttons to Settings, Sensors, and Logout.
 */
class MainActivity : AppCompatActivity() {

    private companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Display user's email
        val userEmail = auth.currentUser?.email ?: "Not signed in"
        findViewById<TextView>(R.id.userEmailText).text = userEmail
        Log.d(TAG, "Displaying user: $userEmail")

        // Logout button
        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            Log.d(TAG, "Logout tapped")
            auth.signOut()
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Settings button — opens SettingsActivity
        findViewById<Button>(R.id.settingsButton).setOnClickListener {
            Log.d(TAG, "Settings tapped")
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // View Sensors button — opens SensorsActivity
        findViewById<Button>(R.id.viewSensorsButton).setOnClickListener {
            Log.d(TAG, "View Sensors tapped")
            startActivity(Intent(this, SensorsActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}