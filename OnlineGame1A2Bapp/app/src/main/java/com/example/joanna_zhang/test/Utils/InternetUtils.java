package com.example.joanna_zhang.test.Utils;


import android.content.Context;
import android.net.ConnectivityManager;

public class InternetUtils {

    public boolean hasInternetConnected(Context context){
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }
}
