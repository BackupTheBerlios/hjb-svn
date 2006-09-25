/*
 HJB (HTTP JMS Bridge) links the HTTP protocol to the JMS API.
 Copyright (C) 2006 Timothy Emiola

 HJB is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the
 Free Software Foundation; either version 2.1 of the License, or (at
 your option) any later version.

 This library is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 USA

 */
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
    
    protected synchronized Clock defaultTestClock() {
        Calendar c = Calendar.getInstance();
        c.set(2003, 12, 25, 0, 0, 0);
        if (null == XMAS_DAY_2004) {
            XMAS_DAY_2004 = c.getTime();
        }
        return clockAtTime(XMAS_DAY_2004);
    }
    
    private static final HJBStrings STRINGS = new HJBStrings();
    public static final String CR = System.getProperty("line.separator");
    protected static Date XMAS_DAY_2004;
    
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
