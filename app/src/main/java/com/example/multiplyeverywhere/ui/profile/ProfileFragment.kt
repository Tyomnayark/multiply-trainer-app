package com.example.multiplyeverywhere.ui.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multiplyeverywhere.R
import com.example.multiplyeverywhere.ScoreRecord
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.multiplyeverywhere.SharedPreferencesHelper
import com.example.multiplyeverywhere.User
import com.example.multiplyeverywhere.database.DataBaseController
import com.example.multiplyeverywhere.databinding.FragmentProfileBinding
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.common.util.DataUtils
import java.lang.reflect.Modifier

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

        val F  = countUserLevel(user?.points)+1.0
            setProgressCircle(F, F.toInt())
        profileViewModel.setText(userName, F.toInt().toString() , requireContext())

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


        val weeklyScoreRecords = db.getScoreRecordsForUser(userName)
        val weeklyData = getWeeklyScoreData(weeklyScoreRecords)

        val barChart = binding.barChart

        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.setDrawGridLines(false)
        barChart.setBackgroundColor(Color.TRANSPARENT)

        barChart.xAxis.textSize = 12f
        barChart.axisLeft.textSize = 12f
        barChart.setHighlightPerTapEnabled(false)
        barChart.setPinchZoom(false)
        barChart.setDragEnabled(false)
        barChart.setScaleEnabled(false)
        barChart.setScaleXEnabled(false)
        barChart.setScaleYEnabled(false)

        val legend = barChart.legend
        legend.textSize = 12f

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(weeklyData.map { it.first }.toTypedArray())

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawLabels(true)

        val leftAxis = barChart.axisLeft
        leftAxis.textSize = 12f
        leftAxis.textColor = resources.getColor(R.color.black)
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
            }
        }
        val maxPoints = weeklyData.maxByOrNull { it.second }?.second ?: 0
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = maxPoints.toFloat()*1.5f

        val rightAxis = barChart.axisRight
        rightAxis.isEnabled = false

        val entries = weeklyData.mapIndexed { index, (date, points) ->
            BarEntry(index.toFloat(), points)
        }

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.setDrawValues(false)

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false



        barChart.invalidate()

        val noResultsYetText = binding.noResultsYetText
        if (weeklyScoreRecords.size==0){
            noResultsYetText.text = getString(R.string.no_results_yet)
        }else{
            noResultsYetText.text=""
        }

        return root
    }
    fun getWeeklyScoreData(scoreRecords: List<ScoreRecord>): List<Pair<String, Float>> {

        val weeklyData = mutableMapOf<String, Float>()
        for (record in scoreRecords) {
            val date = record.date ?: continue
            val earnedPoints = record.earnedPoints.toFloat()

            val currentPoints = weeklyData[date] ?: 0f
            weeklyData[date] = currentPoints + earnedPoints
        }

        val resultData = weeklyData.toList()

        return resultData
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
    private fun countUserLevel(points: Int?) : Double {
        val a = 100.0
        val b = 900.0
        val c = points!!.toDouble() * -1.0

        val discriminant = b * b - 4 * a * c!!

        if (discriminant < 0) {
            return 0.0
        }
        val sqrtDiscriminant = Math.sqrt(discriminant)

        val f1 = (-b + sqrtDiscriminant) / (2 * a)
        val f2 = (-b - sqrtDiscriminant) / (2 * a)

        return if (f1 > 0) f1 else if (f2 > 0) f2 else 0.0
    }
    private fun setProgressCircle(F: Double, level: Int ){
        val progressBarCircle = binding.progressBar
        progressBarCircle.setProgress(((F-(level.toDouble()))*100).toInt())
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

        val F  = countUserLevel(user?.points)+1.0
        setProgressCircle(F, F.toInt())
        profileViewModel.setText(userName, F.toInt().toString() , requireContext())

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