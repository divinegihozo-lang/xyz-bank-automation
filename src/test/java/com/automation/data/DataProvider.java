package com.automation.data;

import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

public final class DataProvider {

    private DataProvider() {
    }
    public static Stream<Arguments> invalidCustomerData() {
        return Stream.of(
                Arguments.of("TC_BM_003: Numeric First Name", "12345", "Doe", "12345"),
                Arguments.of("TC_BM_004: Alpha Postal Code", "John", "Doe", "ABCDE"),
                Arguments.of("TC_BM_005: Empty Fields", "", "", ""));
    }

    public static Stream<String> validDepositAmounts() {
        return Stream.of("100", "500", "1000");
    }

    public static Stream<String> invalidDepositAmounts() {
        return Stream.of("0", "-50");
    }

    public static Stream<Arguments> invalidWithdrawalAmounts() {
        return Stream.of(
                Arguments.of("100000", "Exceeding Balance"),
                Arguments.of("0", "Zero Amount"));
    }
}
