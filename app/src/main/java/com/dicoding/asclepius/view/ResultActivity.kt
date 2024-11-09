package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.navArgs
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlin.math.round

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val args: ResultActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Result"

        args.predictionResult.apply {
            binding.apply {
                resultImage.setImageURI(uri)
                resultText.text = getString(R.string.result, label, "${round(confidenceScore*100).toInt()}%")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}