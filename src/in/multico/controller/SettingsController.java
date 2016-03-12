package in.multico.controller;

import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 05.02.16
 * Time: 09:38
 */
public class SettingsController extends ControllerBased implements Initializable {


    @FXML public TitledPane security_page;
    @FXML public Accordion acrd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acrd.setExpandedPane(security_page);
    }

    public void showMnemonic(ActionEvent event) {
        Main.refreshLayout(event, "check_password.fxml", new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((CheckPasswordController) controller).setNextStepMnemonic();
            }
        });
    }

    public void changePass(ActionEvent event) {
        Main.refreshLayout(event, "check_password.fxml", new ShowListener() {
            @Override
            public void onShow(Object controller) {
                ((CheckPasswordController) controller).setNextChangePass();
            }
        });
    }

    public void togleSpendUnconfirmed(ActionEvent event) {
        Main.showMessage(Main.getLocString("soon"));
    }

    public void back(ActionEvent event) {
        Main.refreshLayout(event, "main.fxml");
    }

    @Override
    protected void refresh() {

    }
}
