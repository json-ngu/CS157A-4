package mindmerge.model;

/** One of the options a decision weighs; is_chosen marks the option the author selected. */
public class DecisionOption {

    private int     optionNum;
    private String  label;
    private String  description;  // may be null
    private boolean chosen;

    public int getOptionNum()               { return optionNum; }
    public void setOptionNum(int v)         { this.optionNum = v; }

    public String getLabel()                { return label; }
    public void setLabel(String v)          { this.label = v; }

    public String getDescription()          { return description; }
    public void setDescription(String v)    { this.description = v; }

    public boolean isChosen()               { return chosen; }
    public void setChosen(boolean v)        { this.chosen = v; }
}
