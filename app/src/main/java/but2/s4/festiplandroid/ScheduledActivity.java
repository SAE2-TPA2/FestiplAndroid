package but2.s4.festiplandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

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
    private List<Festival> festivalList;

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

        // association de la vue avec l'activité
        setContentView(R.layout.activity_scheduled);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
        User.getInstance().setFirstname(null);
        User.getInstance().setLastname(null);
        User.getInstance().setId(-1);
        User.getInstance().setLogin(null);
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
    }
}