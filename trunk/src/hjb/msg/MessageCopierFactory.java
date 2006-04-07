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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>MessageCopierFactory</code> determines the <code>MessageCopier</code>
 * to be used to copy values between specific instances of
 * {@link javax.jms.Message}s and {@link hjb.msg.HJBMessage}s.
 * 
 * @author Tim Emiola
 */
public class MessageCopierFactory {

    public MessageCopierFactory() {
        initialiseFactory();
    }

    public MessageCopier getCopierFor(Message jmsMessage) throws HJBException {
        return getCopierFor(distinctJmsMessageInterfaceOf(jmsMessage.getClass().getName()));
    }

    public MessageCopier getCopierFor(HJBMessage hjbMessage)
            throws HJBException {
        try {
            return getCopierFor(hjbMessage.getHeader(HJB_JMS_CLASS));
        } catch (HJBException e) {
            LOG.error("Message headers");
            LOG.error("" + new HashMap(hjbMessage.getHeaders()));
            LOG.error("Message body:");
            LOG.error("" + hjbMessage.getBody());
            throw e;
        }
    }

    public Map getMessageCopierNames() {
        if (null == messageConverterNames) return null;
        return new HashMap(messageConverterNames);
    }

    protected MessageCopier getCopierFor(String jmsClazz) {
        if (null == jmsClazz) {
            String message = strings().getString(HJBStrings.NO_JMS_MESSAGE_CLASS_SPECIFIED);
            LOG.error(message);
            throw new HJBException(message);
        }
        String converterClazz = (String) getMessageCopierNames().get(jmsClazz);
        if (null == converterClazz) {
            String message = strings().getString(HJBStrings.COULD_NOT_FIND_MESSAGE_COPIER,
                                                 jmsClazz);
            LOG.error(message);
            throw new HJBException(message);
        }
        MessageCopier result = createCopierFor(jmsClazz, converterClazz);
        if (null != result) return result;
        String message = strings().getString(HJBStrings.COULD_NOT_FIND_MESSAGE_COPIER,
                                             jmsClazz);
        LOG.error(message);
        throw new HJBException(message);
    }

    protected void initialiseFactory() {
        synchronized (MessageCopierFactory.class) {
            if (null != getMessageCopierNames()) return;
            Properties p = new Properties();
            try {
                InputStream is = MessageCopierFactory.class.getResourceAsStream('/' + MESSAGE_COPIER_FILE);
                if (null == is) {
                    LOG.warn(strings().getString(HJBStrings.NO_DEPLOYED_MESSAGE_COPIERS));
                    is = MessageCopierFactory.class.getResourceAsStream(MESSAGE_COPIER_FILE);
                }
                is = MessageCopierFactory.class.getResourceAsStream(MESSAGE_COPIER_FILE);
                if (null != is) {
                    p.load(is);
                    LOG.info(strings().getString(HJBStrings.LOADED_MESSAGE_COPIERS));
                } else {
                    LOG.error(strings().getString(HJBStrings.MESSAGE_COPIERS_NOT_FOUND));
                }
            } catch (IOException e) {
                LOG.error(strings().getString(HJBStrings.NO_DEPLOYED_MESSAGE_COPIERS),
                          e);
            }
            messageConverterNames = p;
            verifyMessageCopiers();
        }
    }

    protected void verifyMessageCopiers() {
        for (Iterator i = getMessageCopierNames().keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String converterClazz = (String) getMessageCopierNames().get(key);
            if (null == createCopierFor(key, converterClazz)) {
                getMessageCopierNames().remove(key);
            }
        }
    }

    public String distinctJmsMessageInterfaceOf(String name) {
        try {
            Class messageClazz = Class.forName(name);
            List areImplemented = new ArrayList();
            Class[] interfaces = messageClazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (getMessageCopierNames().containsKey(interfaces[i].getName())) {
                    areImplemented.add(interfaces[i].getName());
                }
            }
            if (1 == areImplemented.size()) {
                return (String) areImplemented.get(0);
            }
            return handleDistinctInterfaceNotFound(name, areImplemented);
        } catch (ClassNotFoundException e) {
            String message = strings().getString(HJBStrings.COULD_NOT_CREATE_MESSAGE_COPIER,
                                                 name);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected String handleDistinctInterfaceNotFound(String name,
                                                   List areImplemented) {
        if (0 == areImplemented.size()) {
            String message = strings().getString(HJBStrings.IS_NOT_A_JMS_MESSAGE,
                                                 name);
            LOG.error(message);
            throw new HJBException(message);
        } else {
            String message = strings().getString(HJBStrings.MANY_JMS_MESSAGE_INTERFACES,
                                                 name,
                                                 areImplemented);
            LOG.error(message);
            throw new HJBException(message);
        }
    }

    protected MessageCopier createCopierFor(String jmsClazz,
                                            String converterClazz) {
        try {
            return (MessageCopier) Class.forName(converterClazz).newInstance();
        } catch (Exception e) {
            String message = strings().getString(HJBStrings.COULD_NOT_CREATE_MESSAGE_COPIER,
                                                 jmsClazz);
            LOG.error(message, e);
            throw new HJBException(message, e);
        }
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    /**
     * Constant that holds the name of the property file for listing the
     * MessageCopier mappings. <p/> The value of this constant is
     * "message-converters.properties".
     */
    public static final String MESSAGE_COPIER_FILE = "message-copiers.properties";

    /**
     * Constant that holds the name of the header that specifies what JMS class
     * is contained in a HTTP message.
     * 
     * <p/> The value of this constant is "hjb.core.jms-message-class".
     */
    public static final String HJB_JMS_CLASS = "hjb.core.jms-message-class";
    
    /**
     * Constant that holds the name of message parameter used to indicate
     * version of a HJB message
     * <p />
     * The value of this constant is "hjb.core.message-version".
     */
    public static final String HJB_MESSAGE_VERSION = "hjb.core.message-version";
    
    /**
     * Constant that holds the value of HJB 1.0 message version 
     * <p />
     * The value of this constant is "1.0".
     */
    public static final String HJB_MESSAGE_VERSION_1_0 = "1.0";
    

    private static Map messageConverterNames;
    private static final Logger LOG = Logger.getLogger(MessageCopierFactory.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
