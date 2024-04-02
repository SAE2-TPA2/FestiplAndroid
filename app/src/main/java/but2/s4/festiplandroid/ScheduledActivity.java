package but2.s4.festiplandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import but2.s4.festiplandroid.adaptater.FestivalAdapter;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

import but2.s4.festiplandroid.api.FestiplanApi;

public class ScheduledActivity extends AppCompatActivity {

    private TextView textError;
    private ArrayList<Festival> festivalList;

    private RequestQueue fileRequete;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scheduled);

        festivalList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = calculateNoOfColumns();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        textError = findViewById(R.id.error_festival_not_found);
        textError.setVisibility(View.INVISIBLE);

        // LinearLayout permettant de naviger vers les activités en favoris
        LinearLayout favorisButton = findViewById(R.id.buttonFavoritesFestival);
        favorisButton.setOnClickListener(v -> navigateToFavorites());

        // Bouton de deconnexion
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_scheduled);
        deconnexionButton.setOnClickListener(v -> navigateTosignOut());

        // Méthode récupération de l'ensemble des festivals
        loadAllFestivalsObject();
    }

    /**
     * Méthode pour calculer le nombre de colonnes en fonction de la taille de l'écran
     * et des items
     */

    private int calculateNoOfColumns() {
        int columnWidthDp = 450; // Largeur de l'item
        float screenWidthDp = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
        int columnCount = (int) (screenWidthDp / columnWidthDp + 0.5); // Arrondi au nombre entier le plus proche
        return Math.max(columnCount, 1); // Au moins une colonne
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
        Navigator.clearAndGoToActivity(ScheduledActivity.this, FavoritesActivity.class);
    }
    /**
     * récupère l'ensemble des festivals programmés
     */
    private void loadAllFestivalsObject() {
        JsonArrayRequest allScheduledFestival = new JsonArrayRequest(
                FestiplanApi.getURLAllFestivalsScheduled(),
                response -> {

                    ArrayList<Festival> festivals = new ArrayList<>();

                    // récupération des festivals programmés
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject festivalJSON = response.getJSONObject(i);

                            System.out.println(festivalJSON.toString());

                            this.updateFavoriteState(festivalJSON);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (festivals.isEmpty()) {
                        textError.setVisibility(View.VISIBLE);
                    } else {
                        festivalList.addAll(festivals);
                        System.out.println(festivalList);
                    }

                    FestivalAdapter adapter = new FestivalAdapter(getApplicationContext(),festivalList);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    error.printStackTrace();
                });

        getFileRequete().add(allScheduledFestival);
    }

    private void updateFavoriteState(JSONObject festivalJSON) {
        JsonArrayRequest userFavorites = new JsonArrayRequest(
                FestiplanApi.getURLFestivalAllFavorites(User.getInstance().getIdUser()),
                response -> {
                    boolean isFavorite = false;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject festivalFavoriteRow = response.getJSONObject(i);


                            if (festivalFavoriteRow.getInt("idFestival")
                                    == festivalJSON.getInt("idFestival")) {

                                isFavorite = true;
                            }

                            festivalList.add(new Festival(
                                    festivalJSON.getInt("idFestival"),
                                    festivalJSON.getString("nomFestival"),
                                    festivalJSON.getString("descriptionFestival"),
                                    festivalJSON.getInt("idImage"),
                                    festivalJSON.getString("imagePath"),
                                    festivalJSON.getString("dateDebutFestival"),
                                    festivalJSON.getString("dateFinFestival"),
                                    festivalJSON.getInt("idGriJ"),
                                    festivalJSON.getInt("idResponsable"),
                                    festivalJSON.getString("ville"),
                                    festivalJSON.getString("codePostal"),
                                    isFavorite
                            ));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if (festivalList.isEmpty()) {
                        textError.setVisibility(View.VISIBLE);
                    }

                    FestivalAdapter adapter = new FestivalAdapter(getApplicationContext(),festivalList);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    error.printStackTrace();
                }) {

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(userFavorites);
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