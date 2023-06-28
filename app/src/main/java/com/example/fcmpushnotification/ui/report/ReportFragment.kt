package com.example.fcmpushnotification.ui.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ekn.gruzer.gaugelibrary.HalfGauge
import com.ekn.gruzer.gaugelibrary.Range
import com.example.fcmpushnotification.R
import com.example.fcmpushnotification.databinding.FragmentReportBinding

class ReportFragment : Fragment() {


    private var _binding: FragmentReportBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val variableD = false

        val view = inflater.inflate(R.layout.fragment_report, container, false)

        if(!variableD){
            Log.d("FRAMENT DE :","REPORTESSSSS")

            /**
             * CREAR LA FUNCIONALIDAD PARA KPI
             * ***/


            val rangosSecond = Range()
            val rangesSecond1 = Range()
            val rangesSecond2 = Range()
            val halfGaugeOneSecond: HalfGauge = view.findViewById(R.id.IdReport)


            rangosSecond.color = Color.parseColor("#E77D8E")
            rangosSecond.from = 0.0
            rangosSecond.to = 21.5

            rangesSecond1.color = Color.parseColor("#FADE8E")
            rangesSecond1.from = 21.5
            rangesSecond1.to = 43.0

            rangesSecond2.color = Color.parseColor("#88CA9A")
            rangesSecond2.from = 43.0
            rangesSecond2.to = 100.0


            halfGaugeOneSecond.minValue = 0.0
            halfGaugeOneSecond.maxValue = 100.0
            halfGaugeOneSecond.value = 0.0

        }



        return view
    }



   override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}