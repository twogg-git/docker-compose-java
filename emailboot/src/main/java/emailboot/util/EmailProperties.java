package emailboot.util;

import java.io.Serializable;

public class EmailProperties implements Serializable {

    private String mailFromAddress;
    private String maxAttachmentsSize;
    private String attachmentsPath;
    private String headerImgPath;
    private String velocityTemplate;
    private String velocityTemplatePath;

    public String getMailFromAddress() {
        return mailFromAddress;
    }

    public void setMailFromAddress(String mailFromAddress) {
        this.mailFromAddress = mailFromAddress;
    }

    public String getMaxAttachmentsSize() {
        return maxAttachmentsSize;
    }

    public void setMaxAttachmentsSize(String maxAttachmentsSize) {
        this.maxAttachmentsSize = maxAttachmentsSize;
    }

    public Long getMaxAttachmentsSizeBytes() {
        return new Long(maxAttachmentsSize) * 1024 * 1024;
    }

    public String getAttachmentsPath() {
        return attachmentsPath;
    }

    public void setAttachmentsPath(String attachmentsPath) {
        this.attachmentsPath = attachmentsPath;
    }

    public String getHeaderImgPath() {
        return headerImgPath;
    }

    public void setHeaderImgPath(String headerImgPath) {
        this.headerImgPath = headerImgPath;
    }

    public String getVelocityTemplate() {
        return velocityTemplate;
    }

    public void setVelocityTemplate(String velocityTemplate) {
        this.velocityTemplate = velocityTemplate;
    }

    public String getVelocityTemplatePath() {
        return velocityTemplatePath;
    }

    public void setVelocityTemplatePath(String velocityTemplatePath) {
        String pathPackage = getClass().getClassLoader().getResource("/").getPath();
        this.velocityTemplatePath = pathPackage + "" + velocityTemplatePath;
    }
}
