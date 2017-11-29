package com.arny.arnylib.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.arny.arnylib.utils.DroidUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = DroidUtils.getNetworkInfo(context);
        Log.i(NetworkChangeReceiver.class.getSimpleName(), "onReceive: status:"  +status);
    }
}