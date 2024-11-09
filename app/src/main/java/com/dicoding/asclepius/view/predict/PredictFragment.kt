package com.dicoding.asclepius.view.predict

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.dicoding.asclepius.data.safeargs.PredictionResult
import com.dicoding.asclepius.databinding.FragmentPredictBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications

class PredictFragment : Fragment() {

    private lateinit var _binding: FragmentPredictBinding
    private val binding get() = _binding

    private var currentImageUri: Uri? = null
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            listener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread { showToast(error) }
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    requireActivity().runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val result = it[0].categories[0]
                                moveToResult(result)
                            }
                        }
                    }
                }
            }
        )

        try {
            imageClassifierHelper.classifyStaticImage(currentImageUri!!)
        } catch (e: Exception) {
            Log.e("PredictFragment", "Error classifying image", e)

            if (e is NullPointerException) {
                showToast("Choose an image first")
            } else {
                showToast("Error classifying image")
            }
        }
    }

    private fun moveToResult(result: Category) {
        val resultPrediction = PredictionResult(
            label = result.label,
            confidenceScore = result.score,
            uri = currentImageUri
        )

        val toResultActivity = PredictFragmentDirections.actionNavigationPredictToResultActivity(resultPrediction)
        findNavController().navigate(toResultActivity)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}