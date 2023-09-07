package com.example.multiplyeverywhere.ui.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.LoginActivity
import com.example.multiplyeverywhere.MainActivity
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
        val preferences = SharedPreferencesHelper(requireContext())
        val db = DataBaseController(requireContext(), null)
        var userName = SharedPreferencesHelper(requireContext()).getUserName()
        user = db.getUserByName(userName)

        val addUserButton = view.findViewById<Button>(R.id.button_add_new_user)

        addUserButton.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.alert_dialog_text_add_new_user_title))
            builder.setMessage(getString(R.string.alert_dialog_text_add_new_user_question))

            builder.setPositiveButton(getString(R.string.yes_button_text)) { dialog, which ->
            val registratioActivity = Intent(requireContext(), LoginActivity::class.java)
                startActivity(registratioActivity)
                requireActivity().finish()
            }
            builder.setNegativeButton(getString(R.string.no_button_text)) { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        val arrayListUsers = db.getUserNames()

        val changeUserSpinner = view.findViewById<Spinner>(R.id.spinner_change_profile)
        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayListUsers)
        changeUserSpinner.adapter = adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val indexOfSelectedUser = arrayListUsers.indexOf(userName)
        changeUserSpinner.setSelection(indexOfSelectedUser)

        adapter.notifyDataSetChanged()

        changeUserSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedName = parent.getItemAtPosition(position).toString()
                preferences.setUserName(selectedName)
                userName = selectedName
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val buttonDeleteUser = view.findViewById<Button>(R.id.button_delete_user)
        buttonDeleteUser.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.title_delete_user)+" "+ userName)
            builder.setMessage(getString(R.string.question_delete_user))

            builder.setPositiveButton(getString(R.string.yes_button_text)) { dialog, which ->

//                val registratioActivity = Intent(requireContext(), LoginActivity::class.java)
//                startActivity(registratioActivity)
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