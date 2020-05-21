package com.mravi.mravi;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.TileService;
import android.widget.Toast;

public class RingService extends TileService {

    /* renamed from: a  reason: collision with root package name */
    private AudioManager audioManager;

    /* renamed from: b  reason: collision with root package name */
    private NotificationManager notificationManager;

    public void a() {
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void b() {
        Intent intent = new Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        a();
        Toast.makeText(this, "To control sound modes we(MrAvi) needs DND permission!", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    public final void c() {
        this.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (this.notificationManager.isNotificationPolicyAccessGranted()) {
            getQsTile().setState(2);
            int ringerMode = this.audioManager.getRingerMode();
            if (ringerMode == 0) {
                g();
            } else if (ringerMode == 1) {
                i();
            } else if (ringerMode == 2) {
                if (this.audioManager.getStreamVolume(3) == 0) {
                    d();
                } else {
                    h();
                }
            }
        } else {
            getQsTile().setState(1);
            getQsTile().updateTile();
        }
    }

    public void d() {
        this.audioManager.setRingerMode(2);
        this.audioManager.setStreamVolume(3, 0, 8);
        getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_music_off));
        getQsTile().setLabel("Media Off");
        getQsTile().updateTile();
    }

    public void e() {
        this.audioManager.setRingerMode(0);
        this.audioManager.setStreamVolume(3, 0, 8);
        g();
    }

    public void f() {
        this.audioManager.setRingerMode(2);
        AudioManager audioManager = this.audioManager;
        audioManager.setStreamVolume(2, audioManager.getStreamMaxVolume(0), 2);
        AudioManager audioManager2 = this.audioManager;
        audioManager2.setStreamVolume(3, audioManager2.getStreamMaxVolume(0), 4);
        h();
    }

    public final void g() {
        getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_mute));
        getQsTile().setLabel("Silent");
        getQsTile().updateTile();
    }

    public final void h() {
        getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_volume_up_white_24dp));
        getQsTile().setLabel("Sound");
        getQsTile().updateTile();
    }

    public final void i() {
        getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_vibration));
        getQsTile().setLabel("Vibrate");
        getQsTile().updateTile();
    }

    public void j() {
        this.audioManager.setRingerMode(1);
        this.audioManager.setStreamVolume(3, 0, 16);
        i();
    }

    public void onClick() {
        super.onClick();
        NotificationManager notificationManager = this.notificationManager;
        if (notificationManager == null) {
            c();
        } else if (!notificationManager.isNotificationPolicyAccessGranted()) {
            getQsTile().setState(1);
            getQsTile().updateTile();
            b();
        } else if (getQsTile().getLabel().equals("Sound")) {
            j();
        } else if (getQsTile().getLabel().equals("Vibrate")) {
            e();
        } else if (getQsTile().getLabel().equals("Silent")) {
            d();
        } else if (getQsTile().getLabel().equals("Media Off")) {
            f();
        }
    }

    public void onStartListening() {
        super.onStartListening();
        c();
    }

    public void onStopListening() {
        super.onStopListening();
        this.audioManager = null;
        this.notificationManager = null;
    }

    public void onTileAdded() {
        super.onTileAdded();
    }

    public void onTileRemoved() {
        super.onTileRemoved();
    }
}