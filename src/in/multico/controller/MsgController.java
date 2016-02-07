package in.multico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MsgController {

    public Label rez;

    public void setMsg(String msg){
        rez.setText(msg);
    }

    @FXML
    private void close(ActionEvent event) {
        Scene scene = ((Control)event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
