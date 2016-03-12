package in.multico.controller;

import javafx.application.Platform;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
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
