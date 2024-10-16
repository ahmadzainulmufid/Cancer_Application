package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.ui.history.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        result?.let {
            Log.d("Result", "showResult: $it")
            binding.resultText.text = it
        }

        binding.saveButton.setOnClickListener {
            saveToDatabase(imageUri, result)
        }
    }

    private fun saveToDatabase(imageUri: Uri?, result: String?) {
        if (imageUri != null && result != null) {
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val uniqueUri = Uri.parse(imageUri.toString() + "?timestamp=" + System.currentTimeMillis())
            val history = History(
                imageUri = uniqueUri.toString(),
                result = result,
                date = date
            )
            historyViewModel.addHistory(history)
            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
