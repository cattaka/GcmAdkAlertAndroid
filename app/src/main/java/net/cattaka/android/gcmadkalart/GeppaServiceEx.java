
package net.cattaka.android.gcmadkalart;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.widget.Toast;

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
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private GeppaServiceEx me = this;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    private WakeLock mWakeLock;

    public GeppaServiceEx() {
        super(new MyPacketFactory());

    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, Constants.TAG);

        mHandlerThread = new HandlerThread("Actions");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }

        mHandlerThread.quit();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        {   // From GCM
            List<Action> actions = null;
            if (intent != null) {
                actions = intent.getParcelableArrayListExtra(EXTRA_ACTIONS);
            }
            if (actions != null) {
                for (Action action : actions) {
                    doAction(action);
                }
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

    private void doAction(final Action action) {
        if (action.getCommand() == Command.LED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    {
                        byte[] bs = new byte[]{
                                (byte) (0xFF & (action.getColor() >> 16)),
                                (byte) (0xFF & (action.getColor() >> 8)),
                                (byte) (0xFF & (action.getColor())),
                        };
                        MyPacket packet = new MyPacket(OpCode.LED_RGB, bs.length, bs);
                        sendPacket(new PacketWrapper(packet));
                    }
                    {
                        SystemClock.sleep(action.getInterval());
                    }
                    {
                        byte[] bs = new byte[]{0,0,0};
                        MyPacket packet = new MyPacket(OpCode.LED_RGB, bs.length, bs);
                        sendPacket(new PacketWrapper(packet));
                    }
                }
            });
        } else if (action.getCommand() == Command.WAIT) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    {
                        SystemClock.sleep(action.getInterval());
                    }
                }
            });
        } else if (action.getCommand() == Command.TALK) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(me, String.valueOf(action.getText()), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}
