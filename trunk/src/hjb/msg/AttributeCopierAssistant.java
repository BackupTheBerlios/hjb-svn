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
package hjb.msg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;
import hjb.msg.codec.*;

/**
 * <code>AttributeCopierAssistant</code> is to used convert values that are
 * attributes of the JMS {@link javax.jms.Message} to encoded HJB values and
 * vice versa.
 * 
 * <p />
 * It contains methods principally used by <code>AttributeCopier</code> and
 * <code>NamedPropertyCopier</code>
 * 
 * @see AttributeCopier
 * @see NamedPropertyCopier
 * 
 * <p />
 * @author Tim Emiola
 */
public class AttributeCopierAssistant {

    public boolean isBuiltinHeader(String name) {
        return Arrays.asList(getBuiltinHeaders()).contains(name);
    }

    public void addToMessage(String hjbHeaderName,
                             String encodedValue,
                             Message aMessage) {
        readerWriterFor(hjbHeaderName).write(encodedValue, aMessage);
    }

    public String getEncodedValueFromMessage(String hjbHeaderName,
                                             Message aMessage) {
        return readerWriterFor(hjbHeaderName).read(aMessage);
    }

    public boolean equals(Object o) {
        if (!(o instanceof AttributeCopierAssistant)) return false;
        return equals((AttributeCopierAssistant) o);
    }

    public boolean equals(AttributeCopierAssistant o) {
        return true;
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    protected String[] getBuiltinHeaders() {
        return AttributeCopierAssistant.BUILT_IN_HEADERS;
    }

    protected ReaderWriter readerWriterFor(String hjbHeaderName) {
        ReaderWriter result = (ReaderWriter) ATTRIBUTE_READER_WRITERS.get(hjbHeaderName);
        return (null == result) ? NULL_RW : result;
    }

    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSType()} in a JMS <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.type".
     */
    public static final String TYPE = "hjb.core.jms.type";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSReplyTo()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.replyTo".
     */
    public static final String REPLY_TO = "hjb.core.jms.replyTo";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSRedelivered()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.redelivered".
     */
    public static final String REDELIVERED = "hjb.core.jms.redelivered";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSPriority()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.priority".
     */
    public static final String PRIORITY = "hjb.core.jms.priority";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSMessageID()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.messageId".
     */
    public static final String MESSAGE_ID = "hjb.core.jms.messageId";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSDestination()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.destination".
     */
    public static final String DESTINATION = "hjb.core.jms.destination";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSExpiration()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.expiration".
     */
    public static final String EXPIRATION = "hjb.core.jms.expiration";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSTimestamp()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.timestamp".
     */
    public static final String TIMESTAMP = "hjb.core.jms.timestamp";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSDeliveryMode()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.deliveryMode".
     */
    public static final String DELIVERY_MODE = "hjb.core.jms.deliveryMode";
    /**
     * Constant that holds the hjbHeaderName of the HJB header that corresponds
     * to the value {@link Message#getJMSCorrelationID()} in a JMS
     * <code>Message</code>.
     * </p>
     * The value of this constant is "hjb.core.jms.correlationId".
     */
    public static final String CORRELATION_ID = "hjb.core.jms.correlationId";

    public static String BUILT_IN_HEADERS[] = new String[] {
            CORRELATION_ID,
            DELIVERY_MODE,
            DESTINATION,
            EXPIRATION,
            MESSAGE_ID,
            PRIORITY,
            REDELIVERED,
            REPLY_TO,
            TIMESTAMP,
            TYPE,
    };

    protected static Map ATTRIBUTE_READER_WRITERS = new HashMap();

    /**
     * <code>ReaderWriter</code> specifies methods for reading/updating an
     * attribute value of a JMS <code>Message</code>.
     * 
     * @author Tim Emiola
     */
    public static interface ReaderWriter {

        /**
         * Writes the encoded value to the <code>aMessage</code>
         * 
         * @param encodedValue
         * @param aMessage
         * @throws HJBException
         *             on problems in writing the message
         */
        public void write(String encodedValue, Message aMessage)
                throws HJBException;

        /**
         * Reads the encoded attribute value from <code>aMessage</code>
         * 
         * @param aMessage
         * @throws HJBException
         *             on problems in reading the attribute value
         */
        public String read(Message aMessage) throws HJBException;

        /**
         * @return the HJB header hjbHeaderName of attribute corresponding to
         *         this instance
         * @throws HJBException
         *             if a problem occurs while determining the hjbHeaderName
         */
        public String getHJBHeaderName() throws HJBException;

    }

    /**
     * <code>BaseReaderWriter</code> provides methods that implement behavior
     * used by the <code>ReaderWriter</code>s in the <code>hjb.msg</code>
     * package.
     * 
     * @author Tim Emiola
     */
    public abstract static class BaseReaderWriter implements ReaderWriter {

