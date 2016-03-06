package in.multico.controller;

import javafx.application.Platform;

/**
 * Created by vp
 * on 06.03.16.
 */
public abstract class ControllerBased {

    public void doRefresh() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    protected abstract void refresh();
}
