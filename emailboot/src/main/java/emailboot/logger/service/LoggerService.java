package emailboot.logger.service;

import emailboot.logger.entities.EmailLogger;
import emailboot.util.exception.BusinessException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoggerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public EmailLogger addEmailLog(String serial, String subject, boolean isDelivered, String message) throws BusinessException {
        EmailLogger emailLogger = new EmailLogger();
        emailLogger.setSerial(serial);
        emailLogger.setSubject(subject);
        emailLogger.setDelivered(isDelivered);
        emailLogger.setContent(message);
        entityManager.persist(emailLogger);
        return emailLogger;
    }

    public List<EmailLogger> getEmailBySerial(String serial) {
        Query query = entityManager.createNamedQuery("logger.find_by_serial");
        query.setParameter("serial", serial);
        return query.getResultList();
    }

    public List<EmailLogger> getEmailBySubject(String subject) {
        Query query = entityManager.createNamedQuery("logger.find_by_subject");
        query.setParameter("subject", "%"+subject+"%");
        return query.getResultList();
    }

    public List<EmailLogger> getListByDates(Date startDate, Date endFate) {
        Query query = entityManager.createNamedQuery("logger.list_all_by_dates");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endFate);
        return query.getResultList();
    }

    public List<EmailLogger> getDeliveredByDates(Date startDate, Date endFate) {
        Query query = entityManager.createNamedQuery("logger.list_delivered_by_dates");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endFate);
        return query.getResultList();
    }

}