        public BaseReaderWriter(String hjbHeaderName) {
            this(hjbHeaderName, new OrderedTypedValueCodec());
        }

        public BaseReaderWriter(String hjbHeaderName, TypedValueCodec codec) {
            if (null == hjbHeaderName)
                throw new IllegalArgumentException(strings().needsANonNull("hjbHeaderName"));
            this.codec = codec;
            this.hjbHeaderName = hjbHeaderName;
            if (null == this.codec) this.codec = new OrderedTypedValueCodec();
            this.hjbHeaderName = hjbHeaderName;
            ATTRIBUTE_READER_WRITERS.put(getHJBHeaderName(), this);
        }

        public String getHJBHeaderName() throws HJBException {
            return hjbHeaderName;
        }

        public boolean equals(Object o) {
            if (!(o instanceof BaseReaderWriter)) return false;
            return equals((BaseReaderWriter) o);
        }

        public boolean equals(AttributeCopierAssistant o) {
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

        protected String handleJMSException(JMSException e) {
            String errorMessage = strings().getString(HJBStrings.COULD_NOT_ACCESS_JMS_ATTRIBUTE_FROM_MESSAGE,
                                                      getHJBHeaderName());
            LOG.error(errorMessage, e);
            throw new HJBException(errorMessage, e);
        }

        protected HJBStrings strings() {
            return STRINGS;
        }

        private TypedValueCodec codec;

        private String hjbHeaderName;
        private static final HJBStrings STRINGS = new HJBStrings();
    }

    protected static ReaderWriter NULL_RW = new ReaderWriter() {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
        }

        public String read(Message aMessage) throws HJBException {
            return null;
        }

        public String getHJBHeaderName() throws HJBException {
            return null;
        }

    };

    protected static ReaderWriter DESTINATION_RW = new BaseReaderWriter(DESTINATION) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            String message = strings().getString(HJBStrings.IGNORED_HJB_HTTP_HEADER,
                                                 getHJBHeaderName(),
                                                 "" + encodedValue);
            LOG.warn(message);
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return aMessage.getJMSDestination().toString();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter REPLY_TO_RW = new BaseReaderWriter(REPLY_TO) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            String message = strings().getString(HJBStrings.IGNORED_HJB_HTTP_HEADER,
                                                 getHJBHeaderName(),
                                                 "" + encodedValue);
            LOG.warn(message);
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return aMessage.getJMSReplyTo().toString();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter CORRELATION_ID_RW = new BaseReaderWriter(CORRELATION_ID) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                aMessage.setJMSCorrelationID(encodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return aMessage.getJMSCorrelationID();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter MESSAGE_ID_RW = new BaseReaderWriter(MESSAGE_ID) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                aMessage.setJMSMessageID(encodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return aMessage.getJMSMessageID();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter TYPE_RW = new BaseReaderWriter(TYPE) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                aMessage.setJMSType(encodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return aMessage.getJMSType();
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter DELIVERY_MODE_RW = new BaseReaderWriter(DELIVERY_MODE, new IntCodec()) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                int decodedValue = ((Integer) getCodec().decode(encodedValue)).intValue();
                aMessage.setJMSDeliveryMode(decodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return getCodec().encode(new Integer(aMessage.getJMSDeliveryMode()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter PRIORITY_RW = new BaseReaderWriter(PRIORITY, new IntCodec()) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                int decodedValue = ((Integer) getCodec().decode(encodedValue)).intValue();
                aMessage.setJMSPriority(decodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return getCodec().encode(new Integer(aMessage.getJMSPriority()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter EXPIRATION_RW = new BaseReaderWriter(EXPIRATION, new LongCodec()) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                long decodedValue = ((Long) getCodec().decode(encodedValue)).longValue();
                aMessage.setJMSExpiration(decodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return getCodec().encode(new Long(aMessage.getJMSExpiration()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter REDELIVERED_RW = new BaseReaderWriter(REDELIVERED, new BooleanCodec()) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                boolean decodedValue = ((Boolean) getCodec().decode(encodedValue)).booleanValue();
                aMessage.setJMSRedelivered(decodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return getCodec().encode(new Boolean(aMessage.getJMSRedelivered()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    protected static ReaderWriter TIMESTAMP_RW = new BaseReaderWriter(TIMESTAMP, new LongCodec()) {

        public void write(String encodedValue, Message aMessage)
                throws HJBException {
            try {
                long decodedValue = ((Long) getCodec().decode(encodedValue)).longValue();
                aMessage.setJMSTimestamp(decodedValue);
            } catch (JMSException e) {
                handleJMSException(e);
            }
        }

        public String read(Message aMessage) throws HJBException {
            try {
                return getCodec().encode(new Long(aMessage.getJMSTimestamp()));
            } catch (JMSException e) {
                return handleJMSException(e);
            }
        }
    };

    private static final Logger LOG = Logger.getLogger(AttributeCopierAssistant.class);
}
