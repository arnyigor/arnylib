package com.arny.arnylib.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.arny.arnylib.utils.DroidUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = DroidUtils.getNetworkInfo(context);

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}