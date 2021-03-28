package com.kevinkirwansoftware.capsule;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;

public class SingleReminderTest {
    private SingleReminder singleReminderCal;
    private SingleReminder singleReminderInts;

    private Calendar calCalendar;
    private Calendar intCalendar;

    @Before
    public void setUp(){
        calCalendar = Calendar.getInstance();
        calCalendar.set(Calendar.YEAR, 2022);
        calCalendar.set(Calendar.MONTH, 4);
        calCalendar.set(Calendar.DAY_OF_MONTH, 16);
        calCalendar.set(Calendar.HOUR_OF_DAY, 8);
        calCalendar.set(Calendar.MINUTE, 32);

        // Constructor for calendar input
        singleReminderCal = new SingleReminder(calCalendar.getTime());

        // Constructor for int input (when loading from database)
        singleReminderInts = new SingleReminder(2022,
                5,
                16,
                8,
                33);
    }


    @Test
    public void getMinuteTest() {
        assertEquals(32, singleReminderCal.getMinute());
        assertEquals(32, singleReminderInts.getMinute());
    }

    @Test
    public void getHourTest() {
        assertEquals(8, singleReminderCal.getHour());
        assertEquals(8, singleReminderInts.getHour());
    }

    @Test
    public void getDayTest() {
        assertEquals(16, singleReminderCal.getDay());
        assertEquals(16, singleReminderInts.getDay());
    }

    @Test
    public void getMonthTest() {
        assertEquals(4, singleReminderCal.getMonth());
        assertEquals(4, singleReminderInts.getMonth());
    }

    @Test
    public void getYearTest() {
        assertEquals(2022, singleReminderCal.getYear());
        assertEquals(2022, singleReminderInts.getYear());
    }

    /* Many of the fields in Calendar are 'don't cares' for this purpose
    @Test
    public void getReminderCalendarTest() {

    }
     */

    @Test
    public void getDateSingleAsStringTest() {

    }

    @Test
    public void getTimeSingleAsStringTest() {
    }

    @Test
    public void getTypeStringTest() {
    }
}