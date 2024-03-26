package but2.s4.festiplandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.errors.Error;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

/**
 * LoginActivity est une classe qui gère l'activité de
 * connexion de l'application.
 * <p>
 * Elle hérite de AppCompatActivity qui est une classe
 * de base pour les activités qui utilisent la barre d'action.
 */
public class LoginActivity
extends AppCompatActivity {

    private EditText login;

    private EditText password;

    private TextView error;

    private Button loginButton;

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
        this.setContentView(R.layout.activity_login);

        // Initialisation de la vue d'erreur
        this.error = findViewById(R.id.login_form__error);

        this.login = findViewById(R.id.login_form__id__input);
        this.login.setText("");

        this.password = findViewById(R.id.login_form__password__input);
        this.login.setText("");

        // Initialisation du bouton de connexion et
        // définition de son comportement lorsqu'il est
        // cliqué
        this.loginButton = findViewById(R.id.login_form__login_button);
        this.loginButton.setOnClickListener(v -> attemptLogin());

        // Appel de la méthode pour gérer le
        // redimensionnement de la mise en page lorsque
        // le clavier est affiché
        this.onKeyboardToggling();
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur
     * tente de se connecter.
     *
     * Elle vérifie les informations de connexion et
     * affiche un message d'erreur si nécessaire.
     */
    private void attemptLogin() {
        System.out.println("Click");
        final String[] loginAttemptApiResponse = new String[1];

        if (this.login.getText().length() == 0 || this.password.getText().length() == 0) {
            this.erreurIdentifiants();
            return;
        }
        System.out.println("Credential valide");

        ApiResponse response = new ApiResponse() {
            @Override
            public void onResponse(String response) {
                loginAttemptApiResponse[0] = response;

                if (loginAttemptApiResponse[0] == null) {
                    if (error.getVisibility() == TextView.GONE) {
                        error.setVisibility(TextView.VISIBLE);
                    }

                    String introduction,
                        message;

                    introduction = getText(R.string.error_introduction).toString();
                    message = getText(R.string.error_server_down).toString();

                    error.setText(Error.getPreparedMessage(introduction, message));

                    return;
                }

                System.out.println(loginAttemptApiResponse[0]);

                if (!loginAttemptApiResponse[0].equals("false")) {
                    // Si on a pas "false" en réponse, c'est que la connexion a réussi
                    if (error.getVisibility() == TextView.GONE) {
                        error.setVisibility(TextView.VISIBLE);
                    }

                    System.out.println(Arrays.toString(loginAttemptApiResponse));
                    Log.d("QUENTIN", Arrays.toString(loginAttemptApiResponse));

                    User userInstance;
                    userInstance = User.getInstance();

                    userInstance.setFirstname(loginAttemptApiResponse[0]);
                    userInstance.setLastname(loginAttemptApiResponse[1]);
                    userInstance.setId(Integer.parseInt(loginAttemptApiResponse[2]));
                    userInstance.setLogin(login.getText().toString());
                    userInstance.setAPIKey(loginAttemptApiResponse[3]);

                    HashMap<String, String> extras = new HashMap<>();
                    extras.put(ScheduledActivity.EXTRA_LOGIN, login.getText().toString());
                    extras.put(ScheduledActivity.EXTRA_PASSWORD, password.getText().toString());

                    Navigator.toActivity(LoginActivity.this, ScheduledActivity.class, extras);
                } else {
                    erreurIdentifiants();
                }
            }
        };

        FestiplanApi.createLoginApiListener(this.login.getText().toString(),
                                            this.password.getText().toString(),
                                            response);  // TODO: STUB

    }

    private void erreurIdentifiants() {
        String introduction,
               message;

        introduction = getText(R.string.error_introduction).toString();
        message = getText(R.string.login_form_error_wrong_credentials).toString();

        error.setVisibility(TextView.VISIBLE);
        error.setText(Error.getPreparedMessage(introduction, message));
    }

    /**
     * Cette méthode redimensionne la mise en page lorsque
     * le clavier est affiché pour continuer à afficher le
     * bouton de connexion.
     */
    private void onKeyboardToggling() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams
                        .SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams
                        .SOFT_INPUT_ADJUST_RESIZE);
    }
}