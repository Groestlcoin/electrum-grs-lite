package in.multico.controller;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;
import com.coinomi.core.wallet.WalletAccount;
import in.multico.Main;
import in.multico.listener.ShowListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import org.bitcoinj.crypto.KeyCrypter;
import org.bitcoinj.wallet.DeterministicSeed;
import org.spongycastle.crypto.params.KeyParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class CheckPasswordController extends ControllerBased{

    @FXML public Label message;
    @FXML public PasswordField pass;
    private CoinType addCoin;

    @Override
    public String getLayout() {
        return "check_password.fxml";
    }

    @Override
    protected void refresh() {

    }

    public void cancel(ActionEvent event) {
        Main.refreshLayout(event, new MainController().getLayout());
    }

    public enum Next {addCoin, showMnemonic, changePass}
    private Next nextStep;

    @FXML
    private void next(final ActionEvent event) {
        try {
            String password = pass.getText();
            Wallet wallet = Main.getInstance().getWallet();
            KeyCrypter crypter = wallet.getKeyCrypter();
            if (crypter == null) {
                Main.refreshLayout(event, new MainController().getLayout());
                return;
            }
            KeyParameter key = crypter.deriveKey(password);

            if (Next.addCoin.equals(nextStep)) {
                if (addCoin != null) {
                    wallet.createAccount(addCoin, false, key);
                    Main.getInstance().setWallet(wallet);
                    Main.refreshLayout(event, new MainController().getLayout());
                }
            }
            else if (Next.showMnemonic.equals(nextStep)) {
                DeterministicSeed seed = wallet.getSeed();
                if (seed == null) {
                    Main.refreshLayout(event, new MainController().getLayout());
                    return;
                }
                final DeterministicSeed dSeed = seed.decrypt(crypter, null, key);
                Main.refreshLayout(event, new ShowMnemonicController().getLayout(), new ShowListener() {
                    @Override
                    public void onShow(Object controller) {
                        ((ShowMnemonicController) controller).setMnemonic(dSeed.getMnemonicCode());
                    }
                });
            }
            else if (Next.changePass.equals(nextStep)) {
                DeterministicSeed seed = wallet.getSeed();
                if (seed == null) {
                    Main.refreshLayout(event, new MainController().getLayout());
                    return;
                }
                final DeterministicSeed dSeed = seed.decrypt(crypter, null, key);
                final List<WalletAccount> waList = new ArrayList<>();
                for (WalletAccount wa : wallet.getAllAccounts()) {
                    wa.decrypt(key);
                    waList.add(wa);
                }
                Main.refreshLayout(event, new SetPasswordController().getLayout(), new ShowListener() {
                    @Override
                    public void onShow(Object controller) {
                        ((SetPasswordController)controller).setMnemonic(dSeed.getMnemonicCode(), waList);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            pass.setText("");
            Main.showMessage(Main.getLocString("wrong_pass"));
        }
    }

    public void setNextStepMnemonic() {
        message.setText(Main.getLocString("enter_pass"));
        nextStep = Next.showMnemonic;
    }


    public void setNextChangePass() {
        message.setText(Main.getLocString("enter_old_pass"));
        nextStep = Next.changePass;
    }

    public void setAppendedCoin(CoinType coin) {
        addCoin = coin;
        message.setText(Main.getLocString("to_add") + " " + coin.getName() + " " + Main.getLocString("enter_pass_part"));
        nextStep = Next.addCoin;
    }



}
