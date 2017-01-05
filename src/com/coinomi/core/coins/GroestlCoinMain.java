package com.coinomi.core.coins;

import com.coinomi.core.coins.families.Families;
import com.coinomi.core.coins.families.GroestlFamily;

import com.hashengineering.crypto.Groestl;
import org.bitcoinj.core.Coin;

/**
 * @author John L. Jegutanis
 */
public class GroestlCoinMain extends GroestlFamily {
    private GroestlCoinMain() {
        id = "groestlcoin.main";

        addressHeader = 36;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 120;


        name = "Groestlcoin";
        symbol = "GRS";
        uriScheme = "groestlcoin";
        bip44Index = 17;
        unitExponent = 8;
        feeValue = value(10000);
        minNonDust = value(1000); // 0.00001 GRS mininput
        softDustLimit = value(10000); // 0.00001 GRS
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
        signedMessageHeader = toBytes("GroestlCoin Signed Message:\n");
    }

    private static GroestlCoinMain instance = new GroestlCoinMain();
    public static synchronized GroestlCoinMain get() {
        return instance;
    }
}
