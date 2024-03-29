package but2.s4.festiplandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import but2.s4.festiplandroid.adaptater.FestivalAdapter;
import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

/**
 * ScheduledActivity permet d'affiché l'ensemble des festivals
 * programmées et de gérer la deconnexion et la navigation vers
 * la page des festival en favoris.
 * <p>
 * Elle hérite de AppCompatActivity qui est une classe
 * de base pour les activités qui utilisent la barre d'action.
 */
public class ScheduledActivity extends AppCompatActivity {
    private TextView textError;
    private ArrayList<Festival> festivalList;

    private RequestQueue fileRequete;

    private RecyclerView recyclerView;

    /**
     * Cette méthode est appelée à la création de
     * l'activité.
     *
     * Initialise la vue des festivals programmés
     * et associe un comportement à chaque bouton
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        festivalList = new ArrayList<>();

        // association de la vue avec l'activité
        setContentView(R.layout.activity_scheduled);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialisation du texte error affiché
        // si aucun festival n'a été trouvé
        textError = this.findViewById(R.id.error_festival_not_found);
        textError.setVisibility(View.INVISIBLE);

        // LinearLayout permettant de naviger vers les activités en favoris
        LinearLayout favorisButton = findViewById(R.id.buttonFavoritesFestival);
        favorisButton.setOnClickListener(v -> navigateToFavorites());

        // Bouton de deconnexion
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_scheduled);
        deconnexionButton.setOnClickListener(v -> navigateTosignOut());

        // appel de la métode de remplissage du recyclerView
        // avec les différents festival
        loadAllFestivalsObject();
    }

    /**
     * Méthode appelé lors du clique sur le bouton de deconnexion
     *
     * Redirige l'utilisateur vers la page de connexion et
     * réinitialise le singleton de l'utilisateur
     */
    private void navigateTosignOut() {
        User.getInstance().setPrenomUser(null);
        User.getInstance().setNomUser(null);
        User.getInstance().setIdUser(-1);
        User.getInstance().setLoginUser(null);
        Navigator.toActivity(ScheduledActivity.this, LoginActivity.class);
    }

    /**
     * Méthode appelé lors du clique sur le bouton permettant à
     * l'utilisateur de consulter ces favoris
     *
     * Redirige l'utilisateur vers la page contenant l'ensemble
     * des festivals mis en favoris par l'utilisateur
     */
    private void navigateToFavorites() {
        Navigator.toActivity(ScheduledActivity.this, FavoritesActivity.class);
    }

    /**
     * récupère l'ensemble des festivals programmés
     */
    private void loadAllFestivalsObject() {
        JsonArrayRequest allScheduledFestival = new JsonArrayRequest(FestiplanApi.getURLAllFestivalsScheduled(),
                response -> {

                    ArrayList<Festival> festivals = new ArrayList<>();

                    // récupération des festivals programmés
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject festivalJSON = response.getJSONObject(i);

                            // ajout du festivalJSON à la liste
                            festivals.add(new Festival(
                                    festivalJSON.getInt("idFestival"),
                                    festivalJSON.getString("nomFestival"),
                                    festivalJSON.getString("descriptionFestival"),
                                    festivalJSON.getInt("idImage"),
                                    "",
                                    festivalJSON.getString("dateDebutFestival"),
                                    festivalJSON.getString("dateFinFestival"),
                                    festivalJSON.getInt("idGriJ"),
                                    festivalJSON.getInt("idResponsable"),
                                    festivalJSON.getString("ville"),
                                    festivalJSON.getString("codePostal")
                            ));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (festivals.isEmpty()) {
                        textError.setVisibility(View.VISIBLE);
                    } else {
                        festivalList.addAll(festivals);
                        System.out.println(festivalList);
                        arrayToRecycler(festivalList);
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        getFileRequete().add(allScheduledFestival);
    }

    /**
     * Permet de placer l'ensemble des festivals de la liste
     * dans le recyclerView en appelent l'adaptateur
     * @param festivalList
     */
    private void arrayToRecycler(ArrayList<Festival> festivalList) {
        FestivalAdapter festiAdapter = new FestivalAdapter(festivalList);
        recyclerView.setAdapter(festiAdapter);
    }

    /**
     * Renvoie la file d'attente pour les requêtes Web :
     * - si la file n'existe pas encore : elle est créée puis renvoyée
     * - si une file d'attente existe déjà : elle est renvoyée
     * On assure ainsi l'unicité de la file d'attente
     *
     * @return RequestQueue une file d'attente pour les requêtes Volley
     */
    private RequestQueue getFileRequete() {
        if (fileRequete == null) {
            fileRequete = Volley.newRequestQueue(this);
        }
        return fileRequete;
    }
}