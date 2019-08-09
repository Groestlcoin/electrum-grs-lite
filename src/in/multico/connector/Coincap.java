package in.multico.connector;

import com.coinomi.core.exchange.shapeshift.Connection;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import in.multico.Main;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 */

public class Coincap extends Connection {

    private static final String URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/short?crypto=BTC";
    private static final String BTC_URL = "https://chainz.cryptoid.info/grs/api.dws?q=ticker.btc";
    private static Coincap instance;
    private static final long expire = 60 * 1000;
    private ConcurrentHashMap<String, PriceResult> cashe = new ConcurrentHashMap<>();

    private Coincap(){}

    public static Coincap getInstance() {
        if (instance == null) instance = new Coincap();
        return instance;
    }

    public void getPrice(final String coin, final PriceListener pl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PriceResult priceResult = cashe.get(coin);
                if (priceResult == null) {
                    pl.onPrice(doGetPrice(coin));
                } else if (System.currentTimeMillis() > priceResult.time + expire) {
                    cashe.remove(coin);
                    pl.onPrice(doGetPrice(coin));
                } else {
                    pl.onPrice(priceResult.price);
                }
            }
        }).start();
    }

    public void getPrices(final List<String> coins, final PricesListener pl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Double> rez = new HashMap<String, Double>();
                boolean cashed = true;
                for (String coin : coins) {
                    PriceResult priceResult = cashe.get(coin);
                    if (priceResult == null) {
                        Main.log("no " + coin + " in cashe. Try get from API...");
                        rez.put(coin, doGetPrice(coin));
                        cashed = false;
                    } else if (System.currentTimeMillis() > priceResult.time + expire) {
                        Main.log(coin + " expired in cashe. Try get from API...");
                        cashe.remove(coin);
                        rez.put(coin, doGetPrice(coin));
                        cashed = false;
                    } else {
                        Main.log(coin + " in cashe: " + priceResult.price);
                        rez.put(coin, priceResult.price);
                    }
                }
                if (cashed) try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pl.onPrice(rez);
            }
        }).start();
    }

    private double doGetPrice(String coin) {
        try {
            Request btcRequest = new Request.Builder().url(BTC_URL).build();
            Request request = new Request.Builder().url(URL.replace("$", coin)).build();
            Main.log("~~> " + request);
            Response btcResponse = new OkHttpClient().newCall(btcRequest).execute();
            Response response = new OkHttpClient().newCall(request).execute();
            String s = response.body().string();
            String sBtc = btcResponse.body().string();
            Double btcPrice = Double.parseDouble(sBtc);
            Main.log("<~~ " + s);
            JSONObject jo = new JSONObject(s);
            PriceResult pr = new PriceResult();
            JSONObject btcusd = jo.getJSONObject("BTCUSD");
            pr.price = btcusd.getDouble("last")*btcPrice;
            pr.time = System.currentTimeMillis();
            cashe.put(coin, pr);
            return pr.price;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private class PriceResult {
        long time;
        double price;
    }

    public interface PriceListener {
        void onPrice(double price);
    }

    public interface PricesListener {
        void onPrice(Map<String, Double> prices);
    }
}
