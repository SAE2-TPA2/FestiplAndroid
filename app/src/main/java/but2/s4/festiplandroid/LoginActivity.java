package but2.s4.festiplandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import but2.s4.festiplandroid.errors.Error;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

/**
 * LoginActivity est une classe qui gère l'activité de
 * connexion de l'application.
 *
 * Elle hérite de AppCompatActivity qui est une classe
 * de base pour les activités qui utilisent la barre d'action.
 */
public class LoginActivity extends AppCompatActivity {
    // TextView pour afficher les erreurs
    private TextView error;

    // Bouton pour déclencher la tentative de connexion
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
        setContentView(R.layout.activity_login);

        // Initialisation de la vue d'erreur
        error = findViewById(R.id.login_form__error);

        // Initialisation du bouton de connexion et
        // définition de son comportement lorsqu'il est
        // cliqué
        loginButton = findViewById(R.id.login_form__login_button);
        loginButton.setOnClickListener(v -> attemptLogin());

        // Appel de la méthode pour gérer le
        // redimensionnement de la mise en page lorsque
        // le clavier est affiché
        onKeyboardToggling();
    }

    /**
     * Cette méthode est appelée lorsque l'utilisateur
     * tente de se connecter.
     *
     * Elle vérifie les informations de connexion et
     * affiche un message d'erreur si nécessaire.
     */
    private void attemptLogin() {
        boolean stub = true;

        if (stub) {
            if (this.error.getVisibility() == TextView.GONE) {
                this.error.setVisibility(TextView.VISIBLE);
            }

            User userInstance;
            userInstance = User.getInstance();
            userInstance.setFirstname("Jean");
            userInstance.setLastname("Dupont");
            userInstance.setId(10);
            userInstance.setLogin("jeandup");

            Navigator.toActivity(this, this.getClass());
        } else {
            String introduction,
                   message;

            introduction = this.getText(R.string.error_introduction).toString();
            message = "Identifiants incorrects [STUB]";  // TODO: STUB

            this.error.setVisibility(TextView.VISIBLE);
            this.error.setText(Error.getPreparedMessage(introduction, message));
        }
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