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

/**
 * Classe pour gérer les appels API pour l'application Festiplan.
 */
public class FestiplanApi {
    // Domaine de l'API
    public static final String DOMAIN_API
            = "http://10.0.2.2";

    // URI pour la requête de connexion
    private static final String URI_LOGIN_API_REQUEST
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/connexion?login=%s&mdp=%s";

    // URI pour la requête de détails du festival
    private static final String URI_FESTIVAL_API_REQUEST
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/detailsfestival/%s";

    // URI pour la requête des organisateurs du festival
    private static final String URI_FESTIVAL_ORGANIZERS_API_REQUEST
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/organisateursfestival/%s";

    // URI pour la requête des scènes du festival
    private static final String URI_FESTIVAL_SCENES_API_REQUEST
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/scenesfestival/%s";

    // URI pour la requête des spectacles du festival
    private static final String URI_FESTIVAL_SHOWS_API_REQUEST
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/spectaclesfestival/%s";

    // URI pour la requête de tous les festivals programmés
    private static final String URI_FESTIVAL_ALL_SCHEDULED
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/tousLesFestivals";

    // URI pour la requête de tous les festivals favoris
    private static final String URI_FESTIVAL_ALL_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/tousLesFavoris/%s";
    private static final String URI_FESTIVAL_SET_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/ajouterFavori";
    private static final String URI_FESTIVAL_DELETE_FAVORITES
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/supprimerFavori";


    /**
     * Crée un écouteur pour l'API de connexion.
     *
     * @param login    Le login de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     * @param callback La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API de détails du festival.
     *
     * @param id       L'ID du festival.
     * @param callback La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API des organisateurs du festival.
     *
     * @param festivalInstance L'instance du festival.
     * @param callback         La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API des scènes du festival.
     *
     * @param festivalInstance L'instance du festival.
     * @param callback         La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API des spectacles du festival.
     *
     * @param festivalInstance L'instance du festival.
     * @param callback         La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API de tous les festivals programmés.
     *
     * @param callback La fonction de rappel à exécuter après la réponse de l'API.
     */
    public static void createAllFestivalsApiListener(ApiResponse callback) {

        String requestUri;
        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_ALL_SCHEDULED, DOMAIN_API);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }

    /**
     * Crée un écouteur pour l'API de tous les festivals favoris.
     *
     * @param id       L'ID de l'utilisateur.
     * @param callback La fonction de rappel à exécuter après la réponse de l'API.
     */
    public static void createFavoritesFestivalsApiListener(int id,
                                                           ApiResponse callback) {

        String requestUri;
        Handler handler;

        handler = new Handler(Looper.getMainLooper());

        requestUri = String.format(URI_FESTIVAL_ALL_FAVORITES, DOMAIN_API, id);

        new Thread(() -> {
            String test;

            test = callApi(requestUri);
            handler.post(() -> callback.onResponse(test));
        }).start();
    }
    /**
     * Crée un écouteur pour l'API permettant de mettre en en favoris un festival.
     *
     * @param idUser       L'ID de l'utilisateur.
     * @param idFestival    L'ID du festival.
     * @param callback La fonction de rappel à exécuter après la réponse de l'API.
     */
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

    /**
     * Crée un écouteur pour l'API permettant de retirer un festival des favoris.
     * @param idUser
     * @param idFestival
     * @param callback
     */
    public static void deleteFavoritesFestivalsDeleteListener(int idUser, int idFestival, ApiResponse callback) {
        String requestUri = URI_FESTIVAL_DELETE_FAVORITES;
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userId", idUser);
            requestBody.put("festivalId", idFestival);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            String test = callApiDelete(requestUri, requestBody);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResponse(test));
        }).start();
    }

    /**
     * Appelle l'API avec l'URI donnée.
     *
     * @param uri L'URI de l'API à appeler.
     * @return La réponse de l'API sous forme de chaîne de caractères.
     */
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
            } else if (responseCode == 400) {
                return "false";
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Appel api en méthode POST
     * @param uri
     * @param requestBody
     * @return
     */
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

    /**
     * Appel api en méthode DEL
     * @param uri
     * @param requestBody
     * @return
     */
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