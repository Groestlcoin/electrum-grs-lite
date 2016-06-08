package in.multico;

import com.coinomi.core.coins.CoinType;
import com.coinomi.core.coins.Value;
import com.coinomi.core.wallet.*;
import com.google.common.collect.ImmutableList;
import in.multico.controller.ControllerBased;
import in.multico.controller.MainController;
import in.multico.controller.MsgController;
import in.multico.controller.StartSelectController;
import in.multico.listener.CloseListener;
import in.multico.listener.ShowListener;
import javafx.application.Application;
import javafx.application.Platform;
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
import org.bitcoinj.wallet.KeyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */

public class Main extends Application implements WalletAccountEventListener {

    public static final String WALLET_FILE = "wallet.dat";
    private Wallet wallet;
    private static Main instance;
    private static ControllerBased controller;
    private static Logger logger;

    @Override
    public void start(Stage primaryStage) throws Exception{
        instance = this;
        String startLayout;
        File walletFile = new File(WALLET_FILE);
        if (walletFile.exists()) {
            startLayout = "layout/" + new MainController().getLayout();
            final long start = System.currentTimeMillis();
            FileInputStream walletStream = null;
            try {
                walletStream = new FileInputStream(walletFile);
                wallet = WalletProtobufSerializer.readWallet(walletStream);
                SyncService.getInstance(this.wallet).restart();
                for (WalletAccount wa : wallet.getAllAccounts()) {
                    wa.addEventListener(this);
                }
                log("wallet loaded from: '" + walletFile.getAbsolutePath() + "', took " + (System.currentTimeMillis() - start) + "ms");
            } catch (Exception e) {
                if (isWindows()) {
                    startLayout = "layout/" + new StartSelectController().getLayout();
                    doStart(primaryStage, startLayout);
                } else {
                    e.printStackTrace();
                    showMessage(getLocString("err_load_wallet") + ": " + e.getMessage(), new CloseListener() {
                        @Override
                        public void onClose() {
                            log("Begin close...");
                            SyncService.getInstance(wallet).stopAll();
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                }
            } finally {
                if (walletStream != null) {
                    try {
                        walletStream.close();
                    } catch (final IOException x) { /* ignore */ }
                }
            }
        } else {
            startLayout = "layout/" + new StartSelectController().getLayout();
        }
        doStart(primaryStage, startLayout);
    }

    private void doStart(Stage primaryStage, String startLayout) throws IOException {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource(startLayout));
        loader.setResources(ResourceBundle.getBundle("bundles.strings", getLocale()));
        Parent root = loader.load();
        primaryStage.setTitle("Multicoin wallet");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller = loader.getController();
            }
        });
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                log("Begin close...");
                SyncService.getInstance(wallet).stopAll();
                Platform.exit();
                System.exit(0);
            }
        });
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
            loader.setResources(ResourceBundle.getBundle("bundles.strings", getLocale()));
            Parent root = loader.load();
            scene.setRoot(root);
            scene.getStylesheets().add(Main.class.getResource("layout/main.css").toExternalForm());
            stage.setScene(scene);
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controller = loader.getController();
                    if (sl != null) sl.onShow(controller);

                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Image getCoinImage(CoinType coin) {
        String name = coin.getName();
        if (name.contains("beta")) name = name.split(" ")[0];
        name = name.replaceAll(" ", "_");
        name = name.toLowerCase();
        InputStream stream = Main.class.getResourceAsStream("icons/" + name + ".png");
        return new Image(stream);
    }

    public static void showMessage(final String msg) {
        showMessage(msg, null);
    }

    public static void showMessage(final String msg, final CloseListener cl) {
        try {
            final FXMLLoader loader = new FXMLLoader(Main.class.getResource("layout/" + new MsgController().getLayout()));
            loader.setResources(ResourceBundle.getBundle("bundles.strings", getLocale()));
            Parent root = loader.load();
            final Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 200));
            stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    MsgController mc = loader.getController();
                    mc.setMsg(msg);
                    if (cl != null) mc.setCloseListener(cl);
                }
            });
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Locale getLocale() {
        Locale loc = Locale.getDefault();
        log("system locale: " + loc);
        if (loc.getLanguage().equals("ru")) {
            return loc;
        }
        return new Locale("en", "EN");
    }

    public static boolean isWindows() {
        String s = System.getProperty("os.name");
        s = s.toLowerCase();
        return s.contains("win");
    }

    public static boolean isUnix() {
        String s = System.getProperty("os.name");
        s = s.toLowerCase();
        return (s.contains("nix") || s.contains("nux") || s.contains("aix"));

    }

    public static String getLocString(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.strings", getLocale());
        return bundle.getString(key);
    }

    public void setWallet(@Nullable Wallet wallet) {
        this.wallet = wallet;
        if (this.wallet != null) {
            try {
                this.wallet.saveToFileStream(new FileOutputStream(new File(WALLET_FILE)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            SyncService.getInstance(this.wallet).restart();
            for (WalletAccount wa : wallet.getAllAccounts()) {
                wa.addEventListener(this);
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

    public static String getAddr(WalletAccount wa) {
        AbstractAddress address;
        if (Settings.getInstanse().isAlwaysRefreshAddr()) {
            address = wa.getReceiveAddress();
        } else {
            address = ((WalletPocketHD) wa).getLastUsedAddress(KeyChain.KeyPurpose.RECEIVE_FUNDS);
            if (address == null) {
                address = wa.getReceiveAddress();
            }
        }
        if (address != null) return address.toString();
        else return "";
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

    public static void log(String s) {
        logger.info(s);
    }

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(Main.class);
        if (MnemonicCode.INSTANCE == null) {
            try {
                MnemonicCode.INSTANCE = new MnemonicCode();
            } catch (IOException e) {
                log("Could not set MnemonicCode.INSTANCE");
            }
        }
        launch(args);
    }

    @Override
    public void onNewBalance(Value newBalance) {
        log("onNewBalance: " + newBalance);
        if (controller != null) controller.doRefresh();
        else log("controller is not found!");
    }

    @Override
    public void onNewBlock(WalletAccount pocket) {}

    @Override
    public void onTransactionConfidenceChanged(WalletAccount pocket, AbstractTransaction tx) {
        log("onTransactionConfidenceChanged: " + tx);
        if (controller != null) controller.doRefresh();
        else log("controller is not found!");
    }

    @Override
    public void onTransactionBroadcastFailure(WalletAccount pocket, AbstractTransaction tx) {}

    @Override
    public void onTransactionBroadcastSuccess(WalletAccount pocket, AbstractTransaction tx) {}

    @Override
    public void onWalletChanged(WalletAccount pocket) {
        log("onWalletChanged: " + pocket);
        if (controller != null) controller.doRefresh();
        else log("controller is not found!");
    }

    @Override
    public void onConnectivityStatus(WalletConnectivityStatus connectivityStatus) {}

}
