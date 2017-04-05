package emailboot.logger.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import emailboot.util.TimestampSerializer;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.*;

@Entity(name = "EmailLogger")
@Table(name = "email_logger")
@NamedQueries({
        @NamedQuery(name = "logger.list_delivered_by_dates", query = "SELECT t FROM EmailLogger t WHERE delivered = true AND t.versionDate BETWEEN :startDate AND :endDate"),
        @NamedQuery(name = "logger.list_all_by_dates", query = "SELECT t FROM EmailLogger t WHERE t.versionDate BETWEEN :startDate AND :endDate"),
        @NamedQuery(name = "logger.find_by_subject", query = "SELECT t FROM EmailLogger t WHERE t.subject like :subject"),
        @NamedQuery(name = "logger.find_by_serial", query = "SELECT t FROM EmailLogger t WHERE t.serial = :serial"),
})
public class EmailLogger {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false, updatable = false)
    private Integer id;

    @Basic
    @Column(name = "log_serial", nullable = false, unique = true, length = 100)
    private String serial;
    
    @Basic
    @Column(name = "log_subject", nullable = false, unique = true, length = 100)
    private String subject;

    @Basic
    @Column(name = "log_delivered", nullable = false)
    private boolean delivered;

    @Basic
    @Column(name = "log_content", nullable = false, unique = true, length = 2500)
    private String content;

    @Basic
    @Column(name = "log_timestamp", nullable = false)
    @JsonSerialize(using = TimestampSerializer.class)
    private Timestamp versionDate;

    @PrePersist
    protected void onCreate() {
        versionDate = new Timestamp(new Date().getTime());
    }

    @PreUpdate
    protected void onUpdate() {
        versionDate = new Timestamp(new Date().getTime());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(Timestamp versionDate) {
        this.versionDate = versionDate;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
