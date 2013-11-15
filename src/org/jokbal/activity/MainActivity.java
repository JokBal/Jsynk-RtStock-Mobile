package org.jokbal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.pusher.client.channel.ChannelEventListener;
import org.jokbal.R;
import org.jokbal.manager.PusherManager;

public class MainActivity extends Activity implements ChannelEventListener
{
    private static PusherManager pusherManager = null;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize()
    {
        pusherManager = PusherManager.getInstance();
        pusherManager.setChannelEventListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.actionbar_setting_btn:
            {
                startActivity(new Intent(this, JPusherPreferenceActivity.class));
                return true;
            }
            case R.id.actionbar_connect:
            {
                pusherManager.connect(this);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSubscriptionSucceeded(String channelName)
    {
        String msg = String.format("Subscription succeeded for [%s]", channelName);
        Log.d("jpusher", msg);
    }

    @Override
    public void onEvent(String channelName, String eventName, String data)
    {
        String msg = String.format("Event received: [%s] [%s] [%s]", channelName, eventName, data);
        Log.d("jpusher", msg);
    }
}