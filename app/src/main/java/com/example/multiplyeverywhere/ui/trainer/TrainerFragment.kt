package com.example.multiplyeverywhere.ui.trainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.GameActivity
import com.example.multiplyeverywhere.LoginActivity
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.databinding.FragmentSettingsBinding
import com.example.multiplyeverywhere.databinding.FragmentTrainerBinding

class TrainerFragment : Fragment() {

    private var _binding: FragmentTrainerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val level1 = view.findViewById<Button>(R.id.level1)

        level1.setOnClickListener {
            val gameActivity = Intent(requireContext(), GameActivity::class.java)
            startActivity(gameActivity)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}