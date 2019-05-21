package leaf.prod.app.receiver;

import java.util.Objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import leaf.prod.app.utils.NetworkUtil;

public abstract class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), ConnectivityManager.CONNECTIVITY_ACTION)) {
            switch (NetworkUtil.getNetWorkState(context)) {
                case NETWORK_NONE:
                    doNetWorkNone();
                    break;
                case NETWORK_WIFI:
                    doNetWorkWifi();
                    break;
                case NETWORK_MOBILE:
                    doNetWorkMobile();
                    break;
            }
        }
    }

    public abstract void doNetWorkNone();

    public abstract void doNetWorkWifi();

    public abstract void doNetWorkMobile();
}
