package but2.s4.festiplandroid.api;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FestiplanApi {
    private static final String URI_LOGIN_API_REQUEST
        = "http://10.0.2.2:8888/festiplan/api/connexion?login=%s&mdp=%s";

    public static void createLoginApiListener(String login,
                                              String password,
                                              ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_LOGIN_API_REQUEST, login, password);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    private static String callApi(String uri) {
        URL apiUrl;
        HttpURLConnection connection;
        int responseCode;

        try {
            apiUrl = new URL(uri);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            responseCode = connection.getResponseCode();

            System.out.println(responseCode);
            System.out.println(apiUrl);

            if (responseCode == 200) {
                InputStream inputStream;
                BufferedReader reader;
                String line;
                StringBuilder content;

                content = new StringBuilder();
                reader = new BufferedReader(
                    new InputStreamReader(inputStream));
                inputStream = connection.getInputStream();

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

                reader.close();
                return content.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}