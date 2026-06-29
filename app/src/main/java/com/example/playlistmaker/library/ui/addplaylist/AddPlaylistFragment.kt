package com.example.playlistmaker.library.ui.addplaylist

import android.Manifest
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.library.ui.viewmodels.AddPlaylistViewModel
import com.example.playlistmaker.utils.DimensionsUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.util.Locale
import kotlin.random.Random

class AddPlaylistFragment : Fragment() {
    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private var imagePath: String? = null

    private val viewModel: AddPlaylistViewModel by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (!isGranted) {
            showDialog()
        } else {
            pickPhoto()
        }
    }

    private val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val radius = DimensionsUtils.dpToPixel(8f, binding.root.context)
            Glide.with(requireContext())
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(radius))
                .into(binding.loadImageView)
            imagePath = saveImageToStorage(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)


        setTextWatcher()
        setupClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onCloseFragmentCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleCloseFragment()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onCloseFragmentCallback
        )
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupClickListeners() {
        binding.headerTitle.setOnClickListener {
            handleCloseFragment()
        }
        binding.loadImageFrame.setOnClickListener {
            checkPermissions()
        }
        binding.createPlaylistBtn.setOnClickListener {
            showSnackBar(
                view = it,
                message = getString(R.string.playlistCreatedMessage, binding.titleEditText.text)
            )
            onCreatePlaylistClick()
        }
    }

    private fun showSnackBar(message: String, view: View) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        val customSnackbar = layoutInflater.inflate(R.layout.custom_snackbar, null)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        val layout = snackbar.view as ViewGroup

        val text = customSnackbar.findViewById<TextView>(R.id.snackbar_message)
        text.text = message

        layout.setPadding(0, 0, 0, 0)
        layout.addView(customSnackbar)
        snackbar.show()
    }

    private fun onCreatePlaylistClick() {
        viewModel.addPlaylist(
            title = binding.titleEditText.text?.trim().toString(),
            description = binding.descriptionEditText.text.toString(),
            imagePath = imagePath,
            nowYear = LocalDate.now().year.toString()
        )
        closeFragment()

    }

    private fun setTextWatcher() {
        binding.titleEditText.doOnTextChanged { text, _, _, _ ->
            binding.createPlaylistBtn.isEnabled = text?.isBlank() != true
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions() {
        requestPermissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
    }

    private fun pickPhoto() {
        photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.permissionDialogMessage)
            .setNegativeButton(R.string.permissionNegativeBtn) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.permissionPositiveBtn) { dialog, _ ->
                dialog.dismiss()

                checkPermissions()
            }.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
    }

    private fun handleCloseFragment() {
        if (!binding.titleEditText.text.isNullOrEmpty()
            || !binding.descriptionEditText.text.isNullOrEmpty()
            || binding.loadImageView.drawable != null) {

            showCloseFragmentDialog()
        } else {
            closeFragment()
        }
    }

    private fun showCloseFragmentDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.completePlaylistAdding)
            .setMessage(R.string.lostAllData)
            .setNegativeButton(R.string.cancellation) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.complete) { dialog, _ ->
                dialog.dismiss()
                closeFragment()
            }.show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
            resources.getColor(R.color.yp_blue)
        )
    }

    private fun saveImageToStorage(uri: Uri): String {
        val id = Random.nextInt(10, 1000)
        val name = String.format(Locale.getDefault(), IMAGE_FILE_NAME, id)
        val context = requireContext()

        val directory = File(context.filesDir, IMAGES_DIRECTORY)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, name)

        return try {
            context.contentResolver.openInputStream(uri).use { input ->
                FileOutputStream(file).use { output ->
                    BitmapFactory
                        .decodeStream(input)
                        .compress(Bitmap.CompressFormat.JPEG, 70, output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun closeFragment() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGE_FILE_NAME = "artWork_%d.jpg"
        private const val IMAGES_DIRECTORY = "images"
    }
}