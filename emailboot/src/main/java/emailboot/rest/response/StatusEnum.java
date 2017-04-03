package emailboot.rest.response;

public enum StatusEnum {

    NOTIFICATION_ACCEPTED("Email task was accepted and sent to SMTP"),
    INTERNAL_SERVER_ERROR("Internal Server Error. Please try again later"),
    MAILING_LIST_DIRECT_WARNING("Mailing list contains malformed email(s)"),
    CONTENT_FORMAT_INVALID("Invalid content format"),
    EMPTY_MAILING_LIST("Empty mailing list"),
    INVALID_PARAMETERS("Missing or invalid parameters. Check text size have to be over 5 characters.");

    private String status;

    StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}