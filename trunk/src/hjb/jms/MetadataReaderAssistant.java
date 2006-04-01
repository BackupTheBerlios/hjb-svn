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
package hjb.jms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionMetaData;
import javax.jms.JMSException;

import org.apache.log4j.Logger;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.IntCodec;
import hjb.msg.codec.OrderedTypedValueCodec;
import hjb.msg.codec.TypedValueCodec;

/**
 * <code>MetadataReaderAssistant</code> is to used translate values that are
 * attributes of the JMS {@link javax.jms.ConnectionMetaData} to encoded HJB
 * form.
 * 
 * @author Tim Emiola
 */
public class MetadataReaderAssistant {

    /**
     * Converts <code>metadata</code> to text suitable for inclusion in a HTTP
     * response.
     * 
     * @param metadata
     * @return <code>metadata</code> as text
     */
    public String asText(ConnectionMetaData metadata) {
        if (null == metadata) return "";
        Map metadataValues = new HashMap();
        String[] headerNames = KNOWN_METADATA_ATTRIBUTES;
        for (int i = 0; i < headerNames.length; i++) {
            metadataValues.put(headerNames[i],
                               getEncodedValueFromMetaData(headerNames[i],
                                                           metadata));
        }
        return new HJBMessageWriter().asText(metadataValues);
    }

    /**
     * Overrides {@link Object#equals(java.lang.Object)} to ensure all
     * <code>MetadataReaderAssistant</code> instances are equivalent.
     */
    public boolean equals(Object o) {
        return o instanceof MetadataReaderAssistant;
    }

    /**
     * Overrides {@link Object#hashCode()} to ensure it is consistent with the
     * implementation of {@link #equals(Object)}.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    protected String getEncodedValueFromMetaData(String hjbKey,
                                                 ConnectionMetaData metadata) {
        return readerFor(hjbKey).read(metadata);
    }

    protected String[] getMetadataAttributes() {
        return MetadataReaderAssistant.KNOWN_METADATA_ATTRIBUTES;
    }

    protected Reader readerFor(String hjbKey) {
        Reader result = (Reader) METADATA_READER_WRITERS.get(hjbKey);
        return (null == result) ? NULL_READER : result;
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getJMSMajorVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jms-major-version".
     */
    public static final String JMS_MAJOR_VERSION = "hjb.core.metadata.jms-major-version";
    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getJMSMinorVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jms-minor-version".
     */
    public static final String JMS_MINOR_VERSION = "hjb.core.metadata.jms-minor-version";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getJMSVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jms-version".
     */
    public static final String JMS_VERSION = "hjb.core.metadata.jms-version";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getJMSProviderName()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jms-provider-name".
     */
    public static final String JMS_PROVIDER_NAME = "hjb.core.metadata.jms-provider-name";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getJMSXPropertyNames()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jmsx-property-names".
     */
    public static final String JMSX_PROPERTY_NAMES = "hjb.core.metadata.jmsx-property-names";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getProviderMajorVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is
     * "hjb.core.metadata.jms-provider-major-version".
     */
    public static final String JMS_PROVIDER_MAJOR_VERSION = "hjb.core.metadata.provider-major-version";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getProviderMinorVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.provider-minor-version".
     */
    public static final String JMS_PROVIDER_MINOR_VERSION = "hjb.core.metadata.provider-minor-version";

    /**
     * Constant that holds the hjbKey that corresponds to the value
     * {@link ConnectionMetaData#getProviderVersion()} in JMS
     * <code>ConnectionMetaData</code>.
     * </p>
     * The value of this constant is "hjb.core.metadata.jms-provider-version".
     */
    public static final String JMS_PROVIDER_VERSION = "hjb.core.metadata.provider-version";

    public static String KNOWN_METADATA_ATTRIBUTES[] = new String[] {
            JMS_MAJOR_VERSION,
            JMS_MINOR_VERSION,
            JMS_VERSION,
            JMSX_PROPERTY_NAMES,
            JMS_PROVIDER_NAME,
            JMS_PROVIDER_MAJOR_VERSION,
            JMS_PROVIDER_MINOR_VERSION,
            JMS_PROVIDER_VERSION,
    };

    protected static Map METADATA_READER_WRITERS = new HashMap();

    /**
     * <code>Reader</code> specifies methods for reading an attribute of a JMS
     * <code>ConnectionMetaData</code>.
     * 
     * @author Tim Emiola
     */
    public static interface Reader {

