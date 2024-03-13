package but2.s4.festiplandroid.navigation;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class Navigator {
    public static void toActivity(AppCompatActivity activity, Class<?> target) {
        activity.startActivity(new Intent(activity, target));
    }
}
