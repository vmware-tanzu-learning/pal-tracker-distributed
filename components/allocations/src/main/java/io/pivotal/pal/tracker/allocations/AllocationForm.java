package io.pivotal.pal.tracker.allocations;

public class AllocationForm {

    public final long projectId;
    public final long userId;
    public final String firstDay;
    public final String lastDay;

    private AllocationForm() { // for jackson
        this(allocationFormBuilder());
    }

    public AllocationForm(Builder builder) {
        projectId = builder.projectId;
        userId = builder.userId;
        firstDay = builder.firstDay;
        lastDay = builder.lastDay;
    }

    public static Builder allocationFormBuilder() {
        return new Builder();
    }


    public static class Builder {
        private long projectId;
        private long userId;
        private String firstDay;
        private String lastDay;

        public AllocationForm build() {
            return new AllocationForm(this);
        }

        public Builder projectId(long projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder firstDay(String firstDay) {
            this.firstDay = firstDay;
            return this;
        }

        public Builder lastDay(String lastDay) {
            this.lastDay = lastDay;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllocationForm that = (AllocationForm) o;

        if (projectId != that.projectId) return false;
        if (userId != that.userId) return false;
        if (firstDay != null ? !firstDay.equals(that.firstDay) : that.firstDay != null)
            return false;
        return lastDay != null ? lastDay.equals(that.lastDay) : that.lastDay == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (firstDay != null ? firstDay.hashCode() : 0);
        result = 31 * result + (lastDay != null ? lastDay.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AllocationForm{" +
            "projectId=" + projectId +
            ", userId=" + userId +
            ", firstDay='" + firstDay + '\'' +
            ", lastDay='" + lastDay + '\'' +
            '}';
    }
}
