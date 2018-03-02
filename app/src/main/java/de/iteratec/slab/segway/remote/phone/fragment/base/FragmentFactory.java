package de.iteratec.slab.segway.remote.phone.fragment.base;

import android.content.Context;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.EmojiFragment;
import de.iteratec.slab.segway.remote.phone.fragment.HeadControlFragment;
import de.iteratec.slab.segway.remote.phone.fragment.MovementGridFragment;
import de.iteratec.slab.segway.remote.phone.fragment.RawControlFragment;
import de.iteratec.slab.segway.remote.phone.fragment.SettingsFragment;
import de.iteratec.slab.segway.remote.phone.fragment.TextToSpeechFragment;
import de.iteratec.slab.segway.remote.phone.fragment.VisionFragment;

/**
 * Created by mss on 23.02.18.
 */

public class FragmentFactory {

    public static RemoteFragmentInterface getFragment(Context context, int id) {
        RemoteFragmentInterface fragment;
        switch (id) {
            case R.id.raw_control:
                fragment = new RawControlFragment();
                break;
            case R.id.head_control:
                fragment = new HeadControlFragment();
                break;
            case R.id.tts:
                fragment = new TextToSpeechFragment();
                break;
            case R.id.vision:
                fragment = new VisionFragment();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
                break;
            case R.id.emoji:
                fragment = new EmojiFragment();
                break;
            case R.id.movement_grid:
                fragment = new MovementGridFragment();
                break;
            default:
                throw new IllegalArgumentException("Unknown Fragment ID");
        }
        fragment.setTitle(getTitleForFragmentId(context, id));
        return fragment;
    }

    private static String getTitleForFragmentId(Context context, int id) {
        String title;
        switch (id) {
            case R.id.raw_control:
                title = context.getResources().getString(R.string.navigation_fragment_title_raw);
                break;
            case R.id.head_control:
                title = context.getResources().getString(R.string.navigation_fragment_title_head);
                break;
            case R.id.tts:
                title = context.getResources().getString(R.string.navigation_fragment_title_tts);
                break;
            case R.id.vision:
                title = context.getResources().getString(R.string.navigation_fragment_title_vision);
                break;
            case R.id.settings:
                title = context.getResources().getString(R.string.navigation_fragment_title_settings);
                break;
            case R.id.emoji:
                title = context.getResources().getString(R.string.navigation_fragment_title_emoji);
                break;
            case R.id.movement_grid:
                title = context.getResources().getString(R.string.navigation_fragment_title_grid);
                break;
            default:
                throw new IllegalArgumentException("Unknown Fragment ID");
        }
        return title;
    }
}
