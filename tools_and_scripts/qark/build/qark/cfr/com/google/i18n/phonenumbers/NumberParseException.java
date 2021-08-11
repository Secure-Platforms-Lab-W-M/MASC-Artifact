/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers;

public class NumberParseException
extends Exception {
    private ErrorType errorType;
    private String message;

    public NumberParseException(ErrorType errorType, String string2) {
        super(string2);
        this.message = string2;
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error type: ");
        stringBuilder.append((Object)this.errorType);
        stringBuilder.append(". ");
        stringBuilder.append(this.message);
        return stringBuilder.toString();
    }

    public static final class ErrorType
    extends Enum<ErrorType> {
        private static final /* synthetic */ ErrorType[] $VALUES;
        public static final /* enum */ ErrorType INVALID_COUNTRY_CODE;
        public static final /* enum */ ErrorType NOT_A_NUMBER;
        public static final /* enum */ ErrorType TOO_LONG;
        public static final /* enum */ ErrorType TOO_SHORT_AFTER_IDD;
        public static final /* enum */ ErrorType TOO_SHORT_NSN;

        static {
            ErrorType errorType;
            INVALID_COUNTRY_CODE = new ErrorType();
            NOT_A_NUMBER = new ErrorType();
            TOO_SHORT_AFTER_IDD = new ErrorType();
            TOO_SHORT_NSN = new ErrorType();
            TOO_LONG = errorType = new ErrorType();
            $VALUES = new ErrorType[]{INVALID_COUNTRY_CODE, NOT_A_NUMBER, TOO_SHORT_AFTER_IDD, TOO_SHORT_NSN, errorType};
        }

        private ErrorType() {
        }

        public static ErrorType valueOf(String string2) {
            return Enum.valueOf(ErrorType.class, string2);
        }

        public static ErrorType[] values() {
            return (ErrorType[])$VALUES.clone();
        }
    }

}

