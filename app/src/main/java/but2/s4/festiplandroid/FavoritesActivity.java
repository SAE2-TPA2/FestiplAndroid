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

public class FavoritesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private TextView textError;
    private List<Festival> festivalFavoritesList;
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

    private void navigateToScheduled() {
        Navigator.toActivity(FavoritesActivity.this, ScheduledActivity.class);
    }

    private void navigateTosignOut() {
        User.getInstance().setPrenomUser(null);
        User.getInstance().setNomUser(null);
        User.getInstance().setIdUser(-1);
        User.getInstance().setLoginUser(null);
        Navigator.toActivity(FavoritesActivity.this, LoginActivity.class);
    }

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
        FestiplanApi.createFavoritesFestivalsApiListener(User.getInstance().getIdUser(), callback);
    }
}
