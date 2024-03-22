package but2.s4.festiplandroid;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import but2.s4.festiplandroid.api.ApiResponse;
import but2.s4.festiplandroid.api.FestiplanApi;
import but2.s4.festiplandroid.festivals.Festival;
import but2.s4.festiplandroid.festivals.Organizer;
import but2.s4.festiplandroid.festivals.Scene;
import but2.s4.festiplandroid.festivals.Show;
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

    private LinearLayout showsList;

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
        this.showsList = this.findViewById(R.id.shows_list);
        this.scenesList = this.findViewById(R.id.scenes_list);

        this.loadFestivalObject();
    }

    private void loadFestivalObject() {
        ApiResponse callback = response -> {
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
            updateShowsList();
        };

        FestiplanApi.createFestivalApiListener(1, callback);
    }

    private void updateOrganizersList() {
        final String PATTERN_LIST
            = "<ul>%s</ul>";

        ApiResponse callback = response -> {
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
        };

        FestiplanApi.createFestivalOrganizersApiListener(this.currentFestival,
                                                         callback);
    }

    private void updateScenesList() {
        ApiResponse callback = response -> {
            final String SCENE_DESCRIPTION_PATTERN
                = "%s places · %s scène";

            Gson gson = new Gson();
            Type scenesType;
            List<Scene> scenesFound;

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

            for (Scene scene: scenesFound) {
                sceneLayout = new LinearLayout(DetailsActivity.this);
                sceneLayout.getContext()
                           .setTheme(R.style.details_scenes_list_item);
                sceneLayout.setOrientation(LinearLayout.VERTICAL);

                System.out.println(sceneLayout.getOrientation());

                sceneName = new TextView(DetailsActivity.this);
                sceneName.setTextAppearance(
                    R.style.details_scenes_list_item_title);
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
                    R.style.details_scenes_list_item_description);
                sceneDescription.setText(sceneDescriptionContent);

                sceneLayout.addView(sceneName);
                sceneLayout.addView(sceneDescription);

                this.scenesList.addView(sceneLayout);
            }
        };

        FestiplanApi.createFestivalScenesApiListener(this.currentFestival,
                                                     callback);
    }

    private void updateShowsList() {
        ApiResponse callback = response -> {
            Gson gson = new Gson();
            Type showsType;
            List<Show> showsFound;

            showsType = new TypeToken<List<Show>>() {}.getType();
            showsFound = gson.fromJson(response, showsType);

            if (showsFound.isEmpty()) {
                return;
            }

            for (Show currentShow: showsFound) {
                this.createShowRow(currentShow);
            }
        };
    }

    private void createShowRow(Show currentShow) {
        ConstraintLayout showLayout,
                         showDates;

        TextView showTitle,
                 showCategory,
                 showDescription,
                 showStartDate,
                 showEndDate;

        ImageView showDatesAngle;

        ConstraintLayout.LayoutParams showLayoutParams;

        showLayoutParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                                                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
        showLayout = new ConstraintLayout(this);
        showLayout.setLayoutParams(showLayoutParams);

        this.newShowPicture(showLayout);

        /*
        showDatesAngle = new ImageView(DetailsActivity.this);
        showDatesAngle.getContext().setTheme(R.style.details_shows_list_item_dates_angle);
        showDatesAngle.setImageResource(R.drawable.angle_down_dates);

        ConstraintLayout.LayoutParams showDatesAngleParams
                = new ConstraintLayout.LayoutParams(
                100,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        showDatesAngleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        showDatesAngleParams.bottomToBottom = R.id.show_start;
        showDatesAngleParams
        */

        this.showsList.addView(showLayout);
    }

    /**
     * Ajout d'une image pour un spectacle.
     *
     * @param showLayout
     */
    private void newShowPicture(ConstraintLayout showLayout) {
        View showPicture;

        ConstraintSet showPictureSet;

        showPicture = new View(this);
        showPicture.getContext().setTheme(R.style.details_shows_list_item_picture);

        showPictureSet = new ConstraintSet();
        showPictureSet.clone(showLayout);
        showPictureSet.connect(
                showPicture.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0);
        showPictureSet.connect(
                showPicture.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                0);
        showPictureSet.applyTo(showLayout);
    }

    private void newShowInformation(ConstraintLayout showLayout,
                                    Show currentShow) {

        ConstraintLayout showInformation;

        ConstraintLayout.LayoutParams showInformationParams;

        showInformation = new ConstraintLayout(this);
        showInformation.getContext().setTheme(R.style.details_shows_list_item);

        showInformationParams = new ConstraintLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        showInformationParams.topToTop = ConstraintLayout.LayoutParams.MATCH_PARENT;
        // TODO: showInformationParams.startToEnd =
    }
}