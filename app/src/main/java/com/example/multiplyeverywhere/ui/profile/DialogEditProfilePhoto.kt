package com.example.multiplyeverywhere.ui.profile

import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.DialogEditProfileImageBinding

class DialogEditProfilePhoto : DialogFragment() {
    private var _binding: DialogEditProfileImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileImageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_border_rectangle)
        val image1 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image1)
        val image2 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image2)
        val image3 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image3)
        val image4 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image4)
        val image5 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image5)
        val image6 = view?.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.image6)

        val imageClickListener = View.OnClickListener { v ->
            when (v?.id) {
                R.id.image1 -> handleImageClick("cat")
                R.id.image2 -> handleImageClick("cat2")
                R.id.image3 -> handleImageClick("cat3")
                R.id.image4 -> handleImageClick("cat4")
                R.id.image5 -> handleImageClick("cat5")
                R.id.image6 -> handleImageClick("cat6")
            }
        }

        image1?.setOnClickListener(imageClickListener)
        image2?.setOnClickListener(imageClickListener)
        image3?.setOnClickListener(imageClickListener)
        image4?.setOnClickListener(imageClickListener)
        image5?.setOnClickListener(imageClickListener)
        image6?.setOnClickListener(imageClickListener)

        return dialog
    }
    private fun handleImageClick(imageName: String) {
        val db = DataBaseController(requireContext(), null)
        val userName = SharedPreferencesHelper(requireContext()).getUserName()

        val updated = db.updateUserProfileImage(userName, imageName)

        if (updated) {
            dialog?.dismiss()
        } else {
        }
    }

}
