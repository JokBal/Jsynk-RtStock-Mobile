package org.jokbal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import org.jokbal.R;
import org.jokbal.adapter.ChannelListAdapter;
import org.jokbal.manager.PusherManager;
import org.jokbal.model.JChannel;

import java.util.ArrayList;

public class MainActivity extends Activity implements ChannelEventListener
{
    private static PusherManager pusherManager = null;
    private ArrayList<JChannel> channelList;
    private ChannelListAdapter channelListAdapter;
    private ListView channelListView;

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
        channelList = new ArrayList<JChannel>();
        channelListAdapter = new ChannelListAdapter(this, R.layout.stocklist_row, channelList);
        channelListView.setAdapter(channelListAdapter);
        subscribe(new JChannel("KT"));
        subscribe(new JChannel("SK"));
        subscribe(new JChannel("SAMSUNG"));
        subscribe(new JChannel("LG"));
        subscribe(new JChannel("SOMA"));
        subscribe(new JChannel("NIPA"));
        subscribe(new JChannel("CAPTCHA"));
        subscribe(new JChannel("Mac"));
        subscribe(new JChannel("LOL"));
        subscribe(new JChannel("Apple"));
        subscribe(new JChannel("Govern"));
        subscribe(new JChannel("Park"));
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
    private JChannel getChannel(String name)
    {
        for(int i=0; i<channelList.size(); i++)
        {
            if(channelList.get(i).getName().equals(name)) return channelList.get(i);
        }
        return null;
    }


    public void subscribe(JChannel jChannel)
    {
        if(jChannel != null && jChannel.isCheckd())
        {
            try
            {
                pusherManager.getPuhser().subscribe(jChannel.getName(), this).bind(jChannel.getName(), this);
            }catch (Exception e)
            {

            }
        }

    }

    public void unsubscribe(JChannel jChannel)
    {
        if(jChannel !=null && !jChannel.isCheckd())
        {
            try
            {
                pusherManager.getPuhser().unsubscribe(jChannel.getName());
            }catch (Exception e)
            {

            }
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
            JChannel jChannel = getChannel(channelName);
            if(jChannel == null)
            {
                channelList.add(new JChannel(data));
            }
            else
            {
                jChannel.setStock(data);
                jChannel.setChanged(true);
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
