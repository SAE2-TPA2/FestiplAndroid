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

import com.bumptech.glide.Glide;
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

    private ImageView picture;

    private TextView description;

    private TextView startDate;

    private TextView endDate;

    private TextView organizersList;

    private LinearLayout scenesList;

    private LinearLayout showsList;

    private ConstraintLayout showLayout;

    private ConstraintLayout showInformation;

    private ConstraintLayout showDates;

    private TextView showTitle;

    private TextView showCategory;

    private TextView showDescription;

    private TextView showStartDate;

    private TextView showEndDate;

    private ImageView showPicture;

    private ImageView showDatesAngle;

    private Festival currentFestival;

    private int festivalId;

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

        this.festivalId = 1;  // TODO STUB

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
                                     ScheduledActivity.class);
                return;
            }

            this.currentFestival = festivalFound.get(0);

            this.picture.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(this)
                    .load(this.currentFestival.getImagePath())
                    .into(this.picture);

            this.festivalName.setText(this.currentFestival
                                  .getNomFestival());
            this.description.setText(this.currentFestival
                                 .getDescriptionFestival());
            this.startDate.setText(this.currentFestival
                               .getDateDebutFestival());
            this.endDate.setText(this.currentFestival
                             .getDateFinFestival());

            updateOrganizersList();
            updateScenesList();
            updateShowsList();
        };

        FestiplanApi.createFestivalApiListener(this.festivalId, callback);
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

        FestiplanApi.createFestivalShowsApiListener(this.currentFestival,
                                                    callback);
    }

    private void createShowRow(Show currentShow) {
        ConstraintLayout.LayoutParams showLayoutParams;

        showLayoutParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                                                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
        showLayoutParams.bottomMargin
                = getResources().getDimensionPixelSize(R.dimen.show_bottomMargin);
        this.showLayout = new ConstraintLayout(this);
        this.showLayout.setLayoutParams(showLayoutParams);

        this.newShowPicture(currentShow);
        this.createShowDatesContainer();
        this.newShowInformation(currentShow);

        this.showsList.addView(this.showLayout);
    }

    /**
     * Ajout d'une image pour un spectacle.
     */
    private void newShowPicture(Show currentShow) {
        int showPictureWidth,
            showPictureHeight;

        ConstraintSet showPictureSet;

        ConstraintLayout.LayoutParams showPictureParams;

        showPictureWidth
                = this.getResources()
                      .getDimensionPixelSize(R.dimen.show_picture_width);

        showPictureHeight
                = this.getResources()
                      .getDimensionPixelSize(R.dimen.show_picture_height);

        showPictureParams
                = new ConstraintLayout.LayoutParams(showPictureWidth,
                                                    showPictureHeight);

        this.showPicture = new ImageView(this);
        this.showPicture.setId(View.generateViewId());
        this.showPicture.setLayoutParams(showPictureParams);
        this.showPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this)
             .load(currentShow.getImagePath())
             .into(this.showPicture);

        showPictureSet = new ConstraintSet();
        showPictureSet.clone(this.showLayout);
        showPictureSet.connect(
                this.showPicture.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0);
        showPictureSet.connect(
                this.showPicture.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                0);
        showPictureSet.applyTo(this.showLayout);

        this.showLayout.addView(this.showPicture);
    }

    private void newShowInformation(Show currentShow) {
        //  CONTAINER INFORMATIONS SPECTACLE
        ConstraintLayout.LayoutParams showInformationParams;

        this.showInformation = new ConstraintLayout(this);
        this.showInformation.setId(View.generateViewId());
        this.showInformation.getContext().setTheme(R.style.details_shows_list_item);

        showInformationParams = new ConstraintLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        showInformationParams.topToTop = ConstraintLayout.LayoutParams.MATCH_PARENT;
        showInformationParams.startToEnd = this.showPicture.getId();
        showInformationParams.endToStart = this.showDates.getId();
        showInformationParams.leftMargin
                = getResources().getDimensionPixelSize(R.dimen.show_picture_endMargin);

        this.showInformation.setLayoutParams(showInformationParams);

        //  TITRE SPECTACLE
        this.createShowTitle(currentShow.getTitreSpectacle());

        this.showLayout.addView(this.showInformation);
    }

    /**
     * Crée l'élément de titre du spectacle à afficher.
     *
     * @param title Titre du spectacle en chaîne de
     *              caractères
     */
    private void createShowTitle(String title) {
        ConstraintSet showTitleSet;

        this.showTitle = new TextView(this);
        this.showTitle.setTextAppearance(R.style.details_shows_list_item_title);
        this.showTitle.setText(title);

        showTitleSet = new ConstraintSet();
        showTitleSet.clone(this.showInformation);
        showTitleSet.connect(
                this.showTitle.getId(),
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0);
        showTitleSet.connect(
                this.showTitle.getId(),
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                0);

        showTitleSet.applyTo(this.showInformation);

        this.showInformation.addView(this.showTitle);
    }

    /**
     * Crée le container des dates de spectacle.
     */
    private void createShowDatesContainer() {
        ConstraintLayout.LayoutParams showDatesParams,
                                      showStartDateParams,
                                      showAngleParams,
                                      showEndDateParams;

        //  CONTAINER DATES SPECTACLE
        showDatesParams = new ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        this.showDates = new ConstraintLayout(this);
        this.showDates.setId(View.generateViewId());
        this.showDates.getContext().setTheme(
                R.style.details_shows_list_item_dates);

        showDatesParams.topToTop
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showDatesParams.bottomToBottom
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showDatesParams.endToEnd
                = ConstraintLayout.LayoutParams.PARENT_ID;

        this.showDates.setLayoutParams(showDatesParams);

        //  START DATE
        showStartDateParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        this.showStartDate = this.addDateToShow("01/01/1980");

        showStartDateParams.topToTop
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showStartDateParams.startToStart
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showStartDateParams.endToEnd
                = ConstraintLayout.LayoutParams.PARENT_ID;

        this.showStartDate.setLayoutParams(showStartDateParams);

        //  ANGLE DOWN
        showAngleParams = new ConstraintLayout.LayoutParams(
                getResources().getDimensionPixelSize(
                        R.dimen.show_dates_angle_width),
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        this.showDatesAngle = new ImageView(this);
        this.showDatesAngle.setId(View.generateViewId());
        this.showDatesAngle.getContext().setTheme(
                R.style.details_shows_list_item_dates_angle);
        this.showDatesAngle.setImageResource(
                R.drawable.angle_down_dates);

        showAngleParams.topToBottom
                = this.showStartDate.getId();
        showAngleParams.startToStart
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showAngleParams.endToEnd
                = ConstraintLayout.LayoutParams.PARENT_ID;

        this.showDatesAngle.setLayoutParams(showAngleParams);
        this.showDates.addView(this.showDatesAngle);

        //  END DATE
        showEndDateParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);

        this.showEndDate = this.addDateToShow("02/01/1980");

        showEndDateParams.topToBottom
                = this.showDatesAngle.getId();
        showEndDateParams.startToStart
                = ConstraintLayout.LayoutParams.PARENT_ID;
        showEndDateParams.endToEnd
                = ConstraintLayout.LayoutParams.PARENT_ID;

        this.showEndDate.setLayoutParams(showEndDateParams);

        this.showLayout.addView(this.showDates);
    }

    /**
     * Ajoute une date de spectacle à son container des
     * dates.
     *
     * @param date Date à afficher
     * @return Élément d'affichage de la date créé
     */
    private TextView addDateToShow(String date) {
        TextView showDate;

        showDate = new TextView(this);
        showDate.setId(View.generateViewId());
        showDate.setTextAppearance(R.style.details_shows_list_item_dates_date);
        showDate.setText(date);

        this.showDates.addView(showDate);

        return showDate;
    }

    public void goToScheduled(View view) {  // FIXME: vérifier si redirection effective
        Navigator.toActivity(this, ScheduledActivity.class);
    }

    /**
     * Déconnexion du compte utilisateur courant,
     * et redirection vers l'activité de connexion.
     */
    public void logout(View view) {
        User.getInstance().logout();
        Navigator.clearAndGoToActivity(this, LoginActivity.class);
    }
}