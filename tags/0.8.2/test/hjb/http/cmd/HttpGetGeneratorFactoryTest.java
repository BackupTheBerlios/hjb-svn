package hjb.http.cmd;

import junit.framework.TestCase;

public class HttpGetGeneratorFactoryTest extends TestCase {
    
    public void testAllInstancesAreTheSame() throws Exception {
        assertEquals(new HttpGetGeneratorFactory(), new HttpGetGeneratorFactory());
        assertEquals(new HttpGetGeneratorFactory().hashCode(), new HttpGetGeneratorFactory().hashCode());
        assertEquals(new HttpGetGeneratorFactory().toString(), new HttpGetGeneratorFactory().toString());
        assertFalse(new HttpGetGeneratorFactory().equals(new Object()));
    }
    
    public void testAtLeastOnceGeneratorIsProvided() throws Exception {
        assertTrue(new HttpGetGeneratorFactory().getGenerators().length > 0);
    }
  
}
