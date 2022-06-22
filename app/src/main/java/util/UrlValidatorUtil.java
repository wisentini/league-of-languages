package util;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlValidatorUtil {
    public static boolean validate(String url) {
        UrlValidator urlValidator = new UrlValidator();
        boolean isValid = urlValidator.isValid(url);
        return isValid;
    }
}
