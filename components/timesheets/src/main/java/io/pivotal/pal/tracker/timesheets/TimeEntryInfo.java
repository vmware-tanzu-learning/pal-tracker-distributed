package io.pivotal.pal.tracker.timesheets;

public class TimeEntryInfo {
    public final long id;
    public final long projectId;
    public final long userId;
    public final String date;
    public final int hours;
    public final String info;

    public TimeEntryInfo() { // for jackson
        this(timeEntryInfoBuilder());
    }

    private TimeEntryInfo(Builder builder) {
        id = builder.id;
        projectId = builder.projectId;
        userId = builder.userId;
        date = builder.date;
        hours = builder.hours;
        info = builder.info;
    }

    public static Builder timeEntryInfoBuilder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private long projectId;
        private long userId;
        private String date;
        private int hours;
        private String info;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder projectId(long projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder hours(int hours) {
            this.hours = hours;
            return this;
        }

        public Builder info(String info) {
            this.info = info;
            return this;
        }

        public TimeEntryInfo build() {
            return new TimeEntryInfo(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntryInfo that = (TimeEntryInfo) o;

        if (id != that.id) return false;
        if (projectId != that.projectId) return false;
        if (userId != that.userId) return false;
        if (hours != that.hours) return false;
        if (date != null ? !date.equals(that.date) : that.date != null)
            return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + hours;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimeEntryInfo{" +
            "id=" + id +
            ", projectId=" + projectId +
            ", userId=" + userId +
            ", date=" + date +
            ", hours=" + hours +
            ", info='" + info + '\'' +
            '}';
    }
}
