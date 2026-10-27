package mindmerge.model;

/** The person a page is shown to: name plus where they belong, for headers. */
public class Viewer {

    private int    userId;
    private String fullName;
    private String companies; // comma-separated company names
    private String teams;     // comma-separated team names
    private boolean admin;    // owner or admin at any of their companies
    private boolean reviewer; // lead or approver on at least one current team, so may approve decisions
    private boolean allowInvites;

    public int getUserId()                { return userId; }
    public void setUserId(int v)          { this.userId = v; }

    public String getFullName()           { return fullName; }
    public void setFullName(String v)     { this.fullName = v; }

    public String getFirstName() {
        int space = fullName == null ? -1 : fullName.indexOf(' ');
        return space < 0 ? fullName : fullName.substring(0, space);
    }

    public String getCompanies()          { return companies; }
    public void setCompanies(String v)    { this.companies = v; }

    public String getTeams()              { return teams; }
    public void setTeams(String v)        { this.teams = v; }

    public boolean isAdmin()              { return admin; }
    public void setAdmin(boolean v)       { this.admin = v; }

    public boolean isReviewer()           { return reviewer; }
    public void setReviewer(boolean v)    { this.reviewer = v; }

    public boolean isAllowInvites()       { return allowInvites; }
    public void setAllowInvites(boolean v) { this.allowInvites = v; }

    public boolean isInCompany()          { return companies != null && !companies.isEmpty(); }
}
