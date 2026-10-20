package mindmerge.model;

import java.time.LocalDateTime;

public class Project {

    private int           projectId;
    private String        name;
    private String        status;
    private String        teamName;
    private int           decisionCount;
    private LocalDateTime lastActivity; // null when the project has no decisions yet

    public int getProjectId()                  { return projectId; }
    public void setProjectId(int v)            { this.projectId = v; }

    public String getName()                    { return name; }
    public void setName(String v)              { this.name = v; }

    public String getStatus()                  { return status; }
    public void setStatus(String v)            { this.status = v; }

    public String getTeamName()                { return teamName; }
    public void setTeamName(String v)          { this.teamName = v; }

    public int getDecisionCount()              { return decisionCount; }
    public void setDecisionCount(int v)        { this.decisionCount = v; }

    public LocalDateTime getLastActivity()     { return lastActivity; }
    public void setLastActivity(LocalDateTime v) { this.lastActivity = v; }
}
