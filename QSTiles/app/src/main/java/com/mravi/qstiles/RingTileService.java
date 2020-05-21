package com.mravi.qstiles;

import android.content.Intent;
import android.content.Context;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.os.Vibrator;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class RingTileService extends TileService {
    private BroadcastReceiver a = new b();

    class a implements Runnable {
        a() {
        }
        public void run() {
            RingTileService.this.d();
        }
    }

    class b extends BroadcastReceiver {
        b() {
        }

        public void onReceive(Context context, Intent intent) {
            RingTileService.this.e();
        }
    }

    private AudioManager a() {
        return (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    private int b() {
        int ringerMode = a().getRingerMode();
        return ringerMode != 0 ? ringerMode != 1 ? 2130903043 : 2130903042 : 2130903044;
    }

    private String c() {
        int ringerMode = a().getRingerMode();
        return ringerMode != 0 ? ringerMode != 1 ? ringerMode != 2 ? "Unknown" : "Normal" : "Vibrate" : "Silent";
    }

    private void d() {
        startActivityAndCollapse(new Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"));
    }

    private void e() {
        Tile qsTile = getQsTile();
        if (qsTile != null) {
            qsTile.setContentDescription(c());
            qsTile.setLabel(c());
            qsTile.setIcon(Icon.createWithResource(this, b()));
            qsTile.updateTile();
            qsTile.setState(2);
        }
    }

    private void f() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }

    public void onClick() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (notificationManager.isNotificationPolicyAccessGranted()) {
                int ringerMode = a().getRingerMode();
                int i = 2;
                if (ringerMode == 1) {
                    i = 0;
                } else if (ringerMode == 2) {
                    f();
                    i = 1;
                }
                a().setRingerMode(i);
            } else if (isLocked()) {
                unlockAndRun(new a());
            } else {
                d();
            }
        }
    }

    public void onStartListening() {
        e();
        registerReceiver(this.a, new IntentFilter("android.media.RINGER_MODE_CHANGED"));
    }

    public void onStopListening() {
        unregisterReceiver(this.a);
    }

    public void onTileAdded() {
    }

    public void onTileRemoved() {
    }
}

