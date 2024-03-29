package but2.s4.festiplandroid.festivals;

import but2.s4.festiplandroid.api.FestiplanApi;

public class Festival {
    private int idFestival;

    private String nomFestival;

    private String categorie;

    private String descriptionFestival;

    private int idImage;

    private String imagePath;

    private String dateDebutFestival;

    private String dateFinFestival;

    private int idGriJ;

    private int idResponsable;

    private String ville;

    private String codePostal;

    private boolean favorite;

    public Festival(int idFestival, String nomFestival, String categorie,
                    String descriptionFestival, int idImage, String imagePath,
                    String dateDebutFestival, String dateFinFestival, int idGriJ,
                    int idResponsable, String ville, String codePostal,
                    boolean favorite) {

        this.idFestival = idFestival;
        this.nomFestival = nomFestival;
        this.categorie = categorie;
        this.descriptionFestival = descriptionFestival;
        this.idImage = idImage;
        this.imagePath = imagePath;
        this.dateDebutFestival = dateDebutFestival;
        this.dateFinFestival = dateFinFestival;
        this.idGriJ = idGriJ;
        this.idResponsable = idResponsable;
        this.ville = ville;
        this.codePostal = codePostal;
        this.favorite = favorite;
    }
  
    public int getIdFestival() {
        return this.idFestival;
    }

    public String getNomFestival() {
        return this.nomFestival;
    }

    public String getCategorieFestival(){ return this.categorie; }
    public String getDescriptionFestival() {
        return this.descriptionFestival;
    }

    public int getIdImage() {
        return this.idImage;
    }

    public String getImagePath() {
        return FestiplanApi.DOMAIN_API + this.imagePath;
    }

    public String getDateDebutFestival() {
        return this.dateDebutFestival;
    }

    public String getDateFinFestival() {
        return this.dateFinFestival;
    }

    public int getIdGriJ() {
        return this.idGriJ;
    }

    public int getIdResponsable() {
        return this.idResponsable;
    }

    public String getVille() {
        return this.ville;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public boolean getFavorite(){ return this.favorite; }

    public void setIdFestival(int idFestival) {
        this.idFestival = idFestival;
    }

    public void setNomFestival(String nomFestival) {
        this.nomFestival = nomFestival;
    }

    public void setCategorieFestival(String categorieFestival){
        this.categorie = categorieFestival; }

    public void setDescriptionFestival(String descriptionFestival) {
        this.descriptionFestival = descriptionFestival;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDateDebutFestival(String dateDebutFestival) {
        this.dateDebutFestival = dateDebutFestival;
    }

    public void setDateFinFestival(String dateFinFestival) {
        this.dateFinFestival = dateFinFestival;
    }

    public void setIdGriJ(int idGriJ) {
        this.idGriJ = idGriJ;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setFavorite(boolean favoris){ this.favorite = favoris; }
}
