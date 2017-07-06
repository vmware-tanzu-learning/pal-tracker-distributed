package io.pivotal.pal.tracker.projects;

public class ProjectForm {

    public final long accountId;
    public final String name;
    public final boolean active;

    private ProjectForm() {
        this(projectFormBuilder());
    }

    public ProjectForm(Builder builder) {
        accountId = builder.accountId;
        name = builder.name;
        active = builder.active;
    }

    public static Builder projectFormBuilder() {
        return new Builder();
    }

    public static class Builder {
        private long accountId;
        private String name;
        private boolean active;

        public ProjectForm build() {
            return new ProjectForm(this);
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectForm that = (ProjectForm) o;

        if (accountId != that.accountId) return false;
        if (active != that.active) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectForm{" +
            "accountId=" + accountId +
            ", name='" + name + '\'' +
            ", active=" + active +
            '}';
    }
}