        /**
         * Reads the encoded attribute value from <code>metadata</code>
         * 
         * @param metadata
         *            a <code>ConnectionMetaData<code> instance
         * @throws HJBException
         *             on problems in reading the attribute value
         */
        public String read(ConnectionMetaData metadata) throws HJBException;

        /**
         * @return the HJB header hjbKey of attribute corresponding to this
         *         instance
         * @throws HJBException
         *             if a problem occurs while determining the hjbKey
         */
        public String getHJBHeaderName() throws HJBException;

    }

    protected static Reader NULL_READER = new Reader() {

        public String read(ConnectionMetaData metadata) throws HJBException {
            return null;
        }

        public String getHJBHeaderName() throws HJBException {
            return null;
        }

    };

    /**
     * <code>BaseReader</code> provides methods that implement behavior used
     * by the <code>Reader</code>s defined in this class.
     * 
     * @author Tim Emiola
     */
    public abstract static class BaseReader implements Reader {

        public BaseReader(String hjbKey) {
            this(hjbKey, new OrderedTypedValueCodec());
        }

        public BaseReader(String hjbKey, TypedValueCodec codec) {
            if (null == hjbKey)
                throw new IllegalArgumentException(strings().needsANonNull("hjbKey"));
            this.codec = codec;
            this.hjbKey = hjbKey;
            if (null == this.codec) this.codec = new OrderedTypedValueCodec();
            this.hjbKey = hjbKey;
            METADATA_READER_WRITERS.put(getHJBHeaderName(), this);
        }

        public String getHJBHeaderName() throws HJBException {
            return hjbKey;
        }

        public boolean equals(Object o) {
            if (!(o instanceof BaseReader)) return false;
            return equals((BaseReader) o);
        }

        public boolean equals(MetadataReaderAssistant o) {
            return this.getClass().getName().equals(o.getClass().getName());
        }

        public int hashCode() {
            return this.getClass().getName().hashCode();
        }

        public String toString() {
            String clazzName = this.getClass().getName();
            if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
            return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1)
                    + "]";
        }

        protected TypedValueCodec getCodec() {
            return codec;
        }

        protected HJBStrings strings() {
            return STRINGS;
        }

        protected String handleJMSException(JMSException e) {
            String errorMessage = strings().getString(HJBStrings.COULD_NOT_ACCESS_CONNECTION_METADATA_ATTRIBUTE,
                                                      getHJBHeaderName());
            LOG.error(errorMessage, e);
            throw new HJBException(errorMessage, e);
        }

        private TypedValueCodec codec;

        private String hjbKey;
        private static final HJBStrings STRINGS = new HJBStrings();

    }

    protected static Reader JMS_MINOR_VERSION_READER = new BaseReader(JMS_MINOR_VERSION, new IntCodec()) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return getCodec().encode(new Integer(metadata.getJMSMinorVersion()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_PROVIDER_NAME_READER = new BaseReader(JMS_PROVIDER_NAME) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return metadata.getJMSProviderName();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_VERSION_READER = new BaseReader(JMS_VERSION) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return metadata.getJMSVersion();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_MAJOR_VERSION_READER = new BaseReader(JMS_MAJOR_VERSION, new IntCodec()) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return getCodec().encode(new Integer(metadata.getJMSMajorVersion()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMSX_PROPERTY_NAMES_READER = new BaseReader(JMSX_PROPERTY_NAMES) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return ""
                        + new ArrayList(Collections.list(metadata.getJMSXPropertyNames()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_PROVIDER_MAJOR_VERSION_READER = new BaseReader(JMS_PROVIDER_MAJOR_VERSION, new IntCodec()) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return getCodec().encode(new Integer(metadata.getProviderMajorVersion()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_PROVIDER_VERSION_READER = new BaseReader(JMS_PROVIDER_VERSION) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return metadata.getProviderVersion();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static Reader JMS_PROVIDER_MINOR_VERSION_READER = new BaseReader(JMS_PROVIDER_MINOR_VERSION, new IntCodec()) {

        public String read(ConnectionMetaData metadata) throws HJBException {
            try {
                return getCodec().encode(new Integer(metadata.getProviderMinorVersion()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    private static final Logger LOG = Logger.getLogger(MetadataReaderAssistant.class);
    private static HJBStrings STRINGS = new HJBStrings();
}
