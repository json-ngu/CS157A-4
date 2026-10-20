package mindmerge.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Invite {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

    private int           inviteId;
    private int           companyId;
    private String        companyName;
    private String        userName;     // who was invited
    private String        userEmail;
    private String        role;
    private String        invitedByName;
    private LocalDateTime createdAt;

    public int getInviteId()                 { return inviteId; }
    public void setInviteId(int v)           { this.inviteId = v; }

    public int getCompanyId()                { return companyId; }
    public void setCompanyId(int v)          { this.companyId = v; }

    public String getCompanyName()           { return companyName; }
    public void setCompanyName(String v)     { this.companyName = v; }

    public String getUserName()              { return userName; }
    public void setUserName(String v)        { this.userName = v; }

    public String getUserEmail()             { return userEmail; }
    public void setUserEmail(String v)       { this.userEmail = v; }

    public String getRole()                  { return role; }
    public void setRole(String v)            { this.role = v; }

    public String getInvitedByName()         { return invitedByName; }
    public void setInvitedByName(String v)   { this.invitedByName = v; }

    public LocalDateTime getCreatedAt()      { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public String getCreatedDate()           { return createdAt == null ? "" : createdAt.format(DATE); }
}
