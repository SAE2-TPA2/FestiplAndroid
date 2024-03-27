package but2.s4.festiplandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

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

public class ScheduledActivity extends AppCompatActivity {

    // Conservation du login et du password pour identifier
    // l'utilisateur dans les différentes activités
    String login;
    String password;

    private TextView textError;
    private List<Festival> festivalList;

    public ScheduledActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textError = this.findViewById(R.id.error_festival_not_found);
        textError.setVisibility(View.INVISIBLE);

        LinearLayout favorisButton = findViewById(R.id.buttonFavoritesFestival);
        favorisButton.setOnClickListener(v -> navigateToFavorites());
        ImageButton deconnexionButton = findViewById(R.id.sign_out_from_scheduled);
        deconnexionButton.setOnClickListener(v -> navigateTosignOut());

        loadAllFestivalsObject();
    }

    private void navigateTosignOut() {
        User.getInstance().setPrenomUser(null);
        User.getInstance().setNomUser(null);
        User.getInstance().setIdUser(-1);
        User.getInstance().setLoginUser(null);
        Navigator.toActivity(ScheduledActivity.this, LoginActivity.class);
    }

    private void navigateToFavorites() {
        Navigator.toActivity(ScheduledActivity.this, FavoritesActivity.class);
    }

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