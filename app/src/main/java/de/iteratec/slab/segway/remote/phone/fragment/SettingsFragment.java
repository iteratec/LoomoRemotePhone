package de.iteratec.slab.segway.remote.phone.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.base.PreferenceFragment;
import de.iteratec.slab.segway.remote.phone.service.ConnectionService;
import de.iteratec.slab.segway.remote.phone.util.CommandStringFactory;


/**
 * Created by mss on 17.01.18.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    public static final String KEY_VOICE = "voice_recognition";
    public static final String KEY_EMOJI = "emoji";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean enabled;
        switch (key) {
            case KEY_VOICE:
                enabled = sharedPreferences.getBoolean(key, true);
                this.toggleVoice(enabled);
                break;
            case KEY_EMOJI:
                enabled = sharedPreferences.getBoolean(key, false);
                this.toggleEmoji(enabled);
                break;
        }
    }

    public void toggleVoice(boolean enabled) {
        Log.d(TAG, "toggleVoice called with " + enabled);
        String[] message = {"settings", KEY_VOICE, String.valueOf(enabled)};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));
    }

    public void toggleEmoji(boolean enabled) {
        Log.d(TAG, "toggleEmoji called with " + enabled);
        String[] message = {"settings", KEY_EMOJI, String.valueOf(enabled)};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));
    }
}
