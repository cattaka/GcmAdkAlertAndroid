package net.cattaka.android.gcmadkalert;


import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import net.cattaka.android.gcmadkalert.entity.Action;
import net.cattaka.android.gcmadkalert.util.JacksonUtils;

import java.util.ArrayList;
import java.util.List;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String actionsJson = data.getString("actions");
        List<Action> origActions = JacksonUtils.getObjects(actionsJson, Action.class);
        if (origActions != null) {
            ArrayList<Action> actions = new ArrayList<Action>(origActions);
            Intent intent = new Intent(this, GeppaServiceEx.class);
            intent.putExtra(GeppaServiceEx.EXTRA_ACTIONS, actions);
            startService(intent);
        }
    }
}
