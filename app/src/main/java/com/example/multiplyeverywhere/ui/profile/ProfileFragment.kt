package com.example.multiplyeverywhere.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), OnProfileImageUpdatedListener {
   private var user: User? = null

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = DataBaseController(requireContext(), null)
        var userName = SharedPreferencesHelper(requireContext()).getUserName()
        user = db.getUserByName(userName)

        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textViewUserName: TextView = binding.textHome
        val textViewUserLevel: TextView = binding.levelText

        profileViewModel.setText(userName, countUserLevel(user?.level) , requireContext())

        profileViewModel.userName.observe(viewLifecycleOwner) {
            textViewUserName.text = it
        }
        profileViewModel.userLevel.observe(viewLifecycleOwner) {
            textViewUserLevel.text = it
        }

        val resourceId = resources.getIdentifier(user?.profileImage, "drawable", context?.packageName)
        binding.profileImage.setImageResource(resourceId)

        binding.editProfilePhotoButton.setOnClickListener {
            val fragmentManager = childFragmentManager
            val dialog = DialogEditProfilePhoto()
            dialog.setProfileImageUpdatedListener(this)
            dialog.show(fragmentManager, "EditProfilePhotoDialog")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProfileImageUpdated(imageName: String) {
        val resourceId = resources.getIdentifier(imageName, "drawable", context?.packageName)
        binding.profileImage.setImageResource(resourceId)

        val cx =  (binding.profileImage.right -  binding.profileImage.left )/2
        val cy =  (binding.profileImage.right -  binding.profileImage.left )/2

        val finalRadius =     binding.profileImage.width

        val anim = ViewAnimationUtils.createCircularReveal(    binding.profileImage, cx, cy, 0f, finalRadius.toFloat())
        anim.duration=1000
        anim.start()
    }
    private fun countUserLevel(points: Int?) : String {
        if (points!! < 1000){
            setProgressCircle(points/(1000/100))
            return "1"
        } else if (points < 2500 ){
            setProgressCircle(points-1000/(1500/100))
            return "2"
        }else if (points < 4500){
            setProgressCircle(points-2500/(2000/100))
            return "3"
        }else if (points < 8000){
            setProgressCircle(points-4500/(3500/100))
            return "4"
        } else if (points < 16000){
            setProgressCircle(points-8000/(8000/100))
            return "5"
        }else if (points < 32000){
            setProgressCircle(points-16000/(16000/100))
            return "6"
        }

        return ""
    }
    private fun setProgressCircle(persent : Int){
        val progressBarCircle = binding.progressBar
        progressBarCircle.setProgress(persent)
    }

    override fun onResume() {
        super.onResume()
        val db = DataBaseController(requireContext(), null)
        var userName = SharedPreferencesHelper(requireContext()).getUserName()
        user = db.getUserByName(userName)

        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val textViewUserName: TextView = binding.textHome
        val textViewUserLevel: TextView = binding.levelText

        profileViewModel.setText(userName, countUserLevel(user?.level) , requireContext())

        profileViewModel.userName.observe(viewLifecycleOwner) {
            textViewUserName.text = it
        }
        profileViewModel.userLevel.observe(viewLifecycleOwner) {
            textViewUserLevel.text = it
        }

        val resourceId = resources.getIdentifier(user?.profileImage, "drawable", context?.packageName)
        binding.profileImage.setImageResource(resourceId)

        binding.editProfilePhotoButton.setOnClickListener {
            val fragmentManager = childFragmentManager
            val dialog = DialogEditProfilePhoto()
            dialog.setProfileImageUpdatedListener(this)
            dialog.show(fragmentManager, "EditProfilePhotoDialog")
        }
    }
}