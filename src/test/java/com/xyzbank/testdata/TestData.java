package com.xyzbank.testdata;

public final class TestData {
    private TestData() {
        // Utility class
    }

    // Customer Names
    public static final String CUSTOMER_HARRY = "Harry Potter";
    public static final String CUSTOMER_HERMIONE = "Hermoine Granger";
    public static final String CUSTOMER_RON = "Ron Weasly";

    // New Customer Data
    public static final String NEW_CUSTOMER_FIRST_NAME = "John";
    public static final String NEW_CUSTOMER_LAST_NAME = "Doe";
    public static final String NEW_CUSTOMER_POSTAL_CODE = "12345";

    // Invalid Data
    public static final String INVALID_FIRST_NAME_NUMBERS = "John123";
    public static final String INVALID_POSTAL_CODE_ALPHA = "ABCDE";
    public static final String EMPTY_FIELD = "";

    // Currencies
    public static final String CURRENCY_DOLLAR = "Dollar";
    public static final String CURRENCY_POUND = "Pound";

    // Transaction Amounts
    public static final String DEPOSIT_AMOUNT_VALID = "1000";
    public static final String DEPOSIT_AMOUNT_SMALL = "100";
    public static final String DEPOSIT_AMOUNT_ZERO = "0";
    public static final String DEPOSIT_AMOUNT_NEGATIVE = "-50";
    public static final String DEPOSIT_AMOUNT_LARGE = "5000";

    public static final String WITHDRAWAL_AMOUNT_VALID = "500";
    public static final String WITHDRAWAL_AMOUNT_EXCEEDING = "100000";
    public static final String WITHDRAWAL_AMOUNT_ZERO = "0";
}
