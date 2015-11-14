
package net.cattaka.android.gcmadkalart;

import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import net.cattaka.android.gcmadkalart.data.Command;
import net.cattaka.android.gcmadkalart.data.MyPacket;
import net.cattaka.android.gcmadkalart.data.MyPacketFactory;
import net.cattaka.android.gcmadkalart.data.OpCode;
import net.cattaka.android.gcmadkalart.entity.Action;
import net.cattaka.libgeppa.AdkPassiveGeppaService;
import net.cattaka.libgeppa.data.ConnectionState;
import net.cattaka.libgeppa.data.PacketWrapper;

import java.util.List;

public class GeppaServiceEx extends AdkPassiveGeppaService<MyPacket> {
    public static final String EXTRA_ACTIONS = "actions";

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

        List<Action> actions = intent.getParcelableArrayListExtra(EXTRA_ACTIONS);
        if (actions != null) {
            for (Action action:actions) {
                doAction(action);
            }
        }
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

    private void doAction(Action action) {
        if (action.getCommand() == Command.LED) {
            byte[] bs = new byte[] {
                    (byte) (0xFF & (action.getColor() >> 16)),
                    (byte) (0xFF & (action.getColor() >> 8)),
                    (byte) (0xFF & (action.getColor())),
            };
            MyPacket packet = new MyPacket(OpCode.LED_RGB, bs.length, bs);
            sendPacket(new PacketWrapper(packet));
        }
    }
}
