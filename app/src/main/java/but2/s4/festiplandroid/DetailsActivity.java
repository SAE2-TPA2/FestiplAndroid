package but2.s4.festiplandroid;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.festivals.Organizer;
import but2.s4.festiplandroid.festivals.Scene;
import but2.s4.festiplandroid.navigation.Navigator;

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

    private Festival currentFestival;

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
        this.picture = this.findViewById(R.id.picture);
        this.description = this.findViewById(R.id.description);
        this.startDate = this.findViewById(R.id.start);
        this.endDate = this.findViewById(R.id.end);
        this.organizersList = this.findViewById(R.id.organizers_list);
        this.scenesList = this.findViewById(R.id.scenes_list);

        this.loadFestivalObject();
    }

    private void loadFestivalObject() {
        ApiResponse callback = new ApiResponse() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type festivalType;
                List<Festival> festivalFound;

                festivalType = new TypeToken<List<Festival>>() {}.getType();
                festivalFound = gson.fromJson(response, festivalType);

                if (festivalFound.isEmpty()) {
                    Navigator.toActivity(DetailsActivity.this,
                                         LoginActivity.class);
                    // TODO: remplacer LoginActivity par la page des festivals programmés
                    return;
                }

                System.out.println(festivalFound.get(0));

                currentFestival = festivalFound.get(0);

                festivalName.setText(currentFestival
                                      .getNomFestival());
                description.setText(currentFestival
                                     .getDescriptionFestival());
                startDate.setText(currentFestival
                                   .getDateDebutFestival());
                endDate.setText(currentFestival
                                 .getDateFinFestival());

                updateOrganizersList();
                updateScenesList();
            }
        };

        FestiplanApi.createFestivalApiListener(1, callback);
    }

    private void updateOrganizersList() {
        final String PATTERN_LIST
            = "<ul>%s</ul>";

        ApiResponse callback = new ApiResponse() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type organizersType;
                List<Organizer> organizersFound;
                StringBuilder listContent;
                String currentOrganizerCompleteName;

                organizersType = new TypeToken<List<Organizer>>() {}.getType();
                organizersFound = gson.fromJson(response, organizersType);

                if (organizersFound.isEmpty()) {
                    return;
                }

                listContent = new StringBuilder();

                for (Organizer currentOrganizer: organizersFound) {
                    currentOrganizerCompleteName
                        = currentOrganizer.getPrenomUser()
                          + " "
                          + currentOrganizer.getNomUser();

                    listContent.append("<li>&nbsp;&nbsp;&nbsp;")
                               .append(currentOrganizerCompleteName)
                               .append("</li>");
                }

                listContent
                    = new StringBuilder(
                        String.format(PATTERN_LIST,
                                      listContent.toString()));

                organizersList
                    .setText(Html.fromHtml(listContent.toString(),
                                           Html.FROM_HTML_MODE_COMPACT));
            }
        };

        FestiplanApi.createFestivalOrganizersApiListener(this.currentFestival,
                                                         callback);
    }

    private void updateScenesList() {
        ApiResponse callback = new ApiResponse() {
            @Override
            public void onResponse(String response) {
                final String SCENE_DESCRIPTION_PATTERN
                    = "%s places · %s scène";

                Gson gson = new Gson();
                Type scenesType;
                List<Scene> scenesFound;
                Scene scene;

                LinearLayout sceneLayout;
                TextView sceneName;
                TextView sceneDescription;

                String sceneDescriptionContent,
                       sceneSize;

                scenesType = new TypeToken<List<Scene>>() {}.getType();
                scenesFound = gson.fromJson(response, scenesType);

                if (scenesFound.isEmpty()) {
                    return;
                }

                scene = scenesFound.get(0);

                sceneLayout = new LinearLayout(DetailsActivity.this);
                sceneLayout.setOrientation(LinearLayout.VERTICAL);

                sceneName = new TextView(DetailsActivity.this);
                sceneName.setTextAppearance(
                    R.style.details_organizers_list_item_title);
                sceneName.setText(scene.getNomScene());

                switch (scene.getTailleScene()) {
                    case "1":
                        sceneSize = "petite";
                        break;

                    case "2":
                        sceneSize = "moyenne";
                        break;

                    case "3":
                        sceneSize = "grande";
                        break;

                    default:
                        sceneSize = "";
                        break;
                }

                sceneDescriptionContent
                    = String.format(SCENE_DESCRIPTION_PATTERN,
                                    scene.getSpectateurMax(),
                                    sceneSize);

                sceneDescription = new TextView(DetailsActivity.this);
                sceneDescription.setTextAppearance(
                    R.style.details_organizers_list_item_description);
                sceneDescription.setText(sceneDescriptionContent);

                sceneLayout.addView(sceneName);
                sceneLayout.addView(sceneDescription);

                scenesList.addView(sceneLayout);
            }
        };

        FestiplanApi.createFestivalScenesApiListener(this.currentFestival,
                                                     callback);
    }
}