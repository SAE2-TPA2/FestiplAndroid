package but2.s4.festiplandroid.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private static final String URI_FESTIVAL_SET_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/ajouterFavori";
    private static final String URI_FESTIVAL_DELETE_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/supprimerFavori";


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
    public static void createFavoritesFestivalsPostListener(int idUser, int idFestival, ApiResponse callback) {

        String requestUri;
        Handler handler;
        handler = new Handler(Looper.getMainLooper());
        requestUri = URI_FESTIVAL_SET_FAVORITES;


        // Exemple de création d'un requestBody au format JSON
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userId", idUser);
            requestBody.put("festivalId", idFestival);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            String test;


            test = callApiPost(requestUri, requestBody.toString());
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
    private static String callApiPost(String uri, String requestBody) {
        URL apiUrl;
        HttpURLConnection connection;
        int responseCode;

        try {
            apiUrl = new URL(uri);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }
            connection.connect();
            responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                InputStream inputStream;
                BufferedReader reader;
                String line;
                StringBuilder content;
                content = new StringBuilder();
                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

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
    private static String callApiDelete(String uri, JSONObject requestBody) {
        URL apiUrl;
        HttpURLConnection connection;
        int responseCode;

        try {
            apiUrl = new URL(uri);
            connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("DELETE");

            // Set the request headers if needed
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Enable output and write the request body to the connection
            connection.setDoOutput(true);
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            responseCode = connection.getResponseCode();

            System.out.println(responseCode);
            System.out.println(apiUrl);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }
                    return content.toString();
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}