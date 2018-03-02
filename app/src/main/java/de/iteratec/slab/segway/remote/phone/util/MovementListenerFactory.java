package de.iteratec.slab.segway.remote.phone.util;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import de.iteratec.slab.segway.remote.phone.fragment.base.JoyStickControllerFragment;
import io.github.controlwear.virtual.joystick.android.JoystickView;

/**
 * Created by mss on 24.01.18.
 */

public class MovementListenerFactory {

    public static final int JOYSTICK_SPEED = 1;
    public static final int JOYSTICK_DIRECTION = 2;
    public static final int JOYSTICK_PITCH = 3;
    public static final int JOYSTICK_YAW = 4;

    /**
     * Returns a OnTouchListener for a Loomo Joystick.
     * <p>
     * These are used to clear the position if a joystick is released.
     *
     * @param joystickController
     * @return
     */
    public static View.OnTouchListener getJoyStickReleaseListener(final JoyStickControllerFragment joystickController, int type) {
        View.OnTouchListener listener = null;

        switch (type) {
            case JOYSTICK_SPEED:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.speedAngle = 0;
                            joystickController.speedStrength = 0;
                            joystickController.sendMoveCommand(true);
                        }
                        return false;
                    }
                };
                break;
            case JOYSTICK_DIRECTION:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.directionAngle = 0;
                            joystickController.directionStrength = 0;
                            joystickController.sendMoveCommand(true);
                        }
                        return false;
                    }
                };
                break;
            case JOYSTICK_PITCH:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.pitchAngle = 0;
                            joystickController.pitchStrength = 0;
                            if (!joystickController.smoothMode) {
                                joystickController.sendHeadStopOrientation();
                            }
                        }
                        return false;
                    }
                };
                break;

            case JOYSTICK_YAW:
                listener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            joystickController.yawAngle = 0;
                            joystickController.yawStrength = 0;
                            if (!joystickController.smoothMode) {
                                joystickController.sendHeadStopOrientation();
                            }
                        }
                        return false;
                    }
                };
                break;
        }

        return listener;
    }

    public static JoystickView.OnMoveListener getJoystickMoveListener(final JoyStickControllerFragment fragment, int type) {
        JoystickView.OnMoveListener listener = null;

        switch (type) {
            case JOYSTICK_SPEED:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.speedAngle = angle;
                        fragment.speedStrength = strength;
                        fragment.sendMoveCommand(false);
                    }
                };
                break;
            case JOYSTICK_DIRECTION:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.directionAngle = angle;
                        fragment.directionStrength = strength;
                        fragment.sendMoveCommand(false);
                    }
                };
                break;
            case JOYSTICK_PITCH:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.pitchAngle = angle;
                        fragment.pitchStrength = strength;
                        fragment.sendHeadCommand(false);
                    }
                };
                break;
            case JOYSTICK_YAW:
                listener = new JoystickView.OnMoveListener() {
                    @Override
                    public void onMove(int angle, int strength) {
                        fragment.yawAngle = angle;
                        fragment.yawStrength = strength;
                        fragment.sendHeadCommand(false);
                    }
                };
                break;

        }

        return listener;
    }
}
