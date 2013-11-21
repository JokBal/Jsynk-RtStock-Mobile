package org.jokbal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import org.jokbal.R;
import org.jokbal.activity.MainActivity;
import org.jokbal.manager.PusherManager;
import org.jokbal.model.Channel;

import java.util.ArrayList;

public class ChannelListAdapter extends ArrayAdapter<Channel>
{
    private Context mContext;
    private int mResource;
    private ArrayList<Channel> channelList;
    private LayoutInflater mInflater;
    private static PusherManager pusherManager;

    public ChannelListAdapter(Context context, int layoutResource, ArrayList<Channel> channelList)
    {
        super(context, layoutResource, channelList);
        this.mContext = context;
        this.mResource = layoutResource;
        this.channelList = channelList;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pusherManager = PusherManager.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Channel channel = channelList.get(position);

        if(convertView == null)
        {
            convertView = mInflater.inflate(mResource, null);
        }

        if(channel != null)
        {
            TextView channelNameTextView = (TextView) convertView.findViewById(R.id.tv_channel_name);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.tv_price);
            TextView deltaTextView = (TextView) convertView.findViewById(R.id.tv_delta);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
            ToggleButton channelSwithButton = (ToggleButton) convertView.findViewById(R.id.tb_channel);

            channelNameTextView.setText(channel.getName());
            priceTextView.setText("Price : " + channel.getStock().getPrice());
            deltaTextView.setText("Delta : " + channel.getStock().getDelta());
            timeTextView.setText("Time : " + channel.getStock().getTime());
            channelSwithButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        ((MainActivity)mContext).subscribe(channel.getName());
                        channel.setCheckd(true);
                        Log.d("jsynk", "onCheckedChanged true");
                    }
                    else
                    {
                        ((MainActivity)mContext).unsubscribe(channel.getName());
                        channel.setCheckd(false);
                        Log.d("jsynk", "onCheckedChanged false");
                    }
                }
            });

            convertView.setTag(channel.getName());
            if(channel.isCheckd())
            {
                if(channel.getStock().getDelta() > 0)
                {

                    convertView.setBackgroundColor(Color.argb(255, 46, 203, 113));
                }
                else
                {
                    convertView.setBackgroundColor(Color.argb(255, 231, 76 , 60));
                }
                convertView.setAnimation(null);
                if(channel.isChanged())
                {
                    convertView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.listview_row_animation));
                    channel.setChanged(false);
                }
            }
            else
            {
                convertView.setBackgroundColor(Color.argb(255, 236, 240 , 241));
            }
        }
        return convertView;
    }

}
