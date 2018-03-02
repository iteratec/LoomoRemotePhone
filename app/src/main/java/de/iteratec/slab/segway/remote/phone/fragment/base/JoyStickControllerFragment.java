package de.iteratec.slab.segway.remote.phone.fragment.base;

import android.util.Log;

import java.util.Arrays;

import de.iteratec.slab.segway.remote.phone.util.CommandStringFactory;

/**
 * Created by mss on 24.01.18.
 */

public abstract class JoyStickControllerFragment extends RemoteFragment {

    public static final String TAG = "JoyStickController";
    private static final int DELTA_COMMAND_SEQUENCE = 100;
    public static final float HALF_OF_PI = 3.14F / 2F;

    public long lastCommandSend = 0;

    public float speedAngle = 0F;
    public float speedStrength = 0F;

    public float directionAngle = 0F;
    public float directionStrength = 0F;

    public float pitchAngle = 0F;
    public float pitchStrength = 0F;

    public float yawAngle = 0F;
    public float yawStrength = 0F;

    public boolean smoothMode = true;

    /**
     * Sends move commands to Loomo.
     * Avoids flooding Loomo with commands by respecting a time delta between commands (unless forced).
     *
     * @param force overrides current commands
     */
    public void sendMoveCommand(boolean force) {
        if (force || (System.currentTimeMillis() - lastCommandSend > DELTA_COMMAND_SEQUENCE)) {
            lastCommandSend = System.currentTimeMillis();
            float speed = getNormalizedSpeed();
            float direction = getNormalizedDirection();

            String[] commandToSend = getMoveCommand(speed, direction);
            Log.d(TAG, "sending movement: " + Arrays.toString(commandToSend));
            getLoomoService().send(CommandStringFactory.getStringMessage(commandToSend));
        }
    }

    /**
     * Returns command String, which can be sent to Loomo
     *
     * @param speed     -3 till 3
     * @param direction -1 till 1
     * @return
     */
    private String[] getMoveCommand(float speed, float direction) {
        return new String[]{"move", String.valueOf(speed), String.valueOf(direction)};
    }

    /**
     * Sends head commands to Loomo.
     * Avoids flooding Loomo with commands by respecting a time delta between commands (unless forced).
     *
     * @param force overrides current commands
     */
    public void sendHeadCommand(boolean force) {
        if (force || (System.currentTimeMillis() - lastCommandSend > DELTA_COMMAND_SEQUENCE)) {
            lastCommandSend = System.currentTimeMillis();
            String[] commandToSend = getHeadCommand(this.smoothMode, getPitchValue(), getYawValue());
            Log.d(TAG, "sending head: " + Arrays.toString(commandToSend));
            getLoomoService().send(CommandStringFactory.getStringMessage(commandToSend));
        }
    }


    /**
     * Returns the command String to be send to Loomo.
     *
     * @param smoothMode
     * @param pitchValue
     * @param yawValue
     * @return
     */
    private String[] getHeadCommand(boolean smoothMode, float pitchValue, float yawValue) {

        String command = "smooth";
        if (!smoothMode) {
            command = "orientation";
        }

        return new String[]{"head", command, String.valueOf(pitchValue), String.valueOf(yawValue)};
    }

    public void sendHeadStopOrientation() {
        String[] command = getHeadCommand(false, 0F, 0F);
        getLoomoService().send(CommandStringFactory.getStringMessage(command));
    }

    public void sendHeadClearCommand() {
        String[] command = getHeadCommand(true, 0F, 0F);
        getLoomoService().send(CommandStringFactory.getStringMessage(command));
    }


    /**
     * Returns a value between -3 (backwards max speed) and 3 (forward max speed) based on stored
     * member variables speedStrength and speedAngle
     *
     * @return speed between -3 and 3
     */
    public float getNormalizedSpeed() {
        float speed = speedStrength;
        if (speedAngle > 180) {
            speed = speed * -1F;
        }

        speed = speed / 35F;


        return speed;
    }

