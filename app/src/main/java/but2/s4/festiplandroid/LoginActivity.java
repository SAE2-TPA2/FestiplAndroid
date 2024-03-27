package but2.s4.festiplandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

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

    private static final String TAG = "François.LoginActivity";

    private EditText login;

    private EditText password;

    private TextView error;

    private Button loginButton;

    /**
     * File d'attente pour les requêtes Web (en lien avec l'utilisation de Volley)
     */
    private RequestQueue fileRequete;

    /**
     * Cette méthode est appelée à la création de
     * l'activité.
     * <p>
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
        this.loginButton.setOnClickListener(v -> {
            try {
                attemptLogin();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });

        // Appel de la méthode pour gérer le
        // redimensionnement de la mise en page lorsque
        // le clavier est affiché
        this.onKeyboardToggling();
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur
     * tente de se connecter.
     * <p>
     * Elle vérifie les informations de connexion et
     * affiche un message d'erreur si nécessaire.
     */
    private void attemptLogin() throws UnsupportedEncodingException {
        System.out.println("Click");
        final String[] loginAttemptApiResponse = new String[1];

        if (this.login.getText().length() == 0 || this.password.getText().length() == 0) {
            this.erreurIdentifiants();
        } else {
            // les ientifiants saisi par l'utilisateur sont récupéré et encodé en UTF-8
            String loginSaisie = URLEncoder.encode(this.login.getText().toString(), "UTF-8");
            String passwordSaisie = URLEncoder.encode(this.password.getText().toString(), "UTF-8");

            String url = FestiplanApi.getURLConnexon(loginSaisie, passwordSaisie);
            Log.d(TAG, url);

            /*
             * on crée une requête GET, paramètrée par l'url préparée ci-dessus,
             * Le résultat de cette requête sera une chaîne de caractères, donc la requête
             * est de type StringRequest
             */
            JsonObjectRequest requeteVolley = new JsonObjectRequest(Request.Method.GET, url, null,
                // écouteur de la réponse renvoyée par la requête
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject reponse) {
                        Log.d(TAG, "onResponse: " + reponse);

                        User user = User.getInstance();

                        try {
                            user.setLoginUser(reponse.getString("loginUser"));
                            user.setNomUser(reponse.getString("nomUser"));
                            user.setPrenomUser(reponse.getString("prenomUser"));
                            user.setIdUser(reponse.getInt("idUser"));
                            user.setAPIKey(reponse.getString("APIKey"));

                            Navigator.toActivity(LoginActivity.this, ScheduledActivity.class);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                // écouteur du retour de la requête si aucun résultat n'est renvoyé
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError erreur) {
                        erreur.printStackTrace();
//                        Log.d(TAG, "onErrorResponse: "+ );
                    }
                }
            );

            // la requête est placée dans la file d'attente des requêtes
            getFileRequete().add(requeteVolley);
        }


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