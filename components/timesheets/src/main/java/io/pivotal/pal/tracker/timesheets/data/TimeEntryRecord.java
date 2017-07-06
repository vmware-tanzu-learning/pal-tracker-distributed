package io.pivotal.pal.tracker.timesheets.data;

import java.time.LocalDate;

public class TimeEntryRecord {

    public final long id;
    public final long projectId;
    public final long userId;
    public final LocalDate date;
    public final int hours;

    private TimeEntryRecord(Builder builder) {
        id = builder.id;
        projectId = builder.projectId;
        userId = builder.userId;
        date = builder.date;
        hours = builder.hours;
    }

    public static Builder timeEntryRecordBuilder() {
        return new Builder();
    }

    public static class Builder {

        private long id;
        private long projectId;
        private long userId;
        private LocalDate date;
        private int hours;

        public TimeEntryRecord build() {
            return new TimeEntryRecord(this);
        }

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

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder hours(int hours) {
            this.hours = hours;
            return this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntryRecord that = (TimeEntryRecord) o;

        if (id != that.id) return false;
        if (projectId != that.projectId) return false;
        if (userId != that.userId) return false;
        if (hours != that.hours) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + hours;
        return result;
    }

    @Override
    public String toString() {
        return "TimeEntryRecord{" +
            "id=" + id +
            ", projectId=" + projectId +
            ", userId=" + userId +
            ", date=" + date +
            ", hours=" + hours +
            '}';
    }
}
