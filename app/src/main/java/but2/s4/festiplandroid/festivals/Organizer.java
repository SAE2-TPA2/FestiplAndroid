package but2.s4.festiplandroid.festivals;

public class Organizer {
    private int idUser;

    private String nomUser;

    private String prenomUser;

    private String loginUser;

    private int idFestival;

    public Organizer(int idUser, String nomUser, String prenomUser, String loginUser, int idFestival) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.loginUser = loginUser;
        this.idFestival = idFestival;
    }

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return this.nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return this.prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getLoginUser() {
        return this.loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }
}
