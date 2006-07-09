package hjb.http.cmd;

import junit.framework.TestCase;

public class HttpPostGeneratorFactoryTest extends TestCase {
    
    public void testAllInstancesAreTheSame() throws Exception {
        assertEquals(new HttpPostGeneratorFactory(), new HttpPostGeneratorFactory());
        assertEquals(new HttpPostGeneratorFactory().hashCode(), new HttpPostGeneratorFactory().hashCode());
        assertEquals(new HttpPostGeneratorFactory().toString(), new HttpPostGeneratorFactory().toString());
        assertFalse(new HttpPostGeneratorFactory().equals(new Object()));
    }
    
    public void testAtLeastOnceGeneratorIsProvided() throws Exception {
        assertTrue(new HttpPostGeneratorFactory().getGenerators().length > 0);
    }
  
}
