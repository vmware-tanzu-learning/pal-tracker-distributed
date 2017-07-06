package io.pivotal.pal.tracker.allocations;

public class ProjectInfo {

    public final boolean active;

    private ProjectInfo() {
        this(false);
    }

    public ProjectInfo(boolean active) {
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectInfo that = (ProjectInfo) o;

        return active == that.active;
    }

    @Override
    public int hashCode() {
        return (active ? 1 : 0);
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
            "active=" + active +
            '}';
    }
}
