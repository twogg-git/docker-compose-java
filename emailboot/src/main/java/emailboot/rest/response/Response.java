package emailboot.rest.response;


import org.springframework.http.HttpStatus;

public class Response {

    private int code;
    private HttpStatus status;
    private String url;
    private String message;
    private Object data;

    public Response() {

    }

    public Response(HttpStatus status, String url, String message, Object data) {
        this.code = status.value();
        this.status = status;
        this.url = url;
        this.message = message;
        this.data = data;
    }

    public Response(HttpStatus status, String url, String message) {
        this.code = status.value();
        this.status = status;
        this.url = url;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
