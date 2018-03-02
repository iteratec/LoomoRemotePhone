package de.iteratec.slab.segway.remote.phone.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import de.iteratec.slab.segway.remote.phone.util.MovementListenerFactory;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class RawControlFragment extends JoyStickControllerFragment {
    private static final String TAG = "RawControlFragment";

    private JoystickView joystickSpeed;
    private JoystickView joystickDirection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View joystickFragment = inflater.inflate(R.layout.fragment_raw_control, container, false);

        joystickSpeed = joystickFragment.findViewById(R.id.joystick_speed);
        joystickDirection = joystickFragment.findViewById(R.id.joystick_direction);

        joystickSpeed.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_SPEED));
        joystickSpeed.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_SPEED));

        joystickDirection.setOnMoveListener(MovementListenerFactory.getJoystickMoveListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));
        joystickDirection.setOnTouchListener(MovementListenerFactory.getJoyStickReleaseListener(this, MovementListenerFactory.JOYSTICK_DIRECTION));

        return joystickFragment;
    }

}
