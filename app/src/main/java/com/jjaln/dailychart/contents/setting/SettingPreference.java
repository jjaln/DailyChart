package com.jjaln.dailychart.contents.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jjaln.dailychart.R;

public class SettingPreference extends PreferenceFragment {
    SharedPreferences prefs;
    public static boolean isMessage = true;
    public static double coin_rate;
    ListPreference soundPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isMessage = true;
        addPreferencesFromResource(R.xml.setting_preference);
        soundPreference = (ListPreference) findPreference("coin_rate");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!prefs.getString("coin_rate", "").equals("")) {
            soundPreference.setSummary(prefs.getString("coin_rate", "10"));
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.registerOnSharedPreferenceChangeListener(prefListener);

    }

    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("message")) {
                boolean b = prefs.getBoolean("message", false);
                isMessage = b;
                Toast.makeText(getActivity(), "" + isMessage, Toast.LENGTH_SHORT).show();
            } else if (key.equals("coin_rate")) {
                String strate = prefs.getString("coin_rate", "10");
                coin_rate = Double.parseDouble(strate);
                soundPreference.setSummary(prefs.getString("coin_rate", "10"));
                Toast.makeText(getActivity(), "" + strate, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
