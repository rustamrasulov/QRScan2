package com.miirrr.qrscan.services.web;

import com.miirrr.qrscan.config.Config;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class WebServiceImpl implements WebService{

    private final Config config;
    private static volatile WebServiceImpl INSTANCE;

    public WebServiceImpl() {
        config = Config.getConfig();
    }

    @Override
    public WebServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (WebServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WebServiceImpl();
                }
            }
        }
        return INSTANCE;
    }


    public String request(String method) throws UnsupportedEncodingException {

        String body = "{\n" +
//                "\"store\": \"" + config.getFilialId() + "\"" +
                "}";

        return callWebService(method, body);
    }

    private String callWebService(String method, String body) throws UnsupportedEncodingException {

        String result;

        HttpPost post = new HttpPost(config.getDbUrl() + method);

        post.setEntity(new StringEntity(body));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)
        ) {
            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 300 && response.getEntity().getContentLength() > 0) {
                result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            } else {
                result = "ERROR";
            }
        } catch (IOException e) {
            JFrame jFrame = new JFrame();
            jFrame.setAlwaysOnTop(true);
            JOptionPane.showMessageDialog(jFrame, "Не возможно подключиться к серверу." +
                    "\nОбратитесь к администратору", "Error", JOptionPane.ERROR_MESSAGE);
            result = "ERROR";

        }

        assert result != null;
        if (!result.contains("HTTPСервис")) {
            return result;
        }

        return "{\n\"code\": \"100\"\n}";
    }


}
