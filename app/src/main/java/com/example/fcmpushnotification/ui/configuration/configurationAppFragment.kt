package com.example.fcmpushnotification.ui.configuration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fcmpushnotification.R
import com.example.fcmpushnotification.databinding.FragmentConfigurationBinding

class configurationAppFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_configuration, container, false)

        val nightModeSwitch = view.findViewById<SwitchCompat>(R.id.nightModeSwitch)

        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Modo nocturno activado
                // Aquí puedes cambiar el tema de la aplicación o realizar otras acciones
                Log.d("RESPONDIENDO DESDE BTN","HOLA SI")
            } else {
                Log.d("RESPONDIENDO NO BTN","HOLA NO")
                // Modo nocturno desactivado
                // Aquí puedes volver al tema original de la aplicación o realizar otras acciones
            }
        }

        val slideshowViewModel =
            ViewModelProvider(this).get(configurationApp::class.java)

        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConfiguration
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}