package io.pivotal.pal.tracker.users;

public class UserInfo {

    public final long id;
    public final String name;
    public final String info;

    public UserInfo(long id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    private UserInfo() {
        this(0, null, null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (id != userInfo.id) return false;
        if (name != null ? !name.equals(userInfo.name) : userInfo.name != null)
            return false;
        return info != null ? info.equals(userInfo.info) : userInfo.info == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", info='" + info + '\'' +
            '}';
    }
}
