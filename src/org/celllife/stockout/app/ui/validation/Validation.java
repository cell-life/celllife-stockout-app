package org.celllife.stockout.app.ui.validation;

import android.widget.EditText;
import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String PHONE_REGEX = "\\d{10}";
    private static final String PIN_REGEX = "\\d{10}";
    private static final String LEAD_REGEX = "\\d{2}";
    private static final String SAFETY_REGEX = "\\d{2}";
    private static final String OPERATING_REGEX = "\\d{1}";

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String PHONE_MSG = "##########";


    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check pin number validation
    public static boolean isPinNumber(EditText editText, boolean required) {
        return isValid(editText, PIN_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check lead time validation
    public static boolean isLeadTime(EditText editText, boolean required) {
        return isValid(editText, LEAD_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check safety time validation
    public static boolean isSafetyTime(EditText editText, boolean required) {
        return isValid(editText, SAFETY_REGEX, PHONE_MSG, required);
    }

    // call this method when you need to check operating days validation
    public static boolean isOperatingDays(EditText editText, boolean required) {
        return isValid(editText, OPERATING_REGEX, PHONE_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
}