package emailboot.util.exception;


public enum BusinessExceptionEnum {

    EXCEPTION_SERVER_ERROR("Server error, please contact technical support.");

    private String message;

    BusinessExceptionEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}