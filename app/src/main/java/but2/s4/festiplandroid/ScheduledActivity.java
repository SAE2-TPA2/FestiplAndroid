package but2.s4.festiplandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.adaptater.FestivalAdapter;

public class ScheduledActivity extends AppCompatActivity {
    public static final String EXTRA_LOGIN = "com.exemple.intention.login";
    public static final String EXTRA_PASSWORD = " com.exemple.intention.password";

    // Conservation du login et du password pour identifier
    // l'utilisateur dans les différentes activités
    String login;
    String password;

    private RecyclerView recyclerView;
    private FestivalAdapter festivalAdapter;
    private List<Festival> festivalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled);

        Intent intent = getIntent();
        login = intent.getStringExtra(EXTRA_LOGIN);
        password = intent.getStringExtra(EXTRA_PASSWORD);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        festivalList = new ArrayList<>();
        festivalAdapter = new FestivalAdapter(festivalList, this);
        recyclerView.setAdapter(festivalAdapter);

        getAllFestivals();
    }

    private void getAllFestivals() {

        final String[] listeAllFestival = new String[1];
        ApiResponse response = new ApiResponse() {
            @Override
            public void onResponse(String response) {
                listeAllFestival[0] = response;
                if (listeAllFestival[0] == null) {
                    //TODO build un texteView avec réponse
                    System.out.println(listeAllFestival[0]);
                } else {
                    System.out.println(listeAllFestival[0]);
                    ArrayList<Festival> festivalList = new ArrayList<>();
                    for (:
                         ) {
                        
                    }
                }
            }
        };
        FestiplanApi.createAllFestivalsApiListener(response);
    }
}