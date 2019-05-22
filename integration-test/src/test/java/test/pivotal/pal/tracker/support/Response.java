package test.pivotal.pal.tracker.support;

public class Response {
    public final int status;
    public final String body;

    public Response(int status, String body) {
        this.status = status;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", body='" + body + '\'' +
                '}';
    }
}
