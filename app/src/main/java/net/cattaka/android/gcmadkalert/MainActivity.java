
package net.cattaka.android.gcmadkalert;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import net.cattaka.android.gcmadkalert.data.MyPacket;
import net.cattaka.android.gcmadkalert.data.OpCode;
import net.cattaka.libgeppa.IPassiveGeppaService;
import net.cattaka.libgeppa.IPassiveGeppaServiceListener;
import net.cattaka.libgeppa.data.PacketWrapper;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int EVENT_ON_RECEIVE_PACKET = 1;

    private static Handler sHandler = new MyHandler(Looper.getMainLooper());
    static class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
//            Object[] objs = (Object[])msg.obj;
//            MainActivity target = (MainActivity)objs[0];
//            if (msg.what == EVENT_ON_RECEIVE_PACKET) {
//                target.onReceivePacket((MyPacket)objs[1]);
//            }
        }
    }

    private MainActivity me = this;

    private IPassiveGeppaService mService;

    private SeekBar mRedSeek;
    private SeekBar mGreenSeek;
    private SeekBar mBlueSeek;

    private IPassiveGeppaServiceListener mGeppaServiceListener = new IPassiveGeppaServiceListener.Stub() {
        public void onConnectionStateChanged(net.cattaka.libgeppa.data.ConnectionState arg0)
                throws android.os.RemoteException {
        }

        public void onReceivePacket(PacketWrapper packetWrapper) throws android.os.RemoteException {
            sHandler.obtainMessage(EVENT_ON_RECEIVE_PACKET, new Object[] {
                    me, packetWrapper.getPacket()
            }).sendToTarget();
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName paramComponentName) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mService = IPassiveGeppaService.Stub.asInterface(binder);
            try {
                mService.registerGeppaServiceListener(mGeppaServiceListener);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRedSeek = (SeekBar) findViewById(R.id.seek_red);
        mGreenSeek = (SeekBar) findViewById(R.id.seek_green);
        mBlueSeek = (SeekBar) findViewById(R.id.seek_blue);

        // Setting event listeners
        findViewById(R.id.button_send_led_rgb).setOnClickListener(this);
        findViewById(R.id.button_gcm_setting).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent serviceIntent = new Intent(this, GeppaServiceEx.class);
        startService(serviceIntent);
        bindService(serviceIntent, mServiceConnection, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_send_led_rgb) {
            byte[] bs = new byte[] {
                    (byte) mRedSeek.getProgress(),
                    (byte) mGreenSeek.getProgress(),
                    (byte) mBlueSeek.getProgress(),
            };
            MyPacket packet = new MyPacket(OpCode.LED_RGB, bs.length, bs);
            try {
                mService.sendPacket(new PacketWrapper(packet));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else if (v.getId() == R.id.button_gcm_setting) {
            Intent intent = new Intent(this, GcmActivity.class);
            startActivity(intent);
        }
    }
}
