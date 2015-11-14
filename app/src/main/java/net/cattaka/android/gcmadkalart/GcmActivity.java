package net.cattaka.android.gcmadkalart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by cattaka on 15/11/14.
 */
public class GcmActivity extends Activity implements View.OnClickListener {

    GcmActivity me = this;
    TextView mTokenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        // Find views
        mTokenText = (TextView) findViewById(R.id.text_token);

        // Bind event handlers
        findViewById(R.id.button_get_gcm_token).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_get_gcm_token) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    InstanceID instanceID = InstanceID.getInstance(me);
                    try {
                        String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                        subscriber.onNext(token);
                        subscriber.onCompleted();
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mTokenText.setText(s);
                            Log.d("Cattaka", s);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(me, throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
