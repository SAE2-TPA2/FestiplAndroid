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
import but2.s4.festiplandroid.session.User;

/**
 * Classe pour gérer les appels API pour l'application Festiplan.
 */
public class FestiplanApi {

    /**
     * Le port pour le serveur API.
     */
    public static final String PORT_API = "";

    // Domaine de l'API
    public static final String DOMAIN_API
            = "http://10.0.2.2:" + PORT_API;

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
            = "%s/sae-s4-festiplan-b-green-b/festiplan/api/supprimerFavori?idUser=%s&idFestival=%s";


    public static String getURLConnexon(String login, String mdp) {
        return String.format(URI_LOGIN_API_REQUEST, DOMAIN_API, login, mdp);
    }

    public static String getURLDetailFestival(int id) {
        return String.format(URI_FESTIVAL_API_REQUEST, DOMAIN_API, id);
    }

    public static String getURLFestivalOrganizers(int idFestival) {
        return String.format(URI_FESTIVAL_ORGANIZERS_API_REQUEST, DOMAIN_API, idFestival);
    }

    public static String getURLFestivalScenes(int idFestival) {
        return String.format(URI_FESTIVAL_SCENES_API_REQUEST, DOMAIN_API, idFestival);
    }

    public static String getURLFestivalShows(int idFestival) {
        return String.format(URI_FESTIVAL_SHOWS_API_REQUEST, DOMAIN_API, idFestival);
    }

    public static String getURLAllFestivalsScheduled() {
        return String.format(URI_FESTIVAL_ALL_SCHEDULED, DOMAIN_API);
    }

    public static String getURLFestivalAllFavorites(int id) {
        return String.format(URI_FESTIVAL_ALL_FAVORITES, DOMAIN_API, id);
    }

    public static String getURLFestivalSetFavorites() {
        return String.format(URI_FESTIVAL_SET_FAVORITES, DOMAIN_API);
    }

    public static String getURLFestivalDeleteFavorites(int idUser, int idFestival) {
        return String.format(URI_FESTIVAL_DELETE_FAVORITES, DOMAIN_API, idUser, idFestival);
    }
}