package de.fhe.ai.colivingpilot.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import de.fhe.ai.colivingpilot.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}