package but2.s4.festiplandroid.session;

public final class User {
    private static final int DISCONNECTED_ID_VALUE = -1;

    private String lastname;

    private String firstname;

    private int id;

    private String login;


    private String APIKey;

    private static User instance;

    private User() {
        this.lastname = null;
        this.firstname = null;
        this.id = DISCONNECTED_ID_VALUE;
        this.login = null;
        this.APIKey = null;
    }

    public boolean isLogged() {
        return this.firstname != null
               && this.lastname != null
               && this.id > -1
               && this.login != null;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public static User getInstance() {
        if (instance == null) {
            return instance = new User();
        } else {
            return instance;
        }
    }
}
