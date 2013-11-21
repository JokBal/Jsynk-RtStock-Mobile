package org.jokbal.manager;

import android.content.Context;
import android.util.Log;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import org.jokbal.utils.PreferencesManager;

public class PusherManager implements ConnectionEventListener
{
    private Pusher pusher = null;
    private static PusherManager pusherManager = null;

    public PusherManager(){}
    public static PusherManager getInstance(){
        if(pusherManager == null)
        {
            synchronized (PusherManager.class)
            {
                if(pusherManager == null)
                {
                    pusherManager = new PusherManager();
                }
            }
        }
        return pusherManager;
    }

    public void connect(Context context)
    {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        PusherOptions options = new PusherOptions();
        String host = preferencesManager.get("websocket_host", "j3.link-to-rink.com");
        int port = preferencesManager.get("websocket_port", 8000);
        if(!host.equals(""))
        {
            options.setHost(host);
        }
        options.setWsPort(port).setEncrypted(false);
        Log.d("jpusher", "port :" + port);
        pusher = new Pusher("8817c5eeccfb1ea2d1c6", options);
        if(pusher.getConnection().getState() == ConnectionState.DISCONNECTED)
        {
            pusher.connect(this, ConnectionState.ALL);
        }
    }

    public void disconnect()
    {
        if(pusher != null) pusher.disconnect();
    }

    public Pusher getPuhser()
    {
        return pusher;
    }
    @Override
    public void onConnectionStateChange(ConnectionStateChange connectionStateChange)
    {
        String msg = String.format("Connection state changed from [%s] to [%s]", connectionStateChange.getPreviousState(), connectionStateChange.getCurrentState());
        Log.d("jpusher", msg);
    }

    @Override
    public void onError(String message, String code, Exception e)
    {
        String msg = String.format("Connection error: [%s] [%s] [%s]", message, code, e);
        Log.d("jpusher", msg);
    }
}
