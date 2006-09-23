package hjb.http.cmd;

import junit.framework.TestCase;

public class HttpDeleteGeneratorFactoryTest extends TestCase {
    
    public void testAllInstancesAreTheSame() throws Exception {
        assertEquals(new HttpDeleteGeneratorFactory(), new HttpDeleteGeneratorFactory());
        assertEquals(new HttpDeleteGeneratorFactory().hashCode(), new HttpDeleteGeneratorFactory().hashCode());
        assertEquals(new HttpDeleteGeneratorFactory().toString(), new HttpDeleteGeneratorFactory().toString());
        assertFalse(new HttpDeleteGeneratorFactory().equals(new Object()));
    }
    
    public void testAtLeastOnceGeneratorIsProvided() throws Exception {
        assertTrue(new HttpDeleteGeneratorFactory().getGenerators().length > 0);
    }
  
}
