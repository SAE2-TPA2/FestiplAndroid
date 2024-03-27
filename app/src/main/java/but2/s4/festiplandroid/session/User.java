package but2.s4.festiplandroid.session;

public final class User {
    private static final int DISCONNECTED_ID_VALUE = -1;

    private String nomUser;

    private String prenomUser;

    private int idUser;

    private String loginUser;


    private String APIKey;

    private static User instance;

    private User() {
        this.nomUser = null;
        this.prenomUser = null;
        this.idUser = DISCONNECTED_ID_VALUE;
        this.loginUser = null;
        this.APIKey = null;
    }

    public boolean isLogged() {
        return this.prenomUser != null
               && this.nomUser != null
               && this.idUser > -1
               && this.loginUser != null;
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

    public int getIdUser() {
        return this.idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLoginUser() {
        return this.loginUser;
    }

    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public void logout() {
        instance = new User();
    }

    public static User getInstance() {
        if (instance == null) {
            return instance = new User();
        } else {
            return instance;
        }
    }
}
