package com.jaredrummler.android.colorpicker.demo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.jaredrummler.android.colorpicker.ColorPreference;

public class DemoFragment extends PreferenceFragmentCompat {

  private static final String DIALOG_FRAGMENT_TAG =
          "android.support.v7.preference.PreferenceFragment.DIALOG";

  private static final String TAG = "DemoFragment";

  private static final String KEY_DEFAULT_COLOR = "default_color";

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.main);

    // Example showing how we can get the new color when it is changed:
    ColorPreference colorPreference = (ColorPreference) findPreference(KEY_DEFAULT_COLOR);
    colorPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (KEY_DEFAULT_COLOR.equals(preference.getKey())) {
          String newDefaultColor = Integer.toHexString((int) newValue);
          Log.d(TAG, "New default color is: #" + newDefaultColor);
        }
        return true;
      }
    });
  }

  @Override
  public void onDisplayPreferenceDialog(Preference preference) {
    // check if dialog is already showing
    if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
      return;
    }

    if (preference instanceof ColorPreference) {
      final DialogFragment f = ((ColorPreference)preference).createDialog();
      f.setTargetFragment(this, 0);
      f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
    } else {
      super.onDisplayPreferenceDialog(preference);
    }
  }
}
