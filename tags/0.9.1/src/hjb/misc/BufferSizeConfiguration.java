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
