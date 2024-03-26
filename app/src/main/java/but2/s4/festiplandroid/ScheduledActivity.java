package but2.s4.festiplandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.adaptater.FestivalAdapter;
import but2.s4.festiplandroid.navigation.Navigator;

public class ScheduledActivity extends AppCompatActivity {

    // Constants for intent extras
    public static final String EXTRA_LOGIN = "com.exemple.intention.login";
    public static final String EXTRA_PASSWORD = "com.exemple.intention.password";

    // Conservation du login et du password pour identifier
    // l'utilisateur dans les différentes activités
    String login;
    String password;

    private TextView textError;
    private RecyclerView recyclerView;
    private FestivalAdapter festivalAdapter;
    private List<Festival> festivalList;

    private FestivalAdapter.FestivalViewHolder festivalInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled);

        Intent intent = getIntent();
        login = intent.getStringExtra(EXTRA_LOGIN);
        password = intent.getStringExtra(EXTRA_PASSWORD);

        recyclerView = findViewById(R.id.recycler_view);
        textError = this.findViewById(R.id.error_festival_not_found);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        festivalList = new ArrayList<>();
        festivalAdapter = new FestivalAdapter(festivalList);
        festivalInit = null;
        recyclerView.setAdapter(festivalAdapter);
        textError.setVisibility(View.INVISIBLE);

        loadAllFestivalsObject();
    }

    private void loadAllFestivalsObject() {
        ApiResponse callback = response -> {
            Gson gson = new Gson();
            Type festivalType = new TypeToken<List<Festival>>() {}.getType();
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