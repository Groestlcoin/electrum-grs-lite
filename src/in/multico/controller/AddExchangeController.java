package in.multico.controller;

import in.multico.Main;
import in.multico.Settings;
import in.multico.listener.CloseListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class AddExchangeController extends ControllerBased {

    @FXML public TextField key;
    @FXML public TextField secret;
    @FXML public PasswordField pass;

    @Override
    public String getLayout() {
        return "add_exchange.fxml";
    }

    @Override
    protected void refresh() {}

    public void back(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    public void save(final ActionEvent event) {
        if (key.getText().isEmpty() || secret.getText().isEmpty() || pass.getText().isEmpty()) return;
        Settings settings = Settings.getInstanse();
        settings.setPoloKey(key.getText());
        settings.setPoloSecret(secret.getText(), pass.getText());
        Main.showMessage(Main.getLocString("success"), new CloseListener() {
            @Override
            public void onClose() {
                Main.refreshLayout(event, new MainController().getLayout());
            }
        });
    }
}
