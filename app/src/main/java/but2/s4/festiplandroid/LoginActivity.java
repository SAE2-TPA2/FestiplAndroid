package but2.s4.festiplandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onKeyboardToggling();
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