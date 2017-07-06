package io.pivotal.pal.tracker.projects;

public class ProjectInfo {

    public final long id;
    public final long accountId;
    public final String name;
    public final boolean active;
    public final String info;

    private ProjectInfo() {
        this(projectInfoBuilder());
    }

    public ProjectInfo(Builder builder) {
        id = builder.id;
        accountId = builder.accountId;
        name = builder.name;
        active = builder.active;
        info = builder.info;
    }

    public static Builder projectInfoBuilder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private long accountId;
        private String name;
        private boolean active;
        private String info;

        public ProjectInfo build() {
            return new ProjectInfo(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder accountId(long accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectInfo that = (ProjectInfo) o;

        if (id != that.id) return false;
        if (accountId != that.accountId) return false;
        if (active != that.active) return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
            "id=" + id +
            ", accountId=" + accountId +
            ", name='" + name + '\'' +
            ", active=" + active +
            ", info='" + info + '\'' +
            '}';
    }
}
