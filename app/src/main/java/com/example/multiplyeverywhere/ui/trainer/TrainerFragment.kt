package com.example.multiplyeverywhere.ui.trainer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.databinding.FragmentTrainerBinding
import com.example.multiplyeverywhere.game.GameActivity

class TrainerFragment : Fragment() {

    private var _binding: FragmentTrainerBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val trainerViewModel =
                ViewModelProvider(this).get(TrainerViewModel::class.java)

        _binding = FragmentTrainerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val startButton = view.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            val game = Intent(requireContext(), GameActivity::class.java)
            startActivity(game)
        }
    }
}