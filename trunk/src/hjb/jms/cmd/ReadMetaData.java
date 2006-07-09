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
package hjb.jms.cmd;

import javax.jms.JMSException;

import hjb.jms.HJBConnection;
import hjb.jms.MetadataReaderAssistant;
import hjb.misc.HJBException;
import hjb.misc.HJBStrings;

public class ReadMetaData extends ConnectionCommand {

    public ReadMetaData(HJBConnection theConnection) {
        super(theConnection);
        this.assistant = new MetadataReaderAssistant();
    }

    public void execute() {
        assertNotCompleted();
        try {
            setMetaDataAsText(assistant.asText(getTheConnection().getMetaData()));
        } catch (RuntimeException e) {
            recordFault(e);
        } catch (JMSException e) {
            recordFault(new HJBException(e));
        }
        completed();
    }

    public String getDescription() {
        return strings().getString(HJBStrings.DESCRIPTION_OF_READ_META_DATA,
                                   getTheConnection());
    }

    public String getStatusMessage() {
        if (isExecutedOK()) {
            return strings().getString(HJBStrings.SUCCESS_MESSAGE_OF_READ_META_DATA,
                                       getTheConnection());
        } else {
            return getFault().getMessage();
        }
    }

    public String getMetaDataAsText() {
        return metaDataAsText;
    }

    public void setMetaDataAsText(String metaDataAsText) {
        this.metaDataAsText = metaDataAsText;
    }

    private String metaDataAsText;
    private MetadataReaderAssistant assistant;
}
