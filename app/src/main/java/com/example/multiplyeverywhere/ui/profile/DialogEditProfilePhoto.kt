package com.example.multiplyeverywhere.ui.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
        return super.onCreateDialog(savedInstanceState)
    }
}
