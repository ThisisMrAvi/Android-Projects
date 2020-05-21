package com.mravi.qstiles;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    class tilecreate implements ServiceConnection {
        tilecreate() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    private void notificationAccess() {
        startActivity(new Intent("android.settings.NOT"));
    }

    public void requestPermissionClicked(View view) {
        notificationAccess();
    }

    public void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (!notificationManager.isNotificationPolicyAccessGranted()) {

            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}