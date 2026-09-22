package mindmerge.model;

import java.time.LocalDate;

public class Decision {

    private int       decisionId;
    private String    title;
    private String    chosenOption;
    private String    rationale;
    private String    decidedBy;
    private LocalDate decidedAt;

    public int getDecisionId()          { return decisionId; }
    public void setDecisionId(int v)    { this.decisionId = v; }

    public String getTitle()            { return title; }
    public void setTitle(String v)      { this.title = v; }

    public String getChosenOption()         { return chosenOption; }
    public void setChosenOption(String v)   { this.chosenOption = v; }

    public String getRationale()        { return rationale; }
    public void setRationale(String v)  { this.rationale = v; }

    public String getDecidedBy()        { return decidedBy; }
    public void setDecidedBy(String v)  { this.decidedBy = v; }

    public LocalDate getDecidedAt()         { return decidedAt; }
    public void setDecidedAt(LocalDate v)   { this.decidedAt = v; }
}