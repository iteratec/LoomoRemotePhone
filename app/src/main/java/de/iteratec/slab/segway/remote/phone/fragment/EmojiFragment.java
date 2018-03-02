package de.iteratec.slab.segway.remote.phone.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

import java.util.ArrayList;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.base.RemoteFragment;
import de.iteratec.slab.segway.remote.phone.service.ConnectionService;
import de.iteratec.slab.segway.remote.phone.util.CommandStringFactory;

/**
 * Created by mss on 17.01.18.
 */

public class EmojiFragment extends RemoteFragment implements Button.OnClickListener {

    private static final String TAG = "EmojiFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View emojiFragment = inflater.inflate(R.layout.fragment_emoji, container, false);

        // force emoji on screen on
        String[] message = {"settings", SettingsFragment.KEY_EMOJI, "true"};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));

        // add click listener to all buttons on fragment
        ArrayList<View> buttons1 = emojiFragment.findViewById(R.id.emoji_layout_1).getTouchables();
        ArrayList<View> buttons2 = emojiFragment.findViewById(R.id.emoji_layout_2).getTouchables();
        ArrayList<View> buttons3 = emojiFragment.findViewById(R.id.emoji_layout_3).getTouchables();
        for (View v : buttons1) {
            v.setOnClickListener(this);
        }
        for (View v : buttons2) {
            v.setOnClickListener(this);
        }
        for (View v : buttons3) {
            v.setOnClickListener(this);
        }

        return emojiFragment;
    }


    @Override
    public void onClick(View v) {
        String[] message = {"emoji", v.getTag().toString()};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }

}
