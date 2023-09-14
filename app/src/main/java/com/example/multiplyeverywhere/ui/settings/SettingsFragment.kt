package com.example.multiplyeverywhere.ui.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.multiplyeverywhere.LoadingActivity
import com.example.multiplyeverywhere.LoginActivity
import com.example.multiplyeverywhere.MainActivity
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.FragmentSettingsBinding
import java.util.Locale


class SettingsFragment : Fragment() {
    private var user: User? = null

    private var _binding: FragmentSettingsBinding? = null

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

        //add user button listener
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
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_border_rectangle)
            dialog.show()
        }
        //create change user spinner
        val arrayListUsers = db.getUserNames()

        val changeUserSpinner = view.findViewById<Spinner>(R.id.spinner_change_profile)
        val adapterChangeUser: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayListUsers)
        changeUserSpinner.adapter = adapterChangeUser
        adapterChangeUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val indexOfSelectedUser = arrayListUsers.indexOf(userName)
        changeUserSpinner.setSelection(indexOfSelectedUser)

        adapterChangeUser.notifyDataSetChanged()

        changeUserSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedName = parent.getItemAtPosition(position).toString()
                preferences.setUserName(selectedName)
                userName = selectedName
                adapterChangeUser.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        //delete user button listener
        val buttonDeleteUser = view.findViewById<Button>(R.id.button_delete_user)
        buttonDeleteUser.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.title_delete_user)+" "+ userName)
            builder.setMessage(getString(R.string.question_delete_user))

            builder.setPositiveButton(getString(R.string.yes_button_text)) { dialog, which ->
                if (arrayListUsers.size > 1){
                    preferences.setUserName(arrayListUsers.get(0))
                    db.deleteUserByName(userName)
                    arrayListUsers.remove(userName)
                    userName = preferences.getUserName()
                    changeUserSpinner.setSelection(0)
                    adapterChangeUser.notifyDataSetChanged()
                } else{
                    db.deleteUserByName(userName)
                    arrayListUsers.remove(userName)
                    preferences.setUserName("")
                    val registratioActivity = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(registratioActivity)
                    requireActivity().finish()
                }
            }
            builder.setNegativeButton(getString(R.string.no_button_text)) { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()

            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_border_rectangle)
            dialog.show()
        }

        // create spinner change language
        val arrayListLanguages = ArrayList<String>()
        arrayListLanguages.add(getString(R.string.language_item_english))
        arrayListLanguages.add(getString(R.string.language_item_kazakh))
        arrayListLanguages.add(getString(R.string.language_item_russian))

        val changeLanguageSpinner = view.findViewById<Spinner>(R.id.spinner_language)
        val adapterLanguage: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayListLanguages)
        changeLanguageSpinner.adapter = adapterLanguage
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        var currentLanguage = preferences.getLanguage()
        var indexCurrentLanguage : Int = 0
        when (currentLanguage) {
            "en"-> {
                indexCurrentLanguage = arrayListLanguages.indexOf(getString(R.string.language_item_english))
            }
            "ru"->{
                indexCurrentLanguage = arrayListLanguages.indexOf(getString(R.string.language_item_russian))
            }
           "kk"-> {
               indexCurrentLanguage = arrayListLanguages.indexOf(getString(R.string.language_item_kazakh))
            }
        }
        changeLanguageSpinner.setSelection(indexCurrentLanguage)

        changeLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                var selectedLanguage = parent.getItemAtPosition(position).toString()

               when (selectedLanguage) {
                    getString(R.string.language_item_english) -> {
                        changeLanguage("en")
                        preferences.setLanguage("en")
                        selectedLanguage =  getString(R.string.language_item_english)
                        if (currentLanguage!="en"){
                            val loadingActivity = Intent(requireContext(), LoadingActivity::class.java)
                            startActivity(loadingActivity)
                            requireActivity().finish()
                        }
                    }
                   getString(R.string.language_item_kazakh)->{
                       changeLanguage("kk")
                       preferences.setLanguage("kk")
                       selectedLanguage =  getString(R.string.language_item_kazakh)
                       if (currentLanguage!="kk"){
                           val loadingActivity = Intent(requireContext(), LoadingActivity::class.java)
                           startActivity(loadingActivity)
                           requireActivity().finish()
                       }
                   }
                   getString(R.string.language_item_russian)-> {
                       changeLanguage("ru")
                       preferences.setLanguage("ru")
                       selectedLanguage =  getString(R.string.language_item_russian)
                       if (currentLanguage!="ru"){
                            val loadingActivity = Intent(requireContext(), LoadingActivity::class.java)
                           startActivity(loadingActivity)
                           requireActivity().finish()
                       }
                   }

               }
                currentLanguage = preferences.getLanguage()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        // create theme sets
        val themeImage = view.findViewById<ImageView>(R.id.theme_picture)

        val switchTheme = view.findViewById<Switch>(R.id.switch_theme)
        val isNightThemeEnabled = preferences.getTheme() == "night"
        switchTheme.isChecked = isNightThemeEnabled
        if (isNightThemeEnabled) {
            themeImage.setImageResource(R.drawable.moon)
        } else {
            themeImage.setImageResource(R.drawable.sun)
        }

        switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferences.setTheme("night")
                themeImage.setImageResource(R.drawable.moon)
                val loadingActivity = Intent(requireContext(), LoadingActivity::class.java)
                startActivity(loadingActivity)
                requireActivity().finish()
            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferences.setTheme("day")
                themeImage.setImageResource(R.drawable.sun)
                val loadingActivity = Intent(requireContext(), LoadingActivity::class.java)
                startActivity(loadingActivity)
                requireActivity().finish()
            }
        }

        // create sound switch

        val switchSound = view.findViewById<Switch>(R.id.switch_sound)
        val audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val isChecked = preferences.getSoundSetttings() == "true" ||  preferences.getSoundSetttings() == ""

        switchSound.isChecked = isChecked

        switchSound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
                preferences.setSoundSetttings("true")
                Toast.makeText(requireContext(), "Sound enabled", Toast.LENGTH_SHORT).show()
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                preferences.setSoundSetttings("false")
                Toast.makeText(requireContext(), "Sound disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun changeLanguage(language: String){
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration();
        configuration.setLocale(locale);
        requireContext().resources.updateConfiguration(configuration,requireContext().resources.displayMetrics)
    }
}