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

        image1?.setOnClickListener {
            changeImage(image1)
        }
        image2?.setOnClickListener {
            changeImage(image2)
        }
        image3?.setOnClickListener {
            changeImage(image3)
        }
        image4?.setOnClickListener {
            changeImage(image4)
        }
        image5?.setOnClickListener {
            changeImage(image5)
        }
        image6?.setOnClickListener {
            changeImage(image6)
        }


        return dialog
    }
    private fun changeImage(image: View?) {
        if (image is de.hdodenhof.circleimageview.CircleImageView && image.drawable is BitmapDrawable) {
            val resourceName = getResourceNameFromImageView(image)

            val db = DataBaseController(requireContext(), null)
            val preferences = SharedPreferencesHelper(requireContext())
            val userName = preferences.getUserName()
            val user = db.getUserByName(userName)
            user?.profileImage = resourceName!!
        }
    }

    private fun getResourceNameFromImageView(imageView: de.hdodenhof.circleimageview.CircleImageView): String? {
        val resourceId = imageView.id
        return resources.getResourceName(resourceId)?.toString()
    }
}
