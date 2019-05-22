package io.pivotal.pal.tracker.accounts;

public class AccountInfo {

    public final long id;
    public final long ownerId;
    public final String name;
    public final String info;

    private AccountInfo() { // for jackson
        this(accountInfoBuilder());
    }

    private AccountInfo(Builder builder) {
        id = builder.id;
        ownerId = builder.ownerId;
        name = builder.name;
        info = builder.info;
    }

    public static Builder accountInfoBuilder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private long ownerId;
        private String name;
        private String info;

        public AccountInfo build() {
            return new AccountInfo(this);
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder ownerId(long ownerId) {
            this.ownerId = ownerId;
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

        AccountInfo that = (AccountInfo) o;

        if (id != that.id) return false;
        if (ownerId != that.ownerId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        return info != null ? info.equals(that.info) : that.info == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (ownerId ^ (ownerId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
            "id=" + id +
            ", ownerId=" + ownerId +
            ", name='" + name + '\'' +
            ", info='" + info + '\'' +
            '}';
    }
}
