package com.example.multiplyeverywhere.ui.settings

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.LoginActivity
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var user: User? = null

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = DataBaseController(requireContext(), null)
        var userName = SharedPreferencesHelper(requireContext()).getUserName()
        user = db.getUserByName(userName)

        val addUserButton = view.findViewById<Button>(R.id.button_add_new_user)
        addUserButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.alert_dialog_text_add_new_user_title))
            builder.setMessage(getString(R.string.alert_dialog_text_add_new_user_question))

            builder.setPositiveButton(getString(R.string.yes_button_text)) { dialog, which ->
                val registration = Intent(requireContext(), LoginActivity::class.java)
                startActivity(registration)
                requireActivity().finish()
            }
            builder.setNegativeButton(getString(R.string.no_button_text)) { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}