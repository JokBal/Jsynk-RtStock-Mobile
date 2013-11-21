package org.jokbal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import org.jokbal.R;
import org.jokbal.activity.MainActivity;
import org.jokbal.model.JChannel;

import java.util.ArrayList;

public class ChannelListAdapter extends ArrayAdapter<JChannel>
{
    private Context mContext;
    private int mResource;
    private ArrayList<JChannel> JChannelList;
    private LayoutInflater mInflater;

    public ChannelListAdapter(Context context, int layoutResource, ArrayList<JChannel> JChannelList)
    {
        super(context, layoutResource, JChannelList);
        this.mContext = context;
        this.mResource = layoutResource;
        this.JChannelList = JChannelList;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final JChannel jChannel = JChannelList.get(position);

        if(convertView == null)
        {
            convertView = mInflater.inflate(mResource, null);
        }

        if(jChannel != null)
        {
            TextView channelNameTextView = (TextView) convertView.findViewById(R.id.tv_channel_name);
            TextView priceTextView = (TextView) convertView.findViewById(R.id.tv_price);
            TextView deltaTextView = (TextView) convertView.findViewById(R.id.tv_delta);
            TextView timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
            ToggleButton channelSwithButton = (ToggleButton) convertView.findViewById(R.id.tb_channel);

            channelNameTextView.setText(jChannel.getName());
            priceTextView.setText("Price : " + jChannel.getStock().getPrice());
            deltaTextView.setText("Delta : " + jChannel.getStock().getDelta());
            timeTextView.setText("Time : " + jChannel.getStock().getTime());
            channelSwithButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                    {
                        jChannel.setCheckd(true);
                        ((MainActivity) mContext).subscribe(jChannel);
                    }
                    else
                    {
                        jChannel.setCheckd(false);
                        ((MainActivity) mContext).unsubscribe(jChannel);
                    }
                }
            });
            if(jChannel.isCheckd())
            {
                if(jChannel.getStock().getDelta() > 0)
                {

                    convertView.setBackgroundColor(Color.argb(255, 46, 203, 113));
                    if(jChannel.isChanged())
                    {
                        convertView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.listview_row_animation_up));
                    }
                }
                else
                {
                    convertView.setBackgroundColor(Color.argb(255, 231, 76 , 60));
                    if(jChannel.isChanged())
                    {
                        convertView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.listview_row_animation_down));
                    }
                }

                if(jChannel.isChanged())
                {
                    jChannel.setChanged(false);
                }
                channelSwithButton.setChecked(true);
            }
            else
            {
                convertView.setBackgroundColor(Color.argb(255, 236, 240 , 241));
                convertView.setAnimation(null);
                channelSwithButton.setChecked(false);
            }
        }
        return convertView;
    }

}
