package com.menesdurak.e_ticaret_uygulamasi.presentation.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.menesdurak.e_ticaret_uygulamasi.R
import com.menesdurak.e_ticaret_uygulamasi.common.AppLanguageProvider
import com.menesdurak.e_ticaret_uygulamasi.databinding.FragmentUserBinding
import java.util.Locale

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLanguage.setOnClickListener {
            val currentLanguage = Locale.getDefault().language

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Language")
            val customAlertLayout =
                layoutInflater.inflate(R.layout.alert_dialog_language_picker, null)

            when (currentLanguage) {
                "en" -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility = View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility = View.GONE
                }
                "tr" -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility = View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility = View.GONE
                }
                else -> {
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckEnglish).visibility = View.VISIBLE
                    customAlertLayout.findViewById<ImageButton>(R.id.ivCheckTurkish).visibility = View.GONE
                }

            }

            builder.setView(customAlertLayout)
            builder.create()
            builder.show()
            customAlertLayout.findViewById<TextView>(R.id.tvTurkish).setOnClickListener {
                AppLanguageProvider(requireContext()).setLocale("tr-TR")
            }
            customAlertLayout.findViewById<TextView>(R.id.tvEnglish).setOnClickListener {
                AppLanguageProvider(requireContext()).setLocale("en-EN")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}