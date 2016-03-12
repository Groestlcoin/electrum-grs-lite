package in.multico.controller;

import in.multico.listener.CloseListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */

public class MsgController extends ControllerBased{

    @FXML public Label rez;

    public void setMsg(String msg){
        rez.setText(msg);
    }
    private CloseListener cl;

    @FXML
    private void close(ActionEvent event) {
        if (cl != null) cl.onClose();
        Scene scene = ((Control)event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    public void setCloseListener(CloseListener cl) {
        this.cl = cl;
    }

    @Override
    protected void refresh() {

    }
}
