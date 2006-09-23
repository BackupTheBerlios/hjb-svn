package hjb.testsupport;

import java.util.Calendar;
import java.util.Date;

import junit.framework.AssertionFailedError;

import org.jmock.MockObjectTestCase;

import hjb.misc.Clock;
import hjb.misc.HJBStrings;

public abstract class BaseHJBTestCase  extends MockObjectTestCase  {

    protected void assertContains(String outer, String inner, String message)  {
        if (-1 == outer.lastIndexOf(inner)) {
            throw new AssertionFailedError(message);
        }
    }
      
    protected void assertDoesNotContain(String outer, String inner, String message)  {
        if (-1 != outer.lastIndexOf(inner)) {
            throw new AssertionFailedError(message);
        }
    }

    protected void assertDoesNotContain(String outer, String inner)  {
        assertDoesNotContain(outer, inner, "");
    }      

    protected void assertContains(String outer, String inner)  {
        assertContains(outer, inner, "");
    }
      
    protected HJBStrings strings() {
        return STRINGS;
    }
    
    protected Clock clockAtTime(Date aTime) {
        return new DummyClock(aTime);
    }
    
    protected Clock defaultTestClock() {
        Calendar c = Calendar.getInstance();
        c.set(2003, 12, 25, 0, 0, 0);
        Date christmas_day_2003 = c.getTime();
        return clockAtTime(christmas_day_2003);
    }
    
    private static final HJBStrings STRINGS = new HJBStrings();
    public static final String CR = System.getProperty("line.separator");
    
    protected static class DummyClock extends Clock {
        private Date currentTime;

        public DummyClock(Date currentTime) {
            this.currentTime = currentTime;
        }

        public Date getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(Date currentTime) {
            this.currentTime = currentTime;
        }
    }
}
