package com.example.multiplyeverywhere.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
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

        profileViewModel.setText(userName, user?.level.toString(), requireContext())

        profileViewModel.userName.observe(viewLifecycleOwner) {
            textViewUserName.text = it
        }
        profileViewModel.userLevel.observe(viewLifecycleOwner) {
            textViewUserLevel.text = it
        }

        val resourceId = resources.getIdentifier(user?.profileImage, context.packageName )
        binding.profileImage.setImageResource(resourceId)

        binding.editProfilePhotoButton.setOnClickListener {
            val fragmentManager = childFragmentManager
            val dialog = DialogEditProfilePhoto()
            dialog.show(fragmentManager, "EditProfilePhotoDialog")
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}