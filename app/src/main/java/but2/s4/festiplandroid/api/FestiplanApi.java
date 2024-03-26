package but2.s4.festiplandroid.api;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import but2.s4.festiplandroid.festivals.Festival;

public class FestiplanApi {
    public static final String DOMAIN_API
        = "http://10.0.2.2:8888";

    private static final String URI_LOGIN_API_REQUEST
        = "%s/sae-s4-festiplan-b-green-b/festiplan/api/connexion?login=%s&mdp=%s";

    private static final String URI_FESTIVAL_API_REQUEST
        = "%s/sae-s4-festiplan-b-green-b/festiplan/api/detailsfestival/%s";

    private static final String URI_FESTIVAL_ORGANIZERS_API_REQUEST
        = "%s/sae-s4-festiplan-b-green-b/festiplan/api/organisateursfestival/%s";

    private static final String URI_FESTIVAL_SCENES_API_REQUEST
        = "%s/sae-s4-festiplan-b-green-b/festiplan/api/scenesfestival/%s";

    private static final String URI_FESTIVAL_SHOWS_API_REQUEST
        = "%s/sae-s4-festiplan-b-green-b/festiplan/api/spectaclesfestival/%s";
    private static final String URI_FESTIVAL_ALL_SCHEDULED
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/tousLesFestivals";
    private static final String URI_FESTIVAL_ALL_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/tousLesFavoris/%s";

    public static void createLoginApiListener(String login,
                                              String password,
                                              ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_LOGIN_API_REQUEST,
                                   DOMAIN_API,
                                   login,
                                   password);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    public static void createFestivalApiListener(int id,
                                                 ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_API_REQUEST,
                                   DOMAIN_API,
                                   id);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    public static void createFestivalOrganizersApiListener(
        Festival festivalInstance,
        ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_ORGANIZERS_API_REQUEST,
                                   DOMAIN_API,
                                   festivalInstance.getIdFestival());

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    public static void createFestivalScenesApiListener(
        Festival festivalInstance,
        ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_SCENES_API_REQUEST,
                                   DOMAIN_API,
                                   festivalInstance.getIdFestival());

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    public static void createFestivalShowsApiListener(
            Festival festivalInstance,
            ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri
                = String.format(URI_FESTIVAL_SHOWS_API_REQUEST,
                                DOMAIN_API,
                                festivalInstance.getIdFestival());

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    public static void createAllFestivalsApiListener(ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_ALL_SCHEDULED);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }
    public static void createFavoritesFestivalsApiListener(int id,
                                                           ApiResponse callback) {

        String requestUri;

        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_ALL_FAVORITES, id);

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
                inputStream = connection.getInputStream();
                reader = new BufferedReader(
                    new InputStreamReader(inputStream));

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