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
import java.util.ArrayList;
import java.util.List;

import but2.s4.festiplandroid.adaptater.FestivalAdapter;
import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

/**
 * FavoritesActivity permet d'affiché l'ensemble des festivals
 * favoris de l'utilisateur et de gérer la deconnexion et la navigation vers
 * la page des festival programmé.
 * <p>
 * Elle hérite de AppCompatActivity qui est une classe
 * de base pour les activités qui utilisent la barre d'action.
 */
public class FavoritesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private TextView textError;
    private List<Festival> festivalFavoritesList;

    /**
     * Cette méthode est appelée à la création de
     * l'activité.
     *
     * Initialise la vue des festivals en favoris
     * et associe un comportement à chaque bouton
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recycler_festival_favori);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textError = this.findViewById(R.id.error_festival_not_found);
        textError.setVisibility(View.INVISIBLE);

        //recyclerView.setAdapter(festivalAdapter);
        LinearLayout scheduledButton = findViewById(R.id.buttonAllFestival);
        scheduledButton.setOnClickListener(v -> navigateToScheduled());
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_favorites);
        deconnexionButton.setOnClickListener(v -> navigateTosignOut());

        loadFavoritesFestivalsObject();
    }

    /**
     * Méthode appelé lors du clique sur le bouton permettant de
     * consulter la liste de l'ensemble des festivals
     * programmés
     *
     * Redirige l'utilisateur vers la page contenant l'ensemble
     * des festivals mis en favoris par l'utilisateur
     */
    private void navigateToScheduled() {
        Navigator.toActivity(FavoritesActivity.this, ScheduledActivity.class);
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
        Navigator.toActivity(FavoritesActivity.this, LoginActivity.class);
    }

    /**
     * récupère l'ensemble des festivals en favoris
     */
    private void loadFavoritesFestivalsObject() {
        ApiResponse callback = response -> {
            Gson gson = new Gson();
            Type festivalType = new TypeToken<List<Festival>>() {
            }.getType();
            List<Festival> festivalFound = gson.fromJson(response, festivalType);

            if (festivalFound.isEmpty()) {
                textError.setVisibility(View.VISIBLE);
            } else {
                festivalFavoritesList.addAll(festivalFound);
            }
        };
        FestiplanApi.createFavoritesFestivalsApiListener(User.getInstance().getId(), callback);
    }
}
