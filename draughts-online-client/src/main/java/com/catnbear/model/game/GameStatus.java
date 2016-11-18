package com.catnbear.model.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

/**
 * Game's status class. Tells what status the game currently has.
 */
public class GameStatus {

    /**
     * Contains all the possible game states.
     */
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

    /**
     * Game status' state.
     */
    private StatusState statusState;

    /**
     * Flag indicating whether status changed.
     */
    private BooleanProperty statusChangeFlag;

    /**
     * Class' constructor.
     */
    GameStatus() {
        statusState = StatusState.NOT_STARTED;
        statusChangeFlag = new SimpleBooleanProperty(false);
    }

    /**
     * Adds a listener for tracking potential status changes.
     * @param booleanChangeListener New listener.
     */
    public void addListener(ChangeListener<Boolean> booleanChangeListener) {
        statusChangeFlag.addListener(booleanChangeListener);
    }

    /**
     * Getter of status' state.
     * @return Status' state.
     */
    public StatusState getStatusState() {
        return statusState;
    }

    /**
     * Setter of status' state.
     * @param statusState New status' state.
     */
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
