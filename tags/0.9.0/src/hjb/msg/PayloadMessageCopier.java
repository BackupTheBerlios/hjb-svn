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

import javax.jms.Message;

import org.apache.log4j.Logger;

import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

/**
 * <code>PayloadMessageCopier</code> is a base class of all the
 * <code>xxxMessageCopiers</code> in the <code>hjb.msg</code> package.
 * 
 * <p>
 * It is provides two abtract methods {@link #acceptHJBMessage(HJBMessage)} and
 * {@link #acceptJMSMessage(Message)} which the base cases implement to perform
 * validation, and provides various utility methods used in the concrete
 * implementations of <code>MessageCopier</code>
 * </p>
 * 
 * @author Tim Emiola
 */
public abstract class PayloadMessageCopier implements MessageCopier {

    public PayloadMessageCopier() {
        attributeCopier = new AttributeCopier();
        propertyCopier = new NamedPropertyCopier();
        versionCopier = new VersionCopier();
    }

    public boolean equals(Object o) {
        if (!(o instanceof PayloadMessageCopier)) return false;
        return (o.getClass().getName().equals(this.getClass().getName()));
    }

    public int hashCode() {
        return this.getClass().getName().hashCode();
    }

    public void copyToJMSMessage(HJBMessage source, Message target)
            throws HJBException {
        verifyMessageTypes(source, target);
        getAttributeCopier().copyToJMSMessage(source, target);
        getPropertyCopier().copyToJMSMessage(source, target);
        getVersionCopier().copyToJMSMessage(source, target);
    }

    public void copyToHJBMessage(Message source, HJBMessage target)
            throws HJBException {
        verifyJMSMessageType(source);
        getAttributeCopier().copyToHJBMessage(source, target);
        getPropertyCopier().copyToHJBMessage(source, target);
        getVersionCopier().copyToHJBMessage(source, target);
    }

    public String toString() {
        String clazzName = this.getClass().getName();
        if (-1 == clazzName.lastIndexOf('.')) return "[" + clazzName + "]";
        return "[" + clazzName.substring(clazzName.lastIndexOf('.') + 1) + "]";
    }

    protected void verifyMessageTypes(HJBMessage source, Message target)
            throws HJBException {
        verifyJMSMessageType(target);
        verifyHJBMessageType(source);
    }

    protected void verifyHJBMessageType(HJBMessage source) throws HJBException {
        if (!acceptHJBMessage(source)) {
            handleIllegalJMSMessageType(source);
        }
    }

    protected void verifyJMSMessageType(Message source) throws HJBException {
        if (!acceptJMSMessage(source)) {
            handleIllegalJMSMessageType(source);
        }
    }

    /**
     * Determines if <code>aMessage</code> is the kind of JMS
     * <code>Message</code> that this <code>MessageCopier</code> support.
     * 
     * @param aMessage
     *            a JMS <code>Message</code>
     * @return true if <code>aMessage</code> is the correct type of JMS
     *         <code>Message</code> false otherwise.
     */
    protected abstract boolean acceptJMSMessage(Message aMessage);

    /**
     * Determines if <code>aMessage</code> is the kind of
     * <code>HJBMessage</code> that this <code>MessageCopier</code>
     * supports.
     * 
     * @param aMessage
     *            an <code>HJBMessage</code>
     * @return true if <code>aMessage</code> is the correct type of JMS
     *         <code>Message</code> false otherwise.
     */
    protected abstract boolean acceptHJBMessage(HJBMessage aMessage);

    protected void handleIllegalJMSMessageType(Class messageClazz)
            throws HJBException {
        handleIllegalJMSMessageType(null == messageClazz ? null
                : messageClazz.getName());
    }

    protected void handleIllegalJMSMessageType(Message aMessage)
            throws HJBException {
        handleIllegalJMSMessageType(null == aMessage ? null
                : aMessage.getClass().getName());
    }

    protected AttributeCopier getAttributeCopier() {
        return attributeCopier;
    }

    protected NamedPropertyCopier getPropertyCopier() {
        return propertyCopier;
    }

    protected VersionCopier getVersionCopier() {
        return versionCopier;
    }

    protected void handleIllegalJMSMessageType(HJBMessage aMessage)
            throws HJBException {
        handleIllegalJMSMessageType(null == aMessage ? null
                : aMessage.getHeader(MessageCopierFactory.HJB_JMS_MESSAGE_INTERFACE));
    }

    protected void handleIllegalJMSMessageType(String clazzName)
            throws HJBException {
        String message = strings().getString(HJBStrings.INCORRECT_JMS_MESSAGE_TYPE,
                                             this,
                                             clazzName);
        LOG.error(message);
        throw new HJBException(message);
    }

    protected void handleMessageCopyFailure(Message message, Exception e) {
        String errorMessage = strings().getString(HJBStrings.COULD_NOT_COPY_MESSAGE,
                                                  this,
                                                  null == message ? "null"
                                                          : message.getClass()
                                                              .getName());
        LOG.error(errorMessage, e);
        throw new HJBException(errorMessage, e);
    }

    protected HJBStrings strings() {
        return STRINGS;
    }

    private NamedPropertyCopier propertyCopier;
    private AttributeCopier attributeCopier;
    private VersionCopier versionCopier;

    private static final Logger LOG = Logger.getLogger(PayloadMessageCopier.class);
    private static final HJBStrings STRINGS = new HJBStrings();
}
