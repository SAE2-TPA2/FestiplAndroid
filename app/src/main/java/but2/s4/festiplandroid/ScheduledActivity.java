package but2.s4.festiplandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< Updated upstream
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

import but2.s4.festiplandroid.api.ApiResponse;
=======
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import but2.s4.festiplandroid.adaptater.FestivalAdapter;
>>>>>>> Stashed changes
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

public class ScheduledActivity extends AppCompatActivity {

    private TextView textError;
<<<<<<< Updated upstream
    private List<Festival> festivalList;
=======
    private ArrayList<Festival> festivalList;

    private RequestQueue fileRequete;
    private RecyclerView recyclerView;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

<<<<<<< Updated upstream
        // association de la vue avec l'activité
        setContentView(R.layout.activity_scheduled);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
=======
        setContentView(R.layout.activity_scheduled);

        festivalList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = calculateNoOfColumns();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
>>>>>>> Stashed changes

        textError = findViewById(R.id.error_festival_not_found);
        textError.setVisibility(View.INVISIBLE);

        // LinearLayout permettant de naviger vers les activités en favoris
        LinearLayout favorisButton = findViewById(R.id.buttonFavoritesFestival);
        favorisButton.setOnClickListener(v -> navigateToFavorites());

        // Bouton de deconnexion
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_scheduled);
        deconnexionButton.setOnClickListener(v -> navigateTosignOut());

<<<<<<< Updated upstream
        // appel de la métode de remplissage du recyclerView
        // avec les différents festival
        //loadAllFestivalsObject();
        //festivalList.add(new Festival(0,"nomFesti","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(1,"nomFesti","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",false));

        FestivalAdapter adapter = new FestivalAdapter(festivalList);
        recyclerView.setAdapter(adapter);
=======

        FestivalAdapter adapter = new FestivalAdapter(festivalList);
        recyclerView.setAdapter(adapter);

        // Méthode récupération de l'ensemble des festivals
        //loadAllFestivalsObject();

        //ajout temporaire en dur
        festivalList.add(new Festival(0,"nomFesti","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(1,"nomFesti2","categorie2","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",false));
        festivalList.add(new Festival(2,"nomFesti3","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(3,"nomFesti4","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(0,"nomFesti5","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(1,"nomFesti6","categorie2","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",false));
        festivalList.add(new Festival(2,"nomFesti7","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(0,"nomFesti8","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(1,"nomFesti9","categorie2","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",false));
        festivalList.add(new Festival(2,"nomFesti10","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(0,"nomFesti11","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));
        festivalList.add(new Festival(1,"nomFesti12","categorie2","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",false));
        festivalList.add(new Festival(2,"nomFesti13","categorie","description",0,"zefgg","dateDeb","dateFin",8,3,"ville","codepostal",true));

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        ApiResponse callback = response -> {
            Gson gson = new Gson();
            Type festivalType = new TypeToken<List<Festival>>() {
            }.getType();
            List<Festival> festivalFound = gson.fromJson(response, festivalType);

            if (festivalFound.isEmpty()) {
                textError.setVisibility(View.VISIBLE);
            } else {
                festivalList.addAll(festivalFound);
            }
        };
        FestiplanApi.createAllFestivalsApiListener(callback);
=======
        System.out.print("e_urnuçevçyuebrvçybeuyvb");
        JsonArrayRequest allScheduledFestival = new JsonArrayRequest(FestiplanApi.getAllFestivalsScheduled(),
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
<<<<<<< Updated upstream
                                    festivalJSON.getString("catégorie"),
=======
                                    festivalJSON.getString("categorieFestival"),
>>>>>>> Stashed changes
                                    festivalJSON.getString("descriptionFestival"),
                                    festivalJSON.getInt("idImage"),
                                    "",
                                    festivalJSON.getString("dateDebutFestival"),
                                    festivalJSON.getString("dateFinFestival"),
                                    festivalJSON.getInt("idGriJ"),
                                    festivalJSON.getInt("idResponsable"),
                                    festivalJSON.getString("ville"),
                                    festivalJSON.getString("codePostal"),
<<<<<<< Updated upstream
                                    festivalJSON.getBoolean("true")
=======
                                    festivalJSON.getBoolean("favorite")
>>>>>>> Stashed changes
                            ));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (festivals.isEmpty()) {
                        System.out.print("e_urnuçevçyuebrvçybeuyvb");
                        textError.setVisibility(View.VISIBLE);
                    } else {
                        System.out.print("e_urnuçevçyuebrvçybeuyvb");
                        festivalList.addAll(festivals);
                        System.out.println(festivalList);
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        getFileRequete().add(allScheduledFestival);
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
>>>>>>> Stashed changes
    }
}

