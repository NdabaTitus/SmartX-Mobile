package za.ac.iie.smartx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * LoginActivity
 *
 * This is the first screen the user sees. It provides a "Sign in with Google"
 * button that authenticates the user via Firebase Authentication using SSO.
 *
 * Lifecycle events are logged for debugging and to demonstrate understanding
 * of the Android activity lifecycle.
 */
class LoginActivity : AppCompatActivity() {

    // Tag for Logcat messages - helps identify logs from this activity
    private companion object {
        const val TAG = "LoginActivity"
        const val RC_SIGN_IN = 9001
    }

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Google Sign-In client
    private lateinit var googleSignInClient: GoogleSignInClient

    // Modern ActivityResult API for handling the sign-in result
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d(TAG, "Sign-in result received: ${result.resultCode}")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG, "Google sign-in successful for: ${account.email}")
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.e(TAG, "Google sign-in failed with code: ${e.statusCode}", e)
            Toast.makeText(this, "Sign-in failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Activity is being created")
        setContentView(R.layout.activity_login)

        // Initialise Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "Firebase Auth initialised")

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "User already signed in: ${currentUser.email}")
            goToMain()
            return
        }

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Wire up the Google Sign-In button
        val googleSignInButton = findViewById<SignInButton>(R.id.googleSignInButton)
        googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        googleSignInButton.setOnClickListener {
            Log.d(TAG, "Sign-in button tapped")
            signIn()
        }
    }

    /**
     * Launches the Google Sign-In intent.
     */
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    /**
     * Takes the Google ID token and exchanges it for a Firebase credential.
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Firebase authentication successful")
                    Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
                    goToMain()
                } else {
                    Log.e(TAG, "Firebase authentication failed", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Navigates to the MainActivity after successful login.
     */
    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity is becoming visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity is now interactive")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity is pausing")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity is no longer visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity is being destroyed")
    }
}