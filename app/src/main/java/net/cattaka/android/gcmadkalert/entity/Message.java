package net.cattaka.android.gcmadkalert.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by cattaka on 15/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
