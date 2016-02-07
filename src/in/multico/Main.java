package in.multico;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.wallet.Wallet;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.core.wallet.WalletFiles;
import com.coinomi.core.wallet.WalletProtobufSerializer;
import com.google.common.collect.ImmutableList;
import in.multico.controller.MsgController;
import in.multico.listener.ShowListener;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bitcoinj.crypto.MnemonicCode;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static final String WALLET_FILE = "wallet.dat";
    private Wallet wallet;
    private static final int WALLET_WRITE_DELAY_SEC = 10;
    private static Main instance;

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance = this;
        String startLayout;
        if (new File(WALLET_FILE).exists()) {
            startLayout = "layout/main.fxml";
            loadWallet();
        } else {
            startLayout = "layout/start_select.fxml";
        }
        Parent root = FXMLLoader.load(getClass().getResource(startLayout));
        primaryStage.setTitle("Multicoin wallet");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void refreshLayout(ActionEvent event, String layout) {
        refreshLayout(event, layout, null);
    }

    public static void refreshLayout(ActionEvent event, String layout, final ShowListener sl) {
        Scene scene = ((Control)event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
        try {
            final FXMLLoader loader = new FXMLLoader(Main.class.getResource("layout/" + layout));
            Parent root = loader.load();
            if (sl != null) {
                stage.setOnShown(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        sl.onShow(loader.getController());
                    }
                });
            }
            scene.setRoot(root);
            scene.getStylesheets().add(Main.class.getResource("layout/main.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image getCoinImage(CoinType coin) {
        String name = coin.getName();
        InputStream stream = Main.class.getResourceAsStream("icons/" + name + ".png");
        return new Image(stream);
    }

    public static void showMessage(final String msg) {
        try {
            final FXMLLoader loader = new FXMLLoader(Main.class.getResource("layout/msg.fxml"));
            Parent root = loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 200));
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ((MsgController)loader.getController()).setMsg(msg);
                }
            });
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWallet(@Nullable Wallet wallet) {
        if (this.wallet != null) {
            this.wallet.shutdownAutosaveAndWait();
        }
        this.wallet = wallet;
        if (this.wallet != null) {
            File walletFile = new File(WALLET_FILE);
            this.wallet.autosaveToFile(walletFile, WALLET_WRITE_DELAY_SEC, TimeUnit.SECONDS, new WalletFiles.Listener() {
                @Override
                public void onBeforeAutoSave(File tempFile) {
                    System.out.println("onBeforeAutoSave " + tempFile.getAbsolutePath());
                }

                @Override
                public void onAfterAutoSave(File newlySavedFile) {
                    System.out.println("onAfterAutoSave " + newlySavedFile.getAbsolutePath());
                }
            });
            this.wallet.saveNow();
            SyncService.getInstance(this.wallet).restart();
        }
    }

    private void loadWallet() {
        File walletFile = new File(WALLET_FILE);
        if (walletFile.exists()) {
            final long start = System.currentTimeMillis();
            FileInputStream walletStream = null;
            try {
                walletStream = new FileInputStream(walletFile);
                setWallet(WalletProtobufSerializer.readWallet(walletStream));
                System.out.println("wallet loaded from: '" + walletFile + "', took " + (System.currentTimeMillis() - start) + "ms");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Ошибка загрузки файла кошелька: " + e.getMessage());
            } finally {
                if (walletStream != null) {
                    try {
                        walletStream.close();
                    } catch (final IOException x) { /* ignore */ }
                }
            }
        }
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<String> getMnemonicWorList() {
        return MnemonicCode.INSTANCE.getWordList();
    }


    public List<WalletAccount> getAccounts(CoinType type) {
        if (wallet != null) {
            return wallet.getAccounts(type);
        } else {
            return ImmutableList.of();
        }
    }


    public List<WalletAccount> getAccounts(List<CoinType> types) {
        if (wallet != null) {
            return wallet.getAccounts(types);
        } else {
            return ImmutableList.of();
        }
    }

    public List<WalletAccount> getAllAccounts() {
        if (wallet != null) {
            return wallet.getAllAccounts();
        } else {
            return ImmutableList.of();
        }
    }

    public boolean isAccountExists(CoinType type) {
        return wallet != null && wallet.isAccountExists(type);
    }

    public static Main getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        if (MnemonicCode.INSTANCE == null) {
            try {
                MnemonicCode.INSTANCE = new MnemonicCode();
            } catch (IOException e) {
                System.out.println("Could not set MnemonicCode.INSTANCE");
            }
        }
        launch(args);
    }
}
