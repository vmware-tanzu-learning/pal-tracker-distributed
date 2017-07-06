package io.pivotal.pal.tracker.backlog;

public class StoryInfo {

    public final long id;
    public final long projectId;
    public final String name;
    public final String info;

    private StoryInfo() {
        this(storyInfoBuilder());
    }

    private StoryInfo(Builder builder) {
        id = builder.id;
        projectId = builder.projectId;
        name = builder.name;
        info = builder.info;
    }

    public static Builder storyInfoBuilder() {
        return new Builder();
    }


    public static class Builder {
        private long id;
        private long projectId;
        private String name;
        private String info;

        public StoryInfo build() {
            return new StoryInfo(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder projectId(long projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
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

        StoryInfo storyInfo = (StoryInfo) o;

        if (id != storyInfo.id) return false;
        if (projectId != storyInfo.projectId) return false;
        if (name != null ? !name.equals(storyInfo.name) : storyInfo.name != null)
            return false;
        return info != null ? info.equals(storyInfo.info) : storyInfo.info == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (projectId ^ (projectId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StoryInfo{" +
            "id=" + id +
            ", projectId=" + projectId +
            ", name='" + name + '\'' +
            ", info='" + info + '\'' +
            '}';
    }
}
