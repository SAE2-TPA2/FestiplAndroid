package but2.s4.festiplandroid;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.errors.Error;
import but2.s4.festiplandroid.navigation.Navigator;
import but2.s4.festiplandroid.session.User;

/**
 * DetailsActivity est une classe qui présente davantage de
 * détails sur un festival programmé.
 * <p>
 * Cette classe permet également l'action utilisateur de
 * mise ou retrait de sa liste de festivals favoris.
 * <p>
 * Elle hérite de AppCompatActivity qui est une classe
 * de base pour les activités qui utilisent la barre d'action.
 */
public class DetailsActivity
extends AppCompatActivity {

    private TextView festivalName;

    private View picture;

    private TextView description;

    private TextView startDate;

    private TextView endDate;

    private TextView organizersList;

    private LinearLayout scenesList;

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
        this.setContentView(R.layout.activity_details);

        this.festivalName = this.findViewById(R.id.festivalName);
        this.festivalName.setText("Festival STUB");

        this.picture = this.findViewById(R.id.picture);
        // TODO: Set picture

        this.description = this.findViewById(R.id.description);
        this.description.setText(R.string.LOREM_TEMP);

        this.startDate = this.findViewById(R.id.start);
        this.startDate.setText("01/01/1970");

        this.endDate = this.findViewById(R.id.end);
        this.endDate.setText("02/01/1970");

        this.organizersList = this.findViewById(R.id.organizers_list);
        this.updateOrganizersList();

        this.scenesList = this.findViewById(R.id.scenes_list);
        this.updateScenesList();
    }

    private void updateOrganizersList() {
        final String PATTERN_LIST
            = "<ul>%s</ul>";

        String[] organizersStub = new String[] {
            "Organisateur 1",
            "Organisateur 2",
            "Organisateur 3"
        };

        StringBuilder listContent;

        listContent = new StringBuilder();

        for (String organizer: organizersStub) {
            listContent.append("<li>&nbsp;&nbsp;&nbsp;")
                       .append(organizer)
                       .append("</li>");
        }

        listContent = new StringBuilder(String.format(PATTERN_LIST,
                                                      listContent.toString()));

        this.organizersList
            .setText(Html.fromHtml(listContent.toString(),
                                   Html.FROM_HTML_MODE_COMPACT));
    }

    private void updateScenesList() {
        
    }
}