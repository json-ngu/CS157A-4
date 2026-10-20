package mindmerge.model;

/** A signed-in user, as kept in the session. Never carries the password hash. */
public class User {

    private int    userId;
    private String fullName;
    private String email;
    private String status;

    public int getUserId()               { return userId; }
    public void setUserId(int v)         { this.userId = v; }

    public String getFullName()          { return fullName; }
    public void setFullName(String v)    { this.fullName = v; }

    public String getEmail()             { return email; }
    public void setEmail(String v)       { this.email = v; }

    public String getStatus()            { return status; }
    public void setStatus(String v)      { this.status = v; }

    public boolean isActive()            { return "active".equals(status); }
}
