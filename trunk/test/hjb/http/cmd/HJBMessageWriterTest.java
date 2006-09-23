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
package hjb.http.cmd;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class HJBMessageWriterTest extends TestCase {
    
    public void testEqualsIdentifiesAllInstancesAsTheSame() {
        assertEquals(testMessageWriter, new HJBMessageWriter());
        assertEquals(testMessageWriter.toString(), new HJBMessageWriter().toString());
        assertEquals(testMessageWriter.hashCode(), new HJBMessageWriter().hashCode());
    }
    
    public void testEqualsDistinguishesInstancesFromOtherObjects() {
        assertFalse(testMessageWriter.equals(new Object()));
    }
    
    public void testConvertsStringifiedMapCorrectly() throws Exception {
        assertEquals(testMap, testMessageWriter.asMap(testString));        
    }

    public void testConvertsStringifiedMapWithBlankLinesCorrectly() throws Exception {
        String withBlankLines = "foo=bar" + CR + "   \nbaz=(int 1)\n\r\r" + CR + "\n\nbar=(long 5)\n\r\t\r\t\n";
        assertEquals(testMap, testMessageWriter.asMap(withBlankLines));        
    }
    
    public void testAnyMapCanBeStringified() throws Exception {
        assertEquals(testString, testMessageWriter.asText(testMap));
    }
    
    protected void setUp() {
        testMessageWriter = new HJBMessageWriter();
        testString = "foo=bar" + CR + "baz=(int 1)" + CR + "bar=(long 5)";
        testMap = new HashMap();
        testMap.put("foo", "bar");
        testMap.put("baz", "(int 1)");
        testMap.put("bar", "(long 5)");
    }
    
    private String CR = System.getProperty("line.separator");
    private String testString;
    private HJBMessageWriter testMessageWriter;
    private Map testMap;
}
