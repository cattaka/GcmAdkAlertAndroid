
package net.cattaka.android.gcmadkalart;

import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import net.cattaka.android.gcmadkalart.data.MyPacket;
import net.cattaka.android.gcmadkalart.data.MyPacketFactory;
import net.cattaka.libgeppa.AdkPassiveGeppaService;
import net.cattaka.libgeppa.data.ConnectionState;

public class GeppaServiceEx extends AdkPassiveGeppaService<MyPacket> {

    private WakeLock mWakeLock;

    public GeppaServiceEx() {
        super(new MyPacketFactory());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    protected void onReceivePacket(MyPacket packet) {
        super.onReceivePacket(packet);
        // No op
    }

    @Override
    protected void onConnectionStateChanged(ConnectionState state) {
        super.onConnectionStateChanged(state);
//        if (state == ConnectionState.CONNECTED) {
//        }
    }
}
