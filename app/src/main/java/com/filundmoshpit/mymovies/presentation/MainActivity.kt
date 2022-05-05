package com.filundmoshpit.mymovies.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.filundmoshpit.mymovies.ModifiedNavigationUI
import com.filundmoshpit.mymovies.R
import com.filundmoshpit.mymovies.Settings
import com.filundmoshpit.mymovies.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import java.util.*

/*
TODO:
    -ADD OMDB?
    Add constructors to internal/external movies
    Add injection to settings
*/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var settingsService: Settings
    }

    override fun attachBaseContext(baseContext: Context?) {
        var newContext = baseContext

        if (baseContext != null) {
            settingsService = Settings(PreferenceManager.getDefaultSharedPreferences(newContext))

            //Load settings

            //Dark theme
            val systemDarkTheme = when (baseContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            val currentDarkTheme = settingsService.darkTheme

            //Change night mode
            if (systemDarkTheme != currentDarkTheme) {
                val nightMode = if (currentDarkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(nightMode)
            }

            //Language
            val systemLanguage = Locale.getDefault().language
            val currentLanguage = settingsService.language

            //Change language
            if (systemLanguage != currentLanguage) {
                val configuration = baseContext.resources.configuration
                configuration.setLocale(Locale.forLanguageTag(currentLanguage))

                newContext = baseContext.createConfigurationContext(configuration)
            }
        }

        super.attachBaseContext(newContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contextComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigation
        val navigationController =
            (supportFragmentManager.findFragmentById(R.id.navigation_host_fragment) as NavHostFragment).navController

        ModifiedNavigationUI.setupWithNavController(
            binding.navigationMenu as NavigationBarView,
            navigationController
        )
    }
}
