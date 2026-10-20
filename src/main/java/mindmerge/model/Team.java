package mindmerge.model;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private int          teamId;
    private String       name;
    private List<Member> members = new ArrayList<>(); // current members; role is team_role

    public int getTeamId()                 { return teamId; }
    public void setTeamId(int v)           { this.teamId = v; }

    public String getName()                { return name; }
    public void setName(String v)          { this.name = v; }

    public List<Member> getMembers()       { return members; }
    public void setMembers(List<Member> v) { this.members = v; }

    public boolean hasMember(int userId) {
        for (Member m : members) {
            if (m.getUserId() == userId) return true;
        }
        return false;
    }
}
