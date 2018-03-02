package de.iteratec.slab.segway.remote.phone.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import de.iteratec.slab.segway.remote.phone.util.MovementListenerFactory;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class HeadControlFragment extends JoyStickControllerFragment {

    private static final String TAG = "HeadControlFragment";

    private JoystickView joystickYaw;
    private JoystickView joystickPitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View joystickFragment = inflater.inflate(R.layout.fragment_head_control, container, false);

        joystickYaw = joystickFragment.findViewById(R.id.joystick_head_yaw);
        joystickYaw.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_YAW));
        joystickYaw.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_YAW));

        joystickPitch = joystickFragment.findViewById(R.id.joystick_head_pitch);
        joystickPitch.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_PITCH));
        joystickPitch.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_PITCH));

        setupButtonsListeners(joystickFragment);

        return joystickFragment;
    }

    private void setupButtonsListeners(View joystickFragment) {
        Button clearButton = joystickFragment.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHeadClearCommand();
            }
        });

        final Button modeButton = joystickFragment.findViewById(R.id.mode_button);
        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smoothMode = !smoothMode;
                if (smoothMode) {
                    modeButton.setText(R.string.head_fragment_mode_button_smooth);
                } else {
                    modeButton.setText(R.string.head_fragment_mode_button_orientation);
                }
            }
        });
    }


}
