package de.iteratec.slab.segway.remote.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import de.iteratec.slab.segway.remote.phone.service.ConnectionCallback;
import de.iteratec.slab.segway.remote.phone.service.ConnectionService;

public class ConnectActivity extends AppCompatActivity implements ConnectionCallback {

    private static final String TAG = ConnectActivity.class.getName();

    private ConnectionService connectionService;
    private EditText ipInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ipInput = (EditText) findViewById(R.id.ip_input);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectionService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connectionService.unregisterCallback(this);
        unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "Connected to service. Redirecting to NavigationActivity");
            connectionService = ((ConnectionService.LocalBinder) iBinder).getService();
            connectionService.registerCallback(ConnectActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "Disconnected from service");
            connectionService.unregisterCallback(ConnectActivity.this);
            connectionService = null;
        }
    };

    @Override
    public void onConnected() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDisconnected() {
        try {
            Toast.makeText(connectionService, "Disconnected from Loomo", Toast.LENGTH_SHORT).show();
        } catch (RuntimeException ignored) {

        }
    }


    public void connectToRobot(View view) {
        if (serviceConnection == null) {
            Toast.makeText(connectionService, "Service is null", Toast.LENGTH_SHORT).show();
        } else {
            connectionService.connectToRobot(ipInput.getText().toString().trim());
        }
    }

    public void skipToController(View view) {
        connectionService.setConnectionSkipped(true);
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

}
