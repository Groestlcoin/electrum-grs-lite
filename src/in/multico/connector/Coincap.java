package in.multico.connector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 */

public class Coincap {

    public static final String URL = "http://coincap.io/page/";

    public static void getPrice(final String coin, final PriceListener pl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = get(URL + coin);
                    JSONObject jo = new JSONObject(s);
                    pl.onPrice(jo.getDouble("usdPrice"));
                } catch (Exception e) {
                    e.printStackTrace();
                    pl.onPrice(0.0);
                }
            }
        }).start();
    }

    private static String get(String req) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(req);
            HttpResponse resp = client.execute(get);
            System.out.println("-~->" + req);
            String s = EntityUtils.toString(resp.getEntity());
            System.out.println("<-~-" + s);
            return s;
        } finally {
            client.close();
        }
    }

    public interface PriceListener {
        void onPrice(double price);
    }
}
