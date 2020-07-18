package kiz.learnwithvel.notes.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

import static kiz.learnwithvel.notes.util.DateUtil.GET_MONTH_ERROR;
import static kiz.learnwithvel.notes.util.DateUtil.getCurrentTimeStamp;
import static kiz.learnwithvel.notes.util.DateUtil.getMonthFromNumber;
import static kiz.learnwithvel.notes.util.DateUtil.monthNumbers;
import static kiz.learnwithvel.notes.util.DateUtil.months;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTest {

    public static final String DATE_TODAY = "07-2020";

    @Test
    void testGetCurrentTimeStamp() throws Exception {
        assertDoesNotThrow(() -> {
            assertEquals(DATE_TODAY, getCurrentTimeStamp());
            System.out.println("Timestamp is generated correctly");
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void getMonthFromNumber_returnSuccess(int monthNumber) {
        assertEquals(months[monthNumber], getMonthFromNumber(monthNumbers[monthNumber]));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    public void testGetMonthFromNumber_returnError(int monthNumber) {
        int randomInt = new Random().nextInt(90) + 13;
        assertEquals(getMonthFromNumber(String.valueOf(monthNumber * randomInt)), GET_MONTH_ERROR);
    }
}
