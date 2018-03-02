package de.iteratec.slab.segway.remote.phone.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.segway.robot.mobile.sdk.connectivity.MobileMessageRouter;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.baseconnectivity.Message;
import com.segway.robot.sdk.baseconnectivity.MessageConnection;
import com.segway.robot.sdk.baseconnectivity.MessageRouter;

import java.util.ArrayList;

import de.iteratec.slab.segway.remote.phone.util.CommandStringFactory;

public class ConnectionService extends Service {

    private static final String TAG = ConnectionService.class.getName();

    private final IBinder binder = new LocalBinder();

    private MobileMessageRouter mobileMessageRouter;
    private MessageConnection messageConnection;

    private static ConnectionService instance;

    private boolean connectionSkipped = false;

    public boolean isConnectionSkipped() {
        return connectionSkipped;
    }

    public void setConnectionSkipped(boolean connectionSkipped) {
        this.connectionSkipped = connectionSkipped;
    }

    public static void setInstance(ConnectionService connectionService) {

        instance = connectionService;
    }

    public static ConnectionService getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ConnectionService should be already instantiated");
        }

        return instance;
    }

    public void sendSound(String speak) {
        try {
            Log.i(TAG, "Trying to say: " + speak);
            String[] message = {"speak", speak};
            messageConnection.sendMessage(CommandStringFactory.getStringMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void send(Message message) {
        if (connectionSkipped) {
            Log.d(TAG, "Connection was skipped");
        } else {
            try {
                Log.d(TAG, "Sending command with content: " + message.getContent().toString());
                messageConnection.sendMessage(message);
            } catch (Exception e) {
                Log.e(TAG, "message send failed for message: " + message.toString(), e);
            }
        }
    }

    private ArrayList<ConnectionCallback> callbackList = new ArrayList<>();
    private ArrayList<ByteMessageReceiver> byteMessageReceivers = new ArrayList<>();

    public void registerByteMessageReceiver(ByteMessageReceiver byteMessageReceiver) {
        byteMessageReceivers.add(byteMessageReceiver);
    }

    public void unregisterByteMessageReceiver(ByteMessageReceiver byteMessageReceiver) {
        byteMessageReceivers.remove(byteMessageReceiver);
    }

    public class LocalBinder extends Binder {
        public ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    public void registerCallback(ConnectionCallback callbackInstance) {
        callbackList.add(callbackInstance);
    }

    public void unregisterCallback(ConnectionCallback callbackInstance) {
        callbackList.remove(callbackInstance);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "ConnectionService onBind called");
        if (mobileMessageRouter == null) {
            mobileMessageRouter = MobileMessageRouter.getInstance();
        }
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "ConnectionService onUnbind called");
        if (callbackList.size() == 0) {
            disconnectFromLoomo();
        }
        return super.onUnbind(intent);
    }

    public void connectToRobot(String ip) {
        Log.d(TAG, "ConnectionService connectToRobot called");
        Log.i(TAG, "Connecting to IP: " + ip);

        try {
            Log.d(TAG, "setting connection IP");
            mobileMessageRouter.setConnectionIp(ip);
            Log.d(TAG, "Binding Loomo message service");
            mobileMessageRouter.bindService(this, bindStateListener);
        } catch (Exception e) {
            Log.e(TAG, "Exception during connection", e);
        }

    }

    private ServiceBinder.BindStateListener bindStateListener = new ServiceBinder.BindStateListener() {
        @Override
        public void onBind() {
            Log.d(TAG, "bindStateListener onBind called");
            try {
                mobileMessageRouter.register(messageConnectionListener);
            } catch (Exception e) {
                Log.e(TAG, "Exception during registering connectionListener");
            }
        }

        @Override
        public void onUnbind(String reason) {
            Log.d(TAG, "bindStateListener onUnBind called: " + reason);
        }
    };


    private MessageRouter.MessageConnectionListener messageConnectionListener = new MessageRouter.MessageConnectionListener() {
        @Override
        public void onConnectionCreated(final MessageConnection connection) {
            Log.d(TAG, "messageConnectionListener onConnectionCreated called");
            Log.i(TAG, "Loomo connection created to " + connection.getName());
            messageConnection = connection;
            try {
                Log.d(TAG, "Setting messageConnection listeners");
                messageConnection.setListeners(connectionStateListener, messageListener);
            } catch (Exception e) {
                Log.e(TAG, "Exception during setting messageConnection listeners");
            }
        }
    };

    private MessageConnection.ConnectionStateListener connectionStateListener = new MessageConnection.ConnectionStateListener() {
        @Override
        public void onOpened() {
            Log.d(TAG, "connectionStateListener onOpened called: " + messageConnection.getName());
            for (ConnectionCallback callback : callbackList) {
                callback.onConnected();
            }
        }

        @Override
        public void onClosed(String error) {
            Log.d(TAG, "connectionStateListener onClosed called: " + error + ";name=" + messageConnection.getName());
            for (ConnectionCallback callback : callbackList) {
                callback.onDisconnected();
            }
        }
    };

    private MessageConnection.MessageListener messageListener = new MessageConnection.MessageListener() {
        @Override
        public void onMessageSentError(Message message, String error) {
            Log.d(TAG, "messageListener onMessageSentError: " + error + " during message: " + message);
        }

        @Override
        public void onMessageSent(Message message) {
            Log.d(TAG, "messageListener onMessageSent: " + message + " was sent successfully!");
        }

        @Override
        public void onMessageReceived(Message message) {
            Log.d(TAG, "messageListener onMessageReceived: " + message);
            if (message.getContent() instanceof byte[]) {
                Log.d(TAG, "Byte message received. Sending frame to receivers");
                for (ByteMessageReceiver receiver : byteMessageReceivers) {
                    receiver.handleByteMessage((byte[]) message.getContent());
                }
            } else {
                Log.i(TAG, "Received string message: " + message.getContent());
            }
        }
    };

    public void disconnectFromLoomo() {
        Log.d(TAG, "ConnectionService disconnectFromLoomo called");
        try {
            mobileMessageRouter.unregister();
        } catch (IllegalStateException e) {
            Log.d(TAG, "MessageConnectionListener is not yet registered. Skipping unregistration.");
        }
        try {
            mobileMessageRouter.unbindService();
        } catch (IllegalStateException e) {
            Log.d(TAG, "MobileMessageRouter is not bount. Skipping unbind.");
        }
    }

}
