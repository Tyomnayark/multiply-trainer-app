package com.example.multiplyeverywhere.ui.home

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
import com.example.multiplyeverywhere.databinding.FragmentDashboardBinding
import com.example.multiplyeverywhere.databinding.FragmentHomeBinding
import com.example.multiplyeverywhere.databinding.FragmentNotificationsBinding
import com.example.multiplyeverywhere.ui.dashboard.DashboardViewModel
import com.example.multiplyeverywhere.ui.notifications.NotificationsViewModel

class HomeFragment : Fragment() {
   private var user: User? = null

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = DataBaseController(requireContext(), null)
        var userName = SharedPreferencesHelper(requireContext()).getUserName()
        user = db.getUserByName(userName)

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textViewUserName: TextView = binding.textHome
        val textViewUserLevel: TextView = binding.levelText

        homeViewModel.setText(userName, user?.level.toString(), requireContext())

        homeViewModel.userName.observe(viewLifecycleOwner) {
            textViewUserName.text = it
        }
        homeViewModel.userLevel.observe(viewLifecycleOwner) {
            textViewUserLevel.text = it
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}