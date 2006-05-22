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
        testString = "foo=bar" + CR + "baz=(int 1)" + CR + "bar=(long 5)" + CR;
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
