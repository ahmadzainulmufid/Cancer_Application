package com.dicoding.asclepius.ui.prediction

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentPredictionBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class PredictionFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.apply {
            analyzeButton.setOnClickListener {
                currentImageUri?.let {
                    analyzeImage(it)
                } ?: showToast(getString(R.string.empty_image_uri))
            }
            galleryButton.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            startCrop(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    // memulai crop
    private fun startCrop(imageUri: Uri) {
        val uniqueFileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, uniqueFileName))

        val uCropIntent = UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(224, 224)
            .getIntent(requireContext())

        cropImageResultLauncher.launch(uCropIntent)
    }

    // Menggunakan ActivityResultLauncher untuk menangani hasil cropping
    private val cropImageResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                currentImageUri = it
                showImage()
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError: Throwable? = UCrop.getError(result.data!!)
            cropError?.let {
                showToast(getString(R.string.image_load_failed))
            }
        }
    }


    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
            binding.analyzeButton.visibility = View.VISIBLE
        }
    }

    // Analyze the selected image using TensorFlow Lite
    private fun analyzeImage(imageUri: Uri) {
        val imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?) {
                    val resultString = results?.joinToString("\n") {
                        val label = it.categories[0].label
                        val score = (it.categories[0].score * 100).toInt()
                        "$label: $score%"
                    } ?: getString(R.string.classification_failed)

                    moveToResult(imageUri, resultString)
                }
            }
        )

        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    private fun moveToResult(imageUri: Uri, result: String) {
        val intent = Intent(requireActivity(), ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
