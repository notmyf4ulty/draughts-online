package com.catnbear.model.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

public class GameStatus {
    public enum StatusState {
        NOT_STARTED,
        CONNECTING_TO_SERVER,
        JOINING_GAME,
        WAITING_FOR_SECOND_PLAYER,
        WAITING_FOR_TURN,
        TURN,
        LOST,
        WON,
        CONNECTION_ERROR,
        EXIT;
    }

    private StatusState statusState;
    private BooleanProperty statusChangeFlag;

    public GameStatus() {
        statusState = StatusState.NOT_STARTED;
        statusChangeFlag = new SimpleBooleanProperty(false);
    }

    public void addListener(ChangeListener<Boolean> booleanChangeListener) {
        statusChangeFlag.addListener(booleanChangeListener);
    }

    public void removeListener(ChangeListener<Boolean> booleanChangeListener) {
        statusChangeFlag.removeListener(booleanChangeListener);
    }

    public StatusState getStatusState() {
        return statusState;
    }

    void setStatusState(StatusState statusState) {
        if (!(this.statusState.equals(StatusState.CONNECTION_ERROR))) {
            this.statusState = statusState;
            statusChangeFlag.setValue(true);
            statusChangeFlag.setValue(false);
        } else {
            System.out.println(this.statusState);
        }
    }
}
