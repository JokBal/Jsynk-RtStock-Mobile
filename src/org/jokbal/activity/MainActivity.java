package org.jokbal.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import com.pusher.client.channel.ChannelEventListener;
import org.jokbal.R;
import org.jokbal.adapter.ChannelListAdapter;
import org.jokbal.manager.PusherManager;
import org.jokbal.model.Channel;

import java.util.ArrayList;

public class MainActivity extends Activity implements ChannelEventListener
{
    private static PusherManager pusherManager = null;
    private ArrayList<Channel> channelList;
    private ChannelListAdapter channelListAdapter;
    private ListView channelListView;
    private static Animation anim;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize()
    {
        pusherManager = PusherManager.getInstance();
        pusherManager.connect(this);
        channelListView = (ListView)findViewById(R.id.lv_channellist);
        channelList = new ArrayList<Channel>();
        channelListAdapter = new ChannelListAdapter(this, R.layout.stocklist_row, channelList);
        channelListView.setAdapter(channelListAdapter);
        anim = AnimationUtils.loadAnimation(this, R.anim.listview_row_animation);
        subscribe("KT");
        subscribe("SK");
        subscribe("SAMSUNG");
        subscribe("LG");
        subscribe("SOMA");
        subscribe("NIPA");
        subscribe("CAPTCHA");
        subscribe("Mac");
        subscribe("LOL");
        subscribe("Apple");
        subscribe("Govern");
        subscribe("Park");
        channelListAdapter.notifyDataSetChanged();
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


    private int getChannelPosition(String name)
    {
        for(int i=0; i<channelList.size(); i++)
        {
            if(channelList.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    public void subscribe(String channelName)
    {
        pusherManager.getPuhser().subscribe(channelName, this).bind(channelName, this);
    }

    public void unsubscribe(String channelName)
    {
        pusherManager.getPuhser().unsubscribe(channelName);
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
        if(data != null && !data.equals(""))
        {
            new ApplyEventTask().execute(channelName, data);
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        initialize();
    }

    @Override
    public void onPause() {
        super.onPause();
        pusherManager.getPuhser().disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pusherManager.getPuhser().disconnect();
    }

    class ApplyEventTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String channelName = params[0];
            String data = params[1];
           int position = getChannelPosition(channelName);
            if(position == -1)
            {
                channelList.add(new Channel(data));
            }
            else
            {
                Channel channel = channelList.get(position);
                channel.setStock(data);
                channel.setChanged(true);
            }

            return channelName;
        }

        @Override
        protected void onPostExecute(String result) {
            channelListAdapter.notifyDataSetChanged();

            super.onPostExecute(result);
        }

    }
}
