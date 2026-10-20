package mindmerge.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Decision {

    private static final DateTimeFormatter SHORT_DATE = DateTimeFormatter.ofPattern("MMM d", Locale.US);

    private int           decisionId;
    private String        projectName;
    private String        title;
    private String        context;
    private String        chosenOption; // null while the decision is proposed
    private String        rationale;    // null while the decision is proposed
    private String        status;
    private String        decidedBy;    // full name of the creator
    private LocalDateTime decidedAt;    // null while the decision is proposed
    private LocalDateTime createdAt;
    private String        replacedBy;   // title of the decision that supersedes this one, if any
    private List<Tag>     tags = new ArrayList<>();

    public int getDecisionId()              { return decisionId; }
    public void setDecisionId(int v)        { this.decisionId = v; }

    public String getProjectName()          { return projectName; }
    public void setProjectName(String v)    { this.projectName = v; }

    public String getTitle()                { return title; }
    public void setTitle(String v)          { this.title = v; }

    public String getContext()              { return context; }
    public void setContext(String v)        { this.context = v; }

    public String getChosenOption()         { return chosenOption; }
    public void setChosenOption(String v)   { this.chosenOption = v; }

    public String getRationale()            { return rationale; }
    public void setRationale(String v)      { this.rationale = v; }

    public String getStatus()               { return status; }
    public void setStatus(String v)         { this.status = v; }

    public String getDecidedBy()            { return decidedBy; }
    public void setDecidedBy(String v)      { this.decidedBy = v; }

    public LocalDateTime getDecidedAt()     { return decidedAt; }
    public void setDecidedAt(LocalDateTime v) { this.decidedAt = v; }

    public LocalDateTime getCreatedAt()     { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public String getReplacedBy()           { return replacedBy; }
    public void setReplacedBy(String v)     { this.replacedBy = v; }

    public List<Tag> getTags()              { return tags; }
    public void setTags(List<Tag> v)        { this.tags = v; }

    /** "May 14": when it was decided, or when it was proposed if it hasn't been yet. */
    public String getShortDate() {
        LocalDateTime when = decidedAt != null ? decidedAt : createdAt;
        return when == null ? "" : when.format(SHORT_DATE);
    }
}
