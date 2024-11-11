package com.dicoding.asclepius.view.predict

import android.content.Intent
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.asclepius.data.safeargs.PredictionResult
import com.dicoding.asclepius.databinding.FragmentPredictBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (requestCode == UCrop.REQUEST_CROP && resultCode == android.app.Activity.RESULT_OK) {
                val croppedImageUri = UCrop.getOutput(data)
                currentImageUri = croppedImageUri
                showImage()
            } else if (requestCode == UCrop.REQUEST_CROP && resultCode == android.app.Activity.RESULT_CANCELED) {
                val cropError = UCrop.getError(data)
                showToast("Crop error: ${cropError?.message}")
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startCrop(uri)
//            showImage()
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

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File.createTempFile("cropped", ".jpg"))
        UCrop.of(uri, destinationUri).start(requireContext(), this)
    }

    private fun analyzeImage() {
        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            listener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    requireActivity().runOnUiThread { showToast(error) }
                }

                override fun onResults(results: List<Classifications>?) {
                    requireActivity().runOnUiThread {
                        results?.let {
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                val result = it[0].categories[0]

                                val factory = ViewModelFactory.getInstance(requireContext())
                                val viewModel: PredictViewModel by viewModels { factory }

                                viewModel.savePredictionResult(result.label, result.score, currentImageUri.toString())
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
                showToast("Failed to classifying image: ${e.message}")
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