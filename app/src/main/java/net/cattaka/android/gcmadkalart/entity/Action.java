package net.cattaka.android.gcmadkalart.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import net.cattaka.android.gcmadkalart.data.Command;
import net.cattaka.util.cathandsgendroid.annotation.DataModel;

/**
 * Created by cattaka on 15/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DataModel(genDbFunc = false, genParcelFunc = true)
public class Action implements Parcelable {
    public static final Creator<Action> CREATOR = ActionCatHands.CREATOR;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ActionCatHands.writeToParcel(this, parcel, i);
    }

    @JsonDeserialize(using = Command.Deserializer.class)
    private Command command;
    private int color;
    private int interval;
    private String text;

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
