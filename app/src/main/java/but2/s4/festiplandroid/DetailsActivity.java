package but2.s4.festiplandroid;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * File d'attente pour les requêtes Web (en lien avec l'utilisation de Volley)
     */
    private RequestQueue fileRequete;

    private TextView festivalName;

    private ConstraintLayout favoriteToggler;

    private ImageView favoriteIcon;

    private ImageView picture;

    private TextView description;

    private TextView startDate;

    private TextView endDate;

    private TextView organizersList;

    private LinearLayout scenesList;

    private LinearLayout showsList;

    private ConstraintLayout festivalNameAndFavoriteTogglerContainer;

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

        this.festivalNameAndFavoriteTogglerContainer
            = this.findViewById(R.id.festivalNameAndFavoriteToggler);
        this.festivalName = this.findViewById(R.id.festivalName);
        this.favoriteToggler = this.findViewById(R.id.favorite_toggler);
        this.favoriteIcon = this.findViewById(R.id.favorite_icon);
        this.picture = this.findViewById(R.id.picture);
        this.description = this.findViewById(R.id.description);
        this.startDate = this.findViewById(R.id.start);
        this.endDate = this.findViewById(R.id.end);
        this.organizersList = this.findViewById(R.id.organizers_list);
        this.showsList = this.findViewById(R.id.shows_list);
        this.scenesList = this.findViewById(R.id.scenes_list);

        this.festivalId = setFestivalId();

        this.loadFestivalObject();
    }

    private int setFestivalId() {
        Intent intent;
        intent = getIntent();

        if (intent != null && intent.hasExtra("ID_EXTRA")) {
            return intent.getIntExtra("ID_EXTRA", 1);
        }

        return 1;
    }

    private void loadFestivalObject() {
        String urlDetailFestival = FestiplanApi.getURLDetailFestival(this.festivalId);

        JsonArrayRequest festivalDetailRequest = new JsonArrayRequest(urlDetailFestival,
                response -> {
                    try {
                        System.out.println(response.get(0));
                        response.get(0);

                        JSONObject festivalRecu = (JSONObject) response.get(0);

                        this.currentFestival = new Festival(
                                (int) festivalRecu.get("idFestival"),
                                (String) festivalRecu.get("nomFestival"),
                                (String) festivalRecu.get("descriptionFestival"),
                                (int) festivalRecu.get("idImage"),
                                (String) festivalRecu.get("imagePath"),
                                (String) festivalRecu.get("dateDebutFestival"),
                                (String) festivalRecu.get("dateFinFestival"),
                                (int) festivalRecu.get("idGriJ"),
                                (int) festivalRecu.get("idResponsable"),
                                (String) festivalRecu.get("ville"),
                                (String) festivalRecu.get("codePostal"),
                                false
                        );

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

                        updateFavoriteToggler();
                        updateOrganizersList();
                        updateScenesList();
                        updateShowsList();

                    } catch (JSONException e) {
                        Navigator.toActivity(DetailsActivity.this, ScheduledActivity.class);
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    System.out.println("Erreur lors de la récupération des détails du festival");
                    error.printStackTrace();
                });

        getFileRequete().add(festivalDetailRequest);
    }

    private void updateFavoriteToggler() {
        String uriFestivalFavorites
            = FestiplanApi.getURLFestivalAllFavorites(User.getInstance().getIdUser());

        JsonArrayRequest favoriteFestivalRequest
                = new JsonArrayRequest(uriFestivalFavorites,
                response -> {
                    try {
                        JSONObject responseObject;

                        this.currentFestival.setFavorite(false);

                        for (int i = 0; i < response.length(); i++) {
                            responseObject = response.getJSONObject(i);

                            if (responseObject.getInt("idFestival")
                                    == this.currentFestival.getIdFestival()) {

                                this.currentFestival.setFavorite(true);
                                break;
                            }
                        }

                        if (this.currentFestival.getFavorite()) {
                            this.favoriteToggler.setBackground(
                                    getDrawable(R.drawable.favorites_on_button));
                            this.favoriteIcon.setImageResource(R.drawable.favorites_selected);
                        } else {
                            this.favoriteToggler.setBackground(
                                    getDrawable(R.drawable.favorites_off_button));
                            this.favoriteIcon.setImageResource(R.drawable.favorites_deselected);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                        this.favoriteToggler.setBackground(
                                getDrawable(R.drawable.favorites_off_button));
                        this.favoriteIcon.setImageResource(R.drawable.favorites_deselected);
                    }

                    System.out.println("Erreur lors de la récupération des favoris du festival");
                    error.printStackTrace();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(favoriteFestivalRequest);
    }

    public void toggleFavoriteState(View view) {
        if (this.currentFestival.getFavorite()) {
            this.removeFavorite();
        } else {
            this.addFavorite();
        }
    }

    private void addFavorite() {
        String uriAddFavorite
            = FestiplanApi.getURLFestivalSetFavorites();

        StringRequest addFavoriteRequest
            = new StringRequest(Request.Method.POST,
                uriAddFavorite,
                response -> {
                    this.currentFestival.setFavorite(true);
                    updateFavoriteToggler();
                },
                error -> {
                    System.out.println("Erreur lors de l'ajout du festival aux favoris");
                    error.printStackTrace();
                }) {

            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put("idUser", String.valueOf(User.getInstance().getIdUser()));
                params.put("idFestival", String.valueOf(currentFestival.getIdFestival()));
                return new JSONObject(params).toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(addFavoriteRequest);
    }

    private void removeFavorite() {
        String uriRemoveFavorite
            = FestiplanApi.getURLFestivalDeleteFavorites(User.getInstance().getIdUser(),
                                                         this.currentFestival.getIdFestival());

        StringRequest removeFavoriteRequest
            = new StringRequest(Request.Method.DELETE,
                uriRemoveFavorite,
                response -> {
                    this.currentFestival.setFavorite(false);
                    updateFavoriteToggler();
                },
                error -> {
                    System.out.println("Erreur lors de la suppression du festival des favoris");
                    error.printStackTrace();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("APIKEY", User.getInstance().getAPIKey());
                return headers;
            }
        };

        getFileRequete().add(removeFavoriteRequest);
    }

    /**
     * Récupère avec une requête API la liste des organisateurs
     * et les ajoute à la vue
     */
    private void updateOrganizersList() {
        final String PATTERN_LIST
                = "<ul>%s</ul>";

        JsonArrayRequest organizersFestivalRequest = new JsonArrayRequest(FestiplanApi.getURLFestivalOrganizers(this.currentFestival.getIdFestival()),
                response -> {
                    StringBuilder listContent;
                    System.out.println(response);

                    ArrayList<Organizer> organizersFound = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject organizer = response.getJSONObject(i);
                            organizersFound.add(new Organizer(
                                    organizer.getInt("idUser"),
                                    organizer.getString("nomUser"),
                                    organizer.getString("prenomUser"),
                                    organizer.getString("loginUser"),
                                    organizer.getInt("idFestival")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(organizersFound);

                    if (!organizersFound.isEmpty()) {
                        listContent = new StringBuilder();

                        for (Organizer currentOrganizer : organizersFound) {
                            String currentOrganizerCompleteName
                                    = currentOrganizer.getPrenomUser()
                                    + " "
                                    + currentOrganizer.getNomUser();

                            listContent.append("<li>&nbsp;&nbsp;&nbsp;")
                                    .append(currentOrganizerCompleteName)
                                    .append("</li>");
                        }

                        organizersList
                                .setText(Html.fromHtml(
                                        String.format(PATTERN_LIST, listContent.toString()),
                                        Html.FROM_HTML_MODE_COMPACT));
                    }
                },
                error -> {
                    System.out.println("Erreur lors de la récupération des organisateurs du festival");
                    error.printStackTrace();
                });

        getFileRequete().add(organizersFestivalRequest);
    }

    /**
     * Récupère avec une requête API la liste des scènes
     * et les ajoute à la vue
     */
    private void updateScenesList() {
        final String SCENE_DESCRIPTION_PATTERN
                = "%s places · %s scène";

        JsonArrayRequest sceneFestivalRequest = new JsonArrayRequest(FestiplanApi.getURLFestivalScenes(this.currentFestival.getIdFestival()),
                response -> {
                    StringBuilder listContent;
                    LinearLayout sceneLayout;
                    TextView sceneName;
                    TextView sceneDescription;

                    String sceneDescriptionContent,
                            sceneSize;
                    System.out.println(response);

                    ArrayList<Scene> scenesFound = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject scene = response.getJSONObject(i);
                            scenesFound.add(new Scene(
                                    scene.getInt("idScene"),
                                    scene.getString("nomScene"),
                                    scene.getString("tailleScene"),
                                    scene.getInt("spectateurMax"),
                                    scene.getString("coordonneesGPS"),
                                    scene.getInt("idFestival")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(scenesFound);

                    if (!scenesFound.isEmpty()) {
                        for (Scene scene : scenesFound) {
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

                    }
                },
                error -> {
                    System.out.println("Erreur lors de la récupération des scènes du festival");
                    error.printStackTrace();
                });

        getFileRequete().add(sceneFestivalRequest);
    }

    /**
     * Met à jour la liste des spectacles programmés
     */
    private void updateShowsList() {

        JsonArrayRequest showsFestivalRequest = new JsonArrayRequest(FestiplanApi.getURLFestivalShows(this.currentFestival.getIdFestival()),
                response -> {
                    ArrayList<Show> showsFound = new ArrayList<>();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject show = response.getJSONObject(i);
                            showsFound.add(new Show(
                                    show.getInt("idSpectacle"),
                                    show.getString("titreSpectacle"),
                                    show.getString("descriptionSpectacle"),
                                    show.getString("imagePath")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(showsFound);

                    if (!showsFound.isEmpty()) {
                        for (Show currentShow : showsFound) {
                            createShowRow(currentShow);
                        }
                    }
                },
                error -> {
                    System.out.println("Erreur lors de la récupération des spectacles du festival");
                    error.printStackTrace();
                });

        getFileRequete().add(showsFestivalRequest);
    }

    /**
     * Ajoute un sepcatcle à la liste des spectacles
     *
     * @param currentShow le spectacle à ajouter
     */
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
        finish();
    }

    /**
     * Renvoie la file d'attente pour les requêtes Web :
     * - si la file n'existe pas encore : elle est créée puis renvoyée
     * - si une file d'attente existe déjà : elle est renvoyée
     * On assure ainsi l'unicité de la file d'attente
     *
     * @return RequestQueue une file d'attente pour les requêtes Volley
     */
    private RequestQueue getFileRequete() {
        if (fileRequete == null) {
            fileRequete = Volley.newRequestQueue(this);
        }

        return fileRequete;
    }
}