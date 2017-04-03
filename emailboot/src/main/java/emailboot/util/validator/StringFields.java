package emailboot.util.validator;

public class StringFields {

    public final static int VERSION_USER_LENGTH_MIN = 3;

    public final static int VERSION_USER_LENGTH_MAX = 100;

    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isNNullNEmpty(String field) {
        if (field != null && !field.isEmpty() && !field.equalsIgnoreCase("null") && !field.equals(" ")) {
            return true;
        }
        return false;
    }

    public static boolean isNNullNEmptyNSpaces(String field) {
        if (field != null && !field.isEmpty() && !field.equalsIgnoreCase("null") && !field.equals(" ") && !field.contains(" ")) {
            return true;
        }
        return false;
    }

    public static boolean isNNullNEmptyMaxLength(String field, int maxLength) {
        if (isNNullNEmpty(field)) {
            if (field.length() <= maxLength) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidVersionUser(String field) {
        if (isNNullNEmptyNSpaces(field)) {
            if (field.length() >= VERSION_USER_LENGTH_MIN && field.length() <= VERSION_USER_LENGTH_MAX) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNNullNEmptyNSpacesMaxLength(String field, int maxLength) {
        if (isNNullNEmptyNSpaces(field)) {
            if (field.length() <= maxLength) {
                return true;
            }
        }
        return false;
    }


    public static boolean isNNullNEmptyNSpacesMinMaxLength(String field, int minLength, int maxLength) {
        if (isNNullNEmptyNSpaces(field)) {
            if (field.length() >= minLength && field.length() <= maxLength) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNNullNEmptyMinMaxLength(String field, int minLength, int maxLength) {
        if (isNNullNEmpty(field)) {
            if (field.length() >= minLength && field.length() <= maxLength) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmail(String field) {
        if (isNNullNEmpty(field)) {
            if (field.matches(EMAIL_PATTERN)) {
                return true;
            }
        }
        return false;
    }
}
