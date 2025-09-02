package com.example.voicetotextapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Request codes for permissions and intents.
    private val RECORD_AUDIO_REQUEST_CODE = 1
    private val STORAGE_REQUEST_CODE = 2

    // UI elements.
    private lateinit var recordButton: Button
    private lateinit var resultTextView: TextView

    // Speech recognition objects.
    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var recognizerIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find and assign the UI elements.
        recordButton = findViewById(R.id.btn_record)
        resultTextView = findViewById(R.id.text_result)

        // Check and request permissions on app start.
        checkPermissions()

        // Set up the speech recognizer intent.
        setupSpeechRecognizer()

        // Set up the click listener for the button.
        recordButton.setOnClickListener {
            startVoiceRecognition()
        }
    }

    /**
     * Checks for necessary permissions and requests them if not granted.
     */
    private fun checkPermissions() {
        // Check for RECORD_AUDIO permission.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE)
        }

        // Check for WRITE_EXTERNAL_STORAGE permission.
        // Note: For newer Android versions (API 29+), this permission is not needed
        // for private app storage, but it's good practice for older versions.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_REQUEST_CODE)
            }
        }
    }

    /**
     * Handles the result of the permission request dialog.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Audio permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Audio permission denied. Cannot record.", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Sets up the speech recognizer and its listener.
     */
    private fun setupSpeechRecognizer() {
        // Create an intent for voice recognition.
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
        }

        // Get a speech recognizer instance.
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    val message = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No recognition result"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer is busy"
                        SpeechRecognizer.ERROR_SERVER -> "Server error"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                        else -> "Unknown error"
                    }
                    Toast.makeText(this@MainActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (matches != null && matches.isNotEmpty()) {
                        val text = matches[0]
                        resultTextView.text = text // Display the text.
                        saveTextToFile(text) // Save the text to a file.
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    // This method is for handling partial results as the user speaks.
                }

                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            // Handle the case where speech recognition is not available on the device.
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_LONG).show()
            recordButton.isEnabled = false // Disable the button.
        }
    }

    /**
     * Starts the voice recognition process.
     */
    private fun startVoiceRecognition() {
        // Check if the permission is granted before starting.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            speechRecognizer?.startListening(recognizerIntent)
            Toast.makeText(this, "Listening...", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please grant audio recording permission first.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Saves the given text to a file in the app's private external storage.
     * @param text The text to save.
     */
    private fun saveTextToFile(text: String) {
        val fileName = "transcription_${System.currentTimeMillis()}.txt"
        val file = File(getExternalFilesDir(null), fileName)

        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(text.toByteArray())
            }
            Toast.makeText(this, "Text saved to file: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save file.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the speech recognizer resources.
        speechRecognizer?.destroy()
    }
}