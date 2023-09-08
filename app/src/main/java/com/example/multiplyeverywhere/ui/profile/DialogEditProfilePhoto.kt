package com.example.multiplyeverywhere.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.multiplyeverywhere.R
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
            changeImage(it)
        }
        image2?.setOnClickListener {
            changeImage(it)
        }
        image3?.setOnClickListener {
            changeImage(it)
        }
        image4?.setOnClickListener {
            changeImage(it)
        }
        image5?.setOnClickListener {
            changeImage(it)
        }
        image6?.setOnClickListener {
            changeImage(it)
        }


        return dialog
    }
    private fun changeImage(image: View?) {

    }
}
