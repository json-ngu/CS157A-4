package mindmerge.model;

public class Company {

    private int    companyId;
    private String name;
    private String role; // the viewer's company_role here

    public int getCompanyId()            { return companyId; }
    public void setCompanyId(int v)      { this.companyId = v; }

    public String getName()              { return name; }
    public void setName(String v)        { this.name = v; }

    public String getRole()              { return role; }
    public void setRole(String v)        { this.role = v; }

    public boolean isOwner()             { return "owner".equals(role); }
}
