package in.multico.controller;

import in.multico.Main;
import in.multico.Settings;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
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
    @FXML public CheckBox spendUnconfirm;
    @FXML public CheckBox refreshAddr;
    private Settings settings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settings = Settings.getInstanse();
        acrd.setExpandedPane(security_page);
        spendUnconfirm.setSelected(settings.isAllowSpendUnconfirmed());
        refreshAddr.setSelected(settings.isAlwaysRefreshAddr());
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
        settings.setAllowSpendUnconfirmed(spendUnconfirm.isSelected());
    }

    public void back(ActionEvent event) {
        Main.refreshLayout(event, "main.fxml");
    }

    @Override
    protected void refresh() {

    }

    public void togleRefreshAddr(ActionEvent event) {
        settings.setAlwaysRefreshAddr(refreshAddr.isSelected());
    }
}
