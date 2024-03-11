package but2.s4.festiplandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.Console;

import but2.s4.festiplandroid.errors.Error;
import but2.s4.festiplandroid.navigation.Navigator;

public class LoginActivity extends AppCompatActivity {
    private TextView error;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        error = findViewById(R.id.login_form__error);

        loginButton = findViewById(R.id.login_form__login_button);
        loginButton.setOnClickListener(v -> attemptLogin());

        onKeyboardToggling();
    }

    private void attemptLogin() {
        boolean stub = false;

        if (stub) {
            if (this.error.getVisibility() == TextView.GONE) {
                this.error.setVisibility(TextView.VISIBLE);
            }

            Navigator.toActivity(this, this.getClass());
        } else {
            String introduction,
                   message;

            introduction = this.getText(R.string.error_introduction).toString();
            message = this.getText(R.string.login_form_error_invalid_credentials).toString();

            this.error.setVisibility(TextView.VISIBLE);
            this.error.setText(Error.getPreparedMessage(introduction, message));
        }
    }

    /**
     * Resize layout when keyboard is toggled
     * to keep showing the login button.
     */
    private void onKeyboardToggling() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams
                        .SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams
                        .SOFT_INPUT_ADJUST_RESIZE);
    }
}