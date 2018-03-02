package de.iteratec.slab.segway.remote.phone.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import com.segway.robot.mobile.sdk.connectivity.StringMessage;

import de.iteratec.slab.segway.remote.phone.R;
import de.iteratec.slab.segway.remote.phone.fragment.base.RemoteFragment;
import de.iteratec.slab.segway.remote.phone.util.CommandStringFactory;

/**
 * Created by mss on 24.01.18.
 */

public class MovementGridFragment extends RemoteFragment implements Button.OnClickListener {

    private static final String TAG = "MovementGridFragment";

    private GridLayout gridLayout;
    private Button resetButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_grid, container, false);

        gridLayout = layout.findViewById(R.id.grid_gridlayout);
        resetButton = layout.findViewById(R.id.grid_reset_button);
        resetButton.setOnClickListener(resetOnClick);

        for (int x = 2; x > -3; x--) {
            for (int y = 2; y > -3; y--) {
                Button button = new Button(layout.getContext());
                if (x == 0 && y == 0) {
                    button.setText("O");
                } else {
                    button.setText("X");
                }
                button.setTag(x + ";" + y);
                button.setOnClickListener(this);
                gridLayout.addView(button);
            }
        }

        String[] message = {"grid", "reset"};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));

        return layout;
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        String[] split = tag.split(";");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);

        String command = "grid_move;" + x + ";" + y;
        Log.d(TAG, "command: " + command);
        String[] message = {"grid_move", String.valueOf(x), String.valueOf(y)};
        getLoomoService().send(CommandStringFactory.getStringMessage(message));
    }


    private View.OnClickListener resetOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String[] message = {"grid_move", "reset"};
            getLoomoService().send(CommandStringFactory.getStringMessage(message));
        }
    };
}
