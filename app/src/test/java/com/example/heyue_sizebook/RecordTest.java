package com.example.heyue_sizebook;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class RecordTest {
    @Test
    public void testSetGetPersonName() throws Exception {
        Record record = new Record();
        record.setPersonName("hahaha");
        assertTrue(record.getPersonName().equals("hahaha"));
    }
    public void testSetGetDate() throws Exception {
        Record record = new Record();
        Calendar calendar = Calendar.getInstance();
        record.setDate(calendar);
        assertTrue(record.getDate().equals(calendar));
    }
    public void testSetGetNeckSize() throws Exception {
        Record record = new Record();
        record.setNeckSize(33.1);
        assertTrue(record.getDate().equals(33.1));
    }
    public void testSetGetChestSize() throws Exception {
        Record record = new Record();
        record.setChestSize(22.8);
        assertTrue(record.getDate().equals(22.8));
    }

}
