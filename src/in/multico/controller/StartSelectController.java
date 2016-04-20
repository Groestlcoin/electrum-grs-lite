package in.multico.controller;

import com.coinomi.core.wallet.Wallet;
import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */

public class StartSelectController extends ControllerBased{

    public Label rez;

    public static final int SEED_ENTROPY_DEFAULT = 192;

    @FXML
    private void createWallet(ActionEvent event) {
        Main.refreshLayout(event, new ShowMnemonicController().getLayout(), new ShowListener() {
            @Override
            public void onShow(Object controller) {
                List<String> mm = Wallet.generateMnemonic(SEED_ENTROPY_DEFAULT);
                ShowMnemonicController c = (ShowMnemonicController) controller;
                c.setMnemonic(mm);
                c.setNextCheck();
            }
        });
    }

    @FXML
    private void importWallet(ActionEvent event) {
        Main.refreshLayout(event, new CheckMnemonicController().getLayout());
    }

    @Override
    public String getLayout() {
        return "start_select.fxml";
    }

    @Override
    protected void refresh() {

    }
}
