package org.jokbal.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import org.jokbal.R;

public class JPusherPreferenceActivity extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}