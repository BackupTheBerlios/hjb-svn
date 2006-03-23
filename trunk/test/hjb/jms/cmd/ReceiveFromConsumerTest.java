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

import java.io.File;
import java.util.HashMap;

import javax.jms.*;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.http.cmd.HJBMessageWriter;
import hjb.jms.*;
import hjb.misc.HJBException;
import hjb.msg.HJBMessage;
import hjb.msg.MessageCopierFactory;
import hjb.testsupport.MessageAttributeInvoker;
import hjb.testsupport.MockHJBRuntime;

public class ReceiveFromConsumerTest extends MockObjectTestCase {

    public void testCommitSessionThrowsOnNullInputs() {
        try {
            new ReceiveFromConsumer(null, 1);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    protected HJBMessage createTestTextHJBMessage() {
        HashMap headers = new HashMap();
        headers.put(MessageCopierFactory.HJB_JMS_CLASS, TextMessage.class.getName());
        return new HJBMessage(headers, "boo!");        
    }
    
    public void testExecuteReceivesAMessage() {
        Mock mockTextMessage = mock(TextMessage.class);
        mockTextMessage.expects(once()).method("getText").will(returnValue("boo!"));
        mockTextMessage.expects(once()).method("acknowledge");
        MessageAttributeInvoker attributeInvoker = new MessageAttributeInvoker();
        attributeInvoker.willInvokeAllAttributeAccessors(mockTextMessage, this);
        attributeInvoker.willInvokeGetPropertyNamesAndReturnEmptyHanded(mockTextMessage, this);

        Message testMessage = (TextMessage) mockTextMessage.proxy();

        Mock mockConsumer = mock(MessageConsumer.class);
        mockConsumer.expects(once()).method("receive").will(returnValue(testMessage));
        MessageConsumer testConsumer = (MessageConsumer) mockConsumer.proxy();
        
        Mock mockSession = mock(Session.class);
        mockSession.stubs().method("createConsumer").will(returnValue(testConsumer));
        Session testSession = (Session) mockSession.proxy();
        
        HJBRoot root = new HJBRoot(testRootPath);
        mockHJB.make1Session(root, testSession, "testProvider", "testFactory");
        HJBConnection testConnection = root.getProvider("testProvider").getConnectionFactory("testFactory").getConnection(0);

        create1Consumer(testConnection);
        ReceiveFromConsumer command = new ReceiveFromConsumer(new HJBMessenger(testConnection, 0),
                                                    0);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNotNull(command.getMessageReceived());
        System.err.println(new HJBMessageWriter().asText(command.getMessageReceived()));
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }

    protected void create1Consumer(HJBConnection testConnection) {
        HJBSessionConsumers sessionConsumers = testConnection.getSessionConsumers();
        Mock mockDestination = mock(Destination.class);
        Destination testDestination = (Destination) mockDestination.proxy();
        CreateConsumer command = new CreateConsumer(sessionConsumers,
                                                    0,
                                                    testDestination);
        command.execute();
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        mockHJB = new MockHJBRuntime();
    }

    protected File testRootPath;
    protected MockHJBRuntime mockHJB;
}
