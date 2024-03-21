package but2.s4.festiplandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ScheduledActivity
        extends AppCompatActivity {
    public static final String EXTRA_LOGIN = "com.exemple.intention.login";
    public static final String EXTRA_PASSWORD = " com.exemple.intention.password";

    // Conservation du login et du password pour identifier
    // l'utilisateur dans les différentes activitées
    String login;
    String password;

    /**
     * Cette méthode est appelée à la création de
     * l'activité.
     *
     * Elle initialise les vues et définit les
     * comportements des boutons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intention = getIntent();

        login = intention.getStringExtra(EXTRA_LOGIN);
        password = intention.getStringExtra(EXTRA_PASSWORD);

        // Initialisation du bouton de redirection vers les favoris et
        // redirige vers la page des favoris
        Button favoriteButton = findViewById(R.id.login_form__login_button);
        //favoriteButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // Redirection vers SecondActivity lors du clic sur le bouton
        //        //Intent intent = new Intent(ScheduledActivity.this, FavoritesActivity.class);
        //        //intention.putExtra(EXTRA_LOGIN, login);
        //        //intention.putExtra(EXTRA_PASSWORD, password);
        //        //startActivity(intent);
        //    }
        //});

        // Initialisation du bouton de deconnexion
        // redirige vers la page de connexion
        Button signOutButton = findViewById(R.id.login_form__login_button);
        //signOutButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        // Redirection vers SecondActivity lors du clic sur le bouton
        //        Intent intent = new Intent(ScheduledActivity.this, LoginActivity.class);
        //        startActivity(intent);
        //    }
        //});


    }
}
