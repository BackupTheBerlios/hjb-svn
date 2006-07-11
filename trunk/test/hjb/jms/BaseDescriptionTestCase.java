package hjb.jms;

import junit.framework.AssertionFailedError;

import org.jmock.MockObjectTestCase;

import hjb.misc.HJBStrings;

public abstract class BaseDescriptionTestCase  extends MockObjectTestCase  {

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
    
    private static final HJBStrings STRINGS = new HJBStrings();
    public static final String CR = System.getProperty("line.separator");
}