    /**
     * Returns a value between -1 (turn right) and 1 (turn left), based on stored
     * member variables directionStrength and directionAngle
     *
     * @return value between -1 and 1
     */
    public float getNormalizedDirection() {
        float direction = (directionAngle);

        //right
        if (directionAngle > 0 && directionAngle <= 90) {
            float delta = directionAngle / 90F;
            direction = -1.0f + delta;
        }
        //left
        else if (directionAngle > 90 && directionAngle <= 180) {
            float delta = (directionAngle - 90F) / 90F;
            direction = 0.0f + delta;
        }
        //right
        else if (directionAngle > 180 && directionAngle <= 270) {
            float delta = (directionAngle - 180F) / 90F;
            direction = 1.0f - delta;
        }
        //left
        else if (directionAngle > 270 && directionAngle <= 360) {
            float delta = (directionAngle - 270F) / 90F;
            direction = 0f - delta;
        }

        return direction;
    }

    /**
     * Returns pitch value that ranges from from
     * totally down = -90° (- Pi/2)
     * totally up = 180° (Pi)
     *
     * @return pitch value to send, based on joystick position
     */
    private float getPitchValue() {
        Log.d(TAG, "pitchStrength: " + pitchStrength);
        Log.d(TAG, "pitchAngle: " + pitchAngle);
        float pitchValue = (pitchStrength * 3.14F) / 100F;
        if (smoothMode) {
            if (pitchAngle > 180) {
                pitchValue = (pitchValue / 2) * -1;
            }
        } else {
            if (pitchAngle > 180) {
                pitchValue = pitchValue * -1;
            }
        }

        return pitchValue;

    }

    private float getYawValue() {
        if (smoothMode) {
            return getYawSmoothMode();
        } else {
            return getYawOrientationMode();
        }
    }

    /**
     * Returns yaw value, which will be interpreted as acceleration, that ranges from
     * -PI (right) to PI (left)
     *
     * @return yaw value to send, based on joystick position
     */
    private float getYawOrientationMode() {
        Log.d(TAG, "yawAngle: " + yawAngle);
        Log.d(TAG, "yawStrength: " + yawStrength);

        float yawValue = (yawStrength * 3.14F) / 100F;

        //right
        if ((yawAngle > 0 && yawAngle <= 90) || (yawAngle > 270 && yawAngle <= 360)) {
            yawValue = yawValue * -1;
        }

        Log.d(TAG, "yawOrientationValue: " + yawValue);

        return yawValue;
    }

    /**
     * Returns yaw value that ranges from from
     * totally right = -150° (-Pi * 0.8)
     * totally left = 150° (Pi * 0.8)
     *
     * @return yaw value to send, based on joystick position
     */
    private float getYawSmoothMode() {
        Log.d(TAG, "yawAngle: " + yawAngle);
        Log.d(TAG, "yawStrength: " + yawStrength);

        float yawValue = 0F;

        //right
        if (yawAngle > 0 && yawAngle <= 90) {
            yawValue = -HALF_OF_PI + ((yawAngle / 90F) * HALF_OF_PI);
        }
        //left
        else if (yawAngle > 90 && yawAngle <= 180) {
            yawValue = 0 + (((yawAngle - 90F) / 90F) * HALF_OF_PI);
        }
        //left
        else if (yawAngle > 180 && yawAngle <= 270) {
            float tempAngle = yawAngle;
            if (yawAngle > 240) {
                tempAngle = 240;
            }

            yawValue = HALF_OF_PI + (((tempAngle - 180) / 90) * HALF_OF_PI);
        }
        //right
        else if (yawAngle > 270 && yawAngle <= 360) {

            float tempAngle = yawAngle;
            if (yawAngle < 300) {
                tempAngle = 300;
            }

            yawValue = -HALF_OF_PI - (((360 - tempAngle) / 90) * HALF_OF_PI);
        } else {
            Log.e(TAG, "Received pitch joystick value, which is out of scope");
        }

        Log.d(TAG, "yawSmoothValue: " + yawValue);

        return yawValue;
    }


}
