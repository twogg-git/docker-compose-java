package emailboot.rest;

import emailboot.logger.service.LoggerService;
import emailboot.rest.response.BuildResponse;
import emailboot.rest.response.DataLog;
import emailboot.rest.response.StatusEnum;
import emailboot.util.EmailProperties;
import emailboot.util.SMTPProperties;
import emailboot.util.validator.EmailFields;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class RestMail {

    private static final Logger logger = Logger.getLogger(RestMail.class.getName());

    @Autowired
    private SMTPProperties smtpProperties;

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private LoggerService loggerService;

    @ResponseBody
    @RequestMapping(value = "/emails", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> postEmail(
            @RequestParam("recipients") String recipients,
            @RequestParam("content") String content,
            @RequestParam("subject") String subject,
            HttpServletRequest request) throws Exception {

        DataLog response = new DataLog(new Date().getTime(), request.getRemoteAddr());
        List<String> mailingList = new ArrayList<>();

        try {

            //Validate mandatory fields
            if (!EmailFields.isMandatoryFieldsValid(subject, content)) {
                logger.log(Level.INFO, StatusEnum.INVALID_PARAMETERS.getStatus() + response.toString());
                return BuildResponse.status(HttpStatus.UNPROCESSABLE_ENTITY, BuildResponse.buildURL(request), StatusEnum.INVALID_PARAMETERS.getStatus(), response);
            }
            response.setSubject(subject);

            //Validate Direct Mailing List
            if (recipients != null && !(recipients.isEmpty())) {
                response.setMailsList(recipients);
                Map<String, List<String>> validMap = EmailFields.validateDirectMailingList(recipients, mailingList);
                mailingList = validMap.get(EmailFields.MAILING_LIST);
                if (!validMap.get(EmailFields.MALFORMED_ADDRESS_TYPE_KEY).isEmpty()) {
                    response.addWarning(StatusEnum.MAILING_LIST_DIRECT_WARNING.getStatus());
                    response.setMalformedDirectEmails(validMap.get(EmailFields.MALFORMED_ADDRESS_TYPE_KEY));
                }
            }

            //Validate errors on consolidated mailing List
            if (mailingList.isEmpty()) {
                logger.log(Level.INFO, StatusEnum.EMPTY_MAILING_LIST.getStatus() + response.toString());
                return BuildResponse.status(HttpStatus.UNPROCESSABLE_ENTITY, BuildResponse.buildURL(request), StatusEnum.EMPTY_MAILING_LIST.getStatus(), response);
            }
            response.setMailsList(recipients);

            // Validate java scripts on content
            if (content.contains("<script")) {
                logger.log(Level.INFO, StatusEnum.CONTENT_FORMAT_INVALID.getStatus() + response.toString());
                return BuildResponse.status(HttpStatus.UNPROCESSABLE_ENTITY, BuildResponse.buildURL(request), StatusEnum.CONTENT_FORMAT_INVALID.getStatus(), response);
            }
            response.setContent(content);

            Map<String, Object> mailProperties = new HashMap<>();
            mailProperties.put("content", content);
            mailProperties.put("header", emailProperties.getHeaderImgPath());

            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute");
            velocityEngine.setProperty("runtime.log.logsystem.log4j.logger","com.mindtree.igg.website.email.TemplateMergeUtilVelocityImpl");
            velocityEngine.addProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, emailProperties.getVelocityTemplatePath());
            velocityEngine.init();

            Properties props = new Properties();
            props.put("mail.transport.protocol", smtpProperties.getProtocol());
            props.put("mail.smtp.auth", smtpProperties.getSmtpAuth());
            props.put("mail.smtp.starttls.enable", smtpProperties.getEnableTls());
            props.put("mail.smtp.host", smtpProperties.getHost());
            props.put("mail.smtp.port", smtpProperties.getPort());
            javax.mail.Session session = session = javax.mail.Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(smtpProperties.getUsername(), smtpProperties.getPassword());
                        }
                    });

            MimeMessage emailDraft = new MimeMessage(session);
            emailDraft.setFrom(new InternetAddress(emailProperties.getMailFromAddress()));
            emailDraft.setRecipients(Message.RecipientType.BCC, EmailFields.getRecipientsWithFormat(mailingList));
            emailDraft.setSubject(subject, "UTF-8");
            emailDraft.setSentDate(new Date());

            emailDraft.setContent(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailProperties.getVelocityTemplate(), "UTF-8", mailProperties), "text/html; charset=utf-8");

            Transport.send(emailDraft);
            loggerService.addEmailLog(response.getSerial(), subject, true, "Email build and send to SMTP");
            logger.log(Level.INFO, StatusEnum.NOTIFICATION_ACCEPTED.getStatus() + response.toString());
            return BuildResponse.status(HttpStatus.ACCEPTED, BuildResponse.buildURL(request), StatusEnum.NOTIFICATION_ACCEPTED.getStatus(), response);


        } catch (Exception ex) {
            loggerService.addEmailLog(response.getSerial(), subject, false, ex.getMessage());
            logger.log(Level.SEVERE, StatusEnum.INTERNAL_SERVER_ERROR.getStatus() + "Email ID [" + response.getSerial() + "] " + ex.getMessage(), ex);
            return BuildResponse.status(HttpStatus.INTERNAL_SERVER_ERROR, BuildResponse.buildURL(request), StatusEnum.INTERNAL_SERVER_ERROR.getStatus(), response);
        }
    }

}
