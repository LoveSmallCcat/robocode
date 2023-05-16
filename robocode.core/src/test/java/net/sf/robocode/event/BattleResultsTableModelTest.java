package net.sf.robocode.event;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class BattleResultsTableModelTest {
    @Test
    public void testGetPlacementString() {
        assertEquals("1st", getPlacementString(1));
        assertEquals("2nd", getPlacementString(2));
        assertEquals("3rd", getPlacementString(3));
        assertEquals("4th", getPlacementString(4));
        assertEquals("11th", getPlacementString(11));
        assertEquals("12th", getPlacementString(12));
        assertEquals("13th", getPlacementString(13));
        assertEquals("21st", getPlacementString(21));
        assertEquals("22nd", getPlacementString(22));
        assertEquals("23rd", getPlacementString(23));
        assertEquals("111st", getPlacementString(111));
        assertEquals("112nd", getPlacementString(112));
        assertEquals("113rd", getPlacementString(113));
    }

    private String getPlacementString(int i) {
        String result = "" + i;

        if (i > 3 && i < 20) {
            result += "th";
        } else if (i % 10 == 1) {
            result += "st";
        } else if (i % 10 == 2) {
            result += "nd";
        } else if (i % 10 == 3) {
            result += "rd";
        } else {
            result += "th";
        }
        return result;
    }

}
