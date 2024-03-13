package but2.s4.festiplandroid.errors;

import android.text.Html;
import android.text.Spanned;

public class Error {
    public static Spanned getPreparedMessage(String introduction, String message) {
        return Html.fromHtml("<b>" + introduction + "</b>\n" + message,
                             Html.FROM_HTML_MODE_LEGACY);
    }
}
