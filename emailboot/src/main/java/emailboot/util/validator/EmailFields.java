package emailboot.util.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailFields {

    public final static String MAILING_LIST = "mailingList";
    public final static String MALFORMED_ADDRESS_TYPE_KEY = "malformedAddress";

    public static final int SUBJECT_LENGTH_MIN = 5;
    public static final int SUBJECT_LENGTH_MAX = 100;

    public static boolean isMandatoryFieldsValid(String subject, String content) {
        if (StringFields.isNNullNEmptyMinMaxLength(subject, SUBJECT_LENGTH_MIN, SUBJECT_LENGTH_MAX)
                && StringFields.isNNullNEmpty(content)) {
            return true;
        } else {
            return false;
        }
    }

    public static Map<String, List<String>> validateDirectMailingList(String notificationMailingList, List<String> mailingListOK) {
        List<String> mailingList = new ArrayList<>();
        List<String> malformedAddress = new ArrayList<>();
        if (notificationMailingList.contains(";")) {
            for (String mailAdd : notificationMailingList.split(";")) {
                if (!StringFields.isEmail(mailAdd)) {
                    malformedAddress.add(mailAdd);
                } else {
                    mailingList.add(mailAdd);
                }
            }
        } else {
            if (!StringFields.isEmail(notificationMailingList)) {
                malformedAddress.add(notificationMailingList);
            } else {
                mailingList.add(notificationMailingList);
            }
        }
        Map<String, List<String>> validList = new HashMap<>();
        if (!mailingListOK.isEmpty())
            mailingList.addAll(mailingListOK);

        validList.put(MAILING_LIST, mailingList);
        validList.put(MALFORMED_ADDRESS_TYPE_KEY, malformedAddress);

        return validList;
    }

    public static InternetAddress[] getRecipientsWithFormat(List<String> recipients) {
        try {
            InternetAddress[] emails = new InternetAddress[recipients.size()];
            for (int i = 0; i < recipients.size(); i++) {
                emails[i] = new InternetAddress(recipients.get(i));
            }
            return emails;
        } catch (AddressException ex) {
            Logger.getLogger(EmailFields.class.getName()).log(Level.SEVERE, "Exception parsing email address", ex);
        }
        return null;
    }

}
