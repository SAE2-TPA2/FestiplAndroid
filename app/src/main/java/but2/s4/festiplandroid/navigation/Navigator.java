package but2.s4.festiplandroid.navigation;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Navigator {
    public static void toActivity(AppCompatActivity activity, Class<?> target) {
        activity.startActivity(new Intent(activity, target));
    }

    public static void toActivity(AppCompatActivity activity, Class<?> target, HashMap<String, String> extras) {
        Intent intent = new Intent(activity, target);

        for (String key : extras.keySet()) {
            intent.putExtra(key, extras.get(key));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void clearAndGoToActivity(AppCompatActivity activity, Class<?> target) {
        Intent intent = new Intent(activity, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

}
