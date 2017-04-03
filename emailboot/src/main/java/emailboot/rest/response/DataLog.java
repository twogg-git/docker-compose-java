package emailboot.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class DataLog {

    private long id;
    private String from;
    private String mailsList;
    private String subject;
    private String content;
    private List<String> warnings;
    private List<String> malformedDirectEmails;

    public DataLog(long id, String from) {
        this.from = from;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMailsList() {
        return mailsList;
    }

    public void setMailsList(String mailsList) {
        this.mailsList = mailsList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void addWarning(String warning) {
        if(this.warnings == null){
            this.warnings = new ArrayList<>();
            this.warnings.add(warning);
        } else{
            this.warnings.add(warning);
        }
    }

    public List<String> getMalformedDirectEmails() {
        return malformedDirectEmails;
    }

    public void setMalformedDirectEmails(List<String> malformedDirectEmails) {
        this.malformedDirectEmails = malformedDirectEmails;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", mailsList='" + mailsList + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", warnings=" + warnings +
                ", malformedDirectEmails=" + malformedDirectEmails +
                '}';
    }
}
