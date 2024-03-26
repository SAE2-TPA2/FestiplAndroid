package but2.s4.festiplandroid.festivals;

import but2.s4.festiplandroid.api.FestiplanApi;

public class Show {
    private int idSpectacle;

    private String titreSpectacle;

    private String descriptionSpectacle;

    private int idImage;

    private String imagePath;

    public int getIdSpectacle() {
        return idSpectacle;
    }

    public void setIdSpectacle(int idSpectacle) {
        this.idSpectacle = idSpectacle;
    }

    public String getTitreSpectacle() {
        return titreSpectacle;
    }

    public void setTitreSpectacle(String titreSpectacle) {
        this.titreSpectacle = titreSpectacle;
    }

    public String getDescriptionSpectacle() {
        return descriptionSpectacle;
    }

    public void setDescriptionSpectacle(String descriptionSpectacle) {
        this.descriptionSpectacle = descriptionSpectacle;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getImagePath() {
        return FestiplanApi.DOMAIN_API + imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
