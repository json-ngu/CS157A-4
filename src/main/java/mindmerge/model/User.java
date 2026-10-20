package mindmerge.model;

public class User {

    private int    userId;
    private String username;
    private String displayName;
    private String role;

    public int getUserId()               { return userId; }
    public void setUserId(int v)         { this.userId = v; }

    public String getUsername()          { return username; }
    public void setUsername(String v)    { this.username = v; }

    public String getDisplayName()       { return displayName; }
    public void setDisplayName(String v) { this.displayName = v; }

    public String getRole()              { return role; }
    public void setRole(String v)        { this.role = v; }
}
