package mindmerge.model;

public class Tag {

    private static final int COLORS = 6;

    private final String name;

    public Tag(String name) { this.name = name; }

    public String getName() { return name; }

    /** Stable 0-5 palette slot, so a tag always gets the same color everywhere. */
    public int getColor()   { return Math.floorMod(name.hashCode(), COLORS); }
}
