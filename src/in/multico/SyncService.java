package in.multico;

import com.coinomi.core.coins.*;
import com.coinomi.core.network.CoinAddress;
import com.coinomi.core.network.ServerClients;
import com.coinomi.core.wallet.Wallet;
import com.coinomi.core.wallet.WalletAccount;
import com.coinomi.stratumj.ServerAddress;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vp
 * Date: 06.02.16
 * Time: 08:24
 */
public class SyncService {

    private static SyncService instance;
    private ServerClients clients;
    private Wallet wallet;

    public static final List<CoinType> SUPPORTED_COINS = ImmutableList.of(
            BitcoinMain.get(),
            DogecoinMain.get(),
            LitecoinMain.get(),
            DashMain.get(),
            NuBitsMain.get(),
            PeercoinMain.get(),
            NamecoinMain.get(),
            BlackcoinMain.get(),
            ClamsMain.get(),
            MonacoinMain.get(),
            NuSharesMain.get(),
            NovacoinMain.get(),
            GcrMain.get(),
            VpncoinMain.get(),
            VertcoinMain.get(),
            ShadowCashMain.get(),
            FeathercoinMain.get(),
            ReddcoinMain.get(),
            DigibyteMain.get(),
            RubycoinMain.get(),
            DigitalcoinMain.get(),
            DogecoindarkMain.get(),
            JumbucksMain.get(),
            CanadaeCoinMain.get(),
            CannacoinMain.get(),
            NeoscoinMain.get(),
            ParkbyteMain.get(),
            BitcoinTest.get(),
            LitecoinTest.get(),
            DogecoinTest.get()
    );
    public static final List<CoinAddress> DEFAULT_COINS_SERVERS = ImmutableList.of(
            new CoinAddress(BitcoinMain.get(), new ServerAddress("btc-cce-1.coinomi.net", 5001), new ServerAddress("btc-cce-2.coinomi.net", 5001)),
            new CoinAddress(BitcoinTest.get(), new ServerAddress("btc-testnet-cce-1.coinomi.net", 15001), new ServerAddress("btc-testnet-cce-2.coinomi.net", 15001)),
            new CoinAddress(DogecoinMain.get(), new ServerAddress("doge-cce-1.coinomi.net", 5003), new ServerAddress("doge-cce-2.coinomi.net", 5003)),
            new CoinAddress(DogecoinTest.get(), new ServerAddress("doge-testnet-cce-1.coinomi.net", 15003), new ServerAddress("doge-testnet-cce-2.coinomi.net", 15003)),
            new CoinAddress(LitecoinMain.get(), new ServerAddress("ltc-cce-1.coinomi.net", 5002), new ServerAddress("ltc-cce-2.coinomi.net", 5002)),
            new CoinAddress(LitecoinTest.get(), new ServerAddress("ltc-testnet-cce-1.coinomi.net", 15002), new ServerAddress("ltc-testnet-cce-2.coinomi.net", 15002)),
            new CoinAddress(PeercoinMain.get(), new ServerAddress("ppc-cce-1.coinomi.net", 5004), new ServerAddress("ppc-cce-2.coinomi.net", 5004)),
            new CoinAddress(NuSharesMain.get(), new ServerAddress("nsr-cce-1.coinomi.net", 5011), new ServerAddress("nsr-cce-2.coinomi.net", 5011)),
            new CoinAddress(NuBitsMain.get(), new ServerAddress("nbt-cce-1.coinomi.net", 5012), new ServerAddress("nbt-cce-2.coinomi.net", 5012)),
            new CoinAddress(DashMain.get(), new ServerAddress("drk-cce-1.coinomi.net", 5013), new ServerAddress("drk-cce-2.coinomi.net", 5013)),
            new CoinAddress(ReddcoinMain.get(), new ServerAddress("rdd-cce-1.coinomi.net", 5014), new ServerAddress("rdd-cce-2.coinomi.net", 5014)),
            new CoinAddress(BlackcoinMain.get(), new ServerAddress("blk-cce-1.coinomi.net", 5015), new ServerAddress("blk-cce-2.coinomi.net", 5015)),
            new CoinAddress(NamecoinMain.get(), new ServerAddress("nmc-cce-1.coinomi.net", 5016), new ServerAddress("nmc-cce-2.coinomi.net", 5016)),
            new CoinAddress(FeathercoinMain.get(), new ServerAddress("ftc-cce-1.coinomi.net", 5017), new ServerAddress("ftc-cce-2.coinomi.net", 5017)),
            new CoinAddress(RubycoinMain.get(), new ServerAddress("rby-cce-1.coinomi.net", 5018), new ServerAddress("rby-cce-2.coinomi.net", 5018)),
            new CoinAddress(UroMain.get(), new ServerAddress("uro-cce-1.coinomi.net", 5019), new ServerAddress("uro-cce-2.coinomi.net", 5019)),
            new CoinAddress(DigitalcoinMain.get(), new ServerAddress("dgc-cce-1.coinomi.net", 5020), new ServerAddress("dgc-cce-2.coinomi.net", 5020)),
            new CoinAddress(CannacoinMain.get(), new ServerAddress("ccn-cce-1.coinomi.net", 5021), new ServerAddress("ccn-cce-2.coinomi.net", 5021)),
            new CoinAddress(MonacoinMain.get(), new ServerAddress("mona-cce-1.coinomi.net", 5022), new ServerAddress("mona-cce-2.coinomi.net", 5022)),
            new CoinAddress(DigibyteMain.get(), new ServerAddress("dgb-cce-1.coinomi.net", 5023), new ServerAddress("dgb-cce-2.coinomi.net", 5023)),
            // 5024 primecoin
            new CoinAddress(ClamsMain.get(), new ServerAddress("clam-cce-1.coinomi.net", 5025), new ServerAddress("clam-cce-2.coinomi.net", 5025)),
            new CoinAddress(ShadowCashMain.get(), new ServerAddress("sdc-cce-1.coinomi.net", 5026), new ServerAddress("sdc-cce-2.coinomi.net", 5026)),
            new CoinAddress(NeoscoinMain.get(), new ServerAddress("neos-cce-1.coinomi.net", 5027), new ServerAddress("neos-cce-2.coinomi.net", 5027)),
            new CoinAddress(VertcoinMain.get(), new ServerAddress("vtc-cce-1.coinomi.net", 5028), new ServerAddress("vtc-cce-2.coinomi.net", 5028)),
            new CoinAddress(JumbucksMain.get(), new ServerAddress("jbs-cce-1.coinomi.net", 5029), new ServerAddress("jbs-cce-2.coinomi.net", 5029)),
            new CoinAddress(VpncoinMain.get(), new ServerAddress("vpn-cce-1.coinomi.net", 5032), new ServerAddress("vpn-cce-2.coinomi.net", 5032)),
            new CoinAddress(CanadaeCoinMain.get(), new ServerAddress("cdn-cce-1.coinomi.net", 5033), new ServerAddress("cdn-cce-2.coinomi.net", 5033)),
            new CoinAddress(NovacoinMain.get(), new ServerAddress("nvc-cce-1.coinomi.net", 5034), new ServerAddress("nvc-cce-2.coinomi.net", 5034)),
            new CoinAddress(ParkbyteMain.get(), new ServerAddress("pkb-cce-1.coinomi.net", 5035), new ServerAddress("pkb-cce-2.coinomi.net", 5035)),
            new CoinAddress(DogecoindarkMain.get(), new ServerAddress("doged-cce-1.coinomi.net", 5036), new ServerAddress("doged-cce-2.coinomi.net", 5036)),
            new CoinAddress(GcrMain.get(), new ServerAddress("gcr-cce-1.coinomi.net", 5038), new ServerAddress("gcr-cce-2.coinomi.net", 5038))
    );


    private SyncService(Wallet wallet) {
        this.wallet = wallet;
        clients = new ServerClients(DEFAULT_COINS_SERVERS);
    }

    public static SyncService getInstance(Wallet wallet) {
        if (instance == null) instance = new SyncService(wallet);
        return instance;
    }

    public void stopAll() {
        clients.stopAllAsync();
    }

    public void startAll() {
        List<WalletAccount> accounts = wallet.getAllAccounts();
        for (WalletAccount account : accounts)  clients.startAsync(account);
    }

    public void restart() {
        stopAll();
        startAll();
    }

}
