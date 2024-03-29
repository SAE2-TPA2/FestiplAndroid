package but2.s4.festiplandroid;

import android.content.Intent;
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

import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

public class FavoritesActivity extends AppCompatActivity {

    private TextView textError;
    private RequestQueue fileRequete;
    private RecyclerView recyclerView;

    private ArrayList<Festival> festivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_view_from_favorites);
        int numberOfColumns = calculateNoOfColumns();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        textError = findViewById(R.id.error_festival_not_found_from_favorites);
        textError.setVisibility(View.INVISIBLE);

        // LinearLayout permettant de naviger vers les activités en favoris
        LinearLayout allFestivalScheduledButton = findViewById(R.id.buttonScheduledFestival);
        allFestivalScheduledButton.setOnClickListener(v -> navigateToScheduled());

        // Bouton de deconnexion
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_favorite);

        this.festivals = new ArrayList<>();

        // Méthode récupération de l'ensemble des festivals
        loadFavoritesFestivalsObject();
    }

    /**
     * Déconnexion du compte utilisateur courant,
     * et redirection vers l'activité de connexion.
     */
    public void logout(View view) {
        User.getInstance().logout();
        Navigator.clearAndGoToActivity(this, LoginActivity.class);
    }

    /**
     * Méthode pour calculer le nombre de colonnes en fonction de la taille de l'écran
     * et des items
     */

    private int calculateNoOfColumns() {
        int columnWidthDp = 450; // Largeur souhaitée d'une colonne en dp
        float screenWidthDp = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density;
        int columnCount = (int) (screenWidthDp / columnWidthDp + 0.5); // Arrondi au nombre entier le plus proche
        return Math.max(columnCount, 1); // Au moins une colonne
    }

    /**
     * Méthode appelé lors du clique sur le bouton permettant à
     * l'utilisateur de consulter les fetivals programmés
     *
     * Redirige l'utilisateur vers la page contenant l'ensemble
     * des festivals programmés
     */
    private void navigateToScheduled() {
        Navigator.toActivity(FavoritesActivity.this, ScheduledActivity.class);
    }

    /**
     * récupère l'ensemble des festivals programmés
     */
    private void loadFavoritesFestivalsObject() {

        JsonArrayRequest allScheduledFestival = new JsonArrayRequest(
                        FestiplanApi.getURLFestivalAllFavorites(User.getInstance().getIdUser()),
                response -> {

                    // récupération des festivals programmés
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject festivalJSON = response.getJSONObject(i);

                            System.out.println(festivalJSON.toString());

                            // ajout du festivalJSON à la liste
                            this.loadFavoriteFestivalObject(festivalJSON.getInt("idFestival"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(allScheduledFestival);
    }

    private void loadFavoriteFestivalObject(int festivalId) {
        JsonArrayRequest favoriteFestival = new JsonArrayRequest(
                FestiplanApi.getURLDetailFestival(festivalId),
                response -> {
                    // récupération des festivals programmés
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject festivalJSON = response.getJSONObject(i);

                            // ajout du festivalJSON à la liste
                            this.festivals.add(new Festival(
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
                                    false
                            ));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    FestivalAdapter adapter = new FestivalAdapter(this.festivals);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    error.printStackTrace();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(favoriteFestival);
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

