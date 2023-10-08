package com.tyom.multiplyeverywhere.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tyom.multiplyeverywhere.R
import com.tyom.multiplyeverywhere.SharedPreferencesHelper
import com.tyom.multiplyeverywhere.database.DataBaseController
import com.tyom.multiplyeverywhere.databinding.DialogEditProfileImageBinding

class DialogEditProfilePhoto : DialogFragment() {
    private var _binding: DialogEditProfileImageBinding? = null
    private val binding get() = _binding!!

    private var profileImageUpdatedListener: OnProfileImageUpdatedListener? = null

    fun setProfileImageUpdatedListener(listener: OnProfileImageUpdatedListener) {
        this.profileImageUpdatedListener = listener
    }

    private fun notifyProfileImageUpdated(imageName: String) {
        profileImageUpdatedListener?.onProfileImageUpdated(imageName)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileImageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dialog?.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.rounded_border_rectangle))
        return root
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val image1 = binding.image1
        val image2 = binding.image2
        val image3 = binding.image3
        val image4 = binding.image4
        val image5 = binding.image5
        val image6 = binding.image6

        image1?.bringToFront()
        image2?.bringToFront()
        image3?.bringToFront()
        image4?.bringToFront()
        image5?.bringToFront()
        image6?.bringToFront()


        val imageClickListener = View.OnClickListener { v ->
            val id = v?.id
            if (id != null) {
                when (id) {
                    R.id.image1 -> handleImageClick("cat")
                    R.id.image2 -> handleImageClick("cat2")
                    R.id.image3 -> handleImageClick("cat3")
                    R.id.image4 -> handleImageClick("cat4")
                    R.id.image5 -> handleImageClick("cat5")
                    R.id.image6 -> handleImageClick("cat6")
                }
            }
        }

        image1?.setOnClickListener(imageClickListener)
        image2?.setOnClickListener(imageClickListener)
        image3?.setOnClickListener(imageClickListener)
        image4?.setOnClickListener(imageClickListener)
        image5?.setOnClickListener(imageClickListener)
        image6?.setOnClickListener(imageClickListener)

    }

    private fun handleImageClick(imageName: String) {
        val db = DataBaseController(requireContext(), null)
        val userName = SharedPreferencesHelper(requireContext()).getUserName()

        val updated = db.updateUserProfileImage(userName, imageName)
        if (updated) {
            notifyProfileImageUpdated(imageName)
            dialog?.dismiss()

        } else {
            // TODO: need to define this case
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}
