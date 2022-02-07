package com.filundmoshpit.mymovies.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.filundmoshpit.mymovies.presentation.MainActivity
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.Settings
import com.filundmoshpit.mymovies.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        //Loading settings from shared preferences
        binding.switchDarkTheme.isChecked = MainActivity.settingsService.darkTheme

        when (MainActivity.settingsService.language) {
            Settings.LANGUAGE_ENGLISH -> binding.toggleLanguageEnglish.isChecked = true
            Settings.LANGUAGE_RUSSIAN -> binding.toggleLanguageRussian.isChecked = true
        }

        //UI handlers
        binding.switchDarkTheme.setOnCheckedChangeListener { button, checked ->
            val mode =
                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            //Save to shared preferences
            MainActivity.settingsService.darkTheme = (mode == AppCompatDelegate.MODE_NIGHT_YES)

            AppCompatDelegate.setDefaultNightMode(mode)
        }

        //Language
        binding.toggleLanguage.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val language =
                if (checkedId == R.id.toggleLanguageRussian) Settings.LANGUAGE_RUSSIAN else Settings.LANGUAGE_ENGLISH

            //Save to shared preferences
            MainActivity.settingsService.language = language

            when (language) {
                Settings.LANGUAGE_ENGLISH -> Toast.makeText(
                    context,
                    getString(R.string.setiings_language_changed_message_en),
                    Toast.LENGTH_SHORT
                ).show()

                Settings.LANGUAGE_RUSSIAN -> Toast.makeText(
                    context,
                    getString(R.string.setiings_language_changed_message_ru),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}