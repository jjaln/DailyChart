package com.jjaln.dailychart.contents.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jjaln.dailychart.R;

public class SettingPreference extends PreferenceFragment {
    SharedPreferences prefs;
    public static boolean isMessage = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isMessage = true;
        addPreferencesFromResource(R.xml.setting_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


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
                Toast.makeText(getActivity(), ""+isMessage, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
