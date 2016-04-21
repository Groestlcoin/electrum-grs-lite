package in.multico.connector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Copyright © 2016 Marat Shmush. All rights reserved.
 * Date: 05.02.16
 * Time: 09:38
 */
public class ConnectorBased {

    protected static String get(String req) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(req);
            HttpResponse resp = client.execute(get);
//            System.out.println("-~->" + req);
            String s = EntityUtils.toString(resp.getEntity());
//            System.out.println("<-~-" + s);
            return s;
        } finally {
            client.close();
        }
    }
}
