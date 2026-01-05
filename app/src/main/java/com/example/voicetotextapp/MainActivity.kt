package com.example.voicetotextapp  // Match your package

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var btnVoice: Button
    private lateinit var editTranscript: EditText
    private lateinit var btnSummarize: Button
    private lateinit var tvMinutes: TextView
    private lateinit var progressBar: ProgressBar
    private val REQUEST_RECORD_AUDIO = 200

    private val model by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }

    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the meeting transcript...")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVoice = findViewById(R.id.btn_voice)
        editTranscript = findViewById(R.id.edit_transcript)
        btnSummarize = findViewById(R.id.btn_summarize)
        tvMinutes = findViewById(R.id.tv_minutes)
        progressBar = findViewById(R.id.progress_bar)

        btnVoice.setOnClickListener { startVoiceInput() }
        btnSummarize.setOnClickListener { summarizeTranscript() }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Mic ready!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(speechRecognizerIntent, 100)
        } else {
            checkPermissions()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            results?.get(0)?.let { transcript ->
                editTranscript.setText(transcript)
            }
        }
    }

    private fun summarizeTranscript() {
        val transcript = editTranscript.text.toString()
        if (transcript.isBlank()) {
            Toast.makeText(this, "Enter transcript first (voice or type)!", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = ProgressBar.VISIBLE
        btnSummarize.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prompt = "From this meeting transcript, generate structured minutes:\n• Key discussion points\n• Decisions made\n• Action items (with owners/deadlines if mentioned)\n\nTranscript:\n$transcript"
                val response = model.generateContent(prompt)
                val minutes = response.text ?: "No response."

                withContext(Dispatchers.Main) {
                    tvMinutes.text = minutes
                    progressBar.visibility = ProgressBar.GONE
                    btnSummarize.isEnabled = true
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    tvMinutes.text = "Error: ${e.message}\n\n1. Check GEMINI_API_KEY in local.properties\n2. Internet on?\n3. Free quota?"
                    progressBar.visibility = ProgressBar.GONE
                    btnSummarize.isEnabled = true
                }
            }
        }
    }
}