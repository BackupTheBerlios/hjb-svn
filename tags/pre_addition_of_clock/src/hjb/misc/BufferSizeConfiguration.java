package hjb.misc;

/**
 * <code>BufferSizeConfiguration</code> is used by several HJB classes to
 * configure the buffer size used to read bytes into Byte arrays.
 * 
 * @author Tim Emiola
 */
public class BufferSizeConfiguration {

    /**
     * Returns the bufferSize.
     * 
     * It defaults to {@link #DEFAULT_READ_BUFFER_SIZE}, but if
     * {@link #BUFFER_SIZE_PROPERTY_NAME} is set and its value is valid, it uses
     * that.
     * 
     * @return the bufferSize.
     */
    public int getBufferSize() {
        return Integer.getInteger(BUFFER_SIZE_PROPERTY_NAME,
                                  DEFAULT_READ_BUFFER_SIZE).intValue();
    }

    /**
     * System property name that can be used to change the buffer size used
     * throughout HJB wherever bytes are read into a byte array.
     * <p />
     * The value of this constant is 'hjb.runtime.byte-buffer-size'.
     */
    public static final String BUFFER_SIZE_PROPERTY_NAME = "hjb.runtime.byte-buffer-size";

    /**
     * Constant that holds the default buffer size used throughout HJB wherever
     * bytes are read into a byte array.
     * <p />
     * The value of this constant is 8192.
     */
    public static final int DEFAULT_READ_BUFFER_SIZE = 8192;
}
