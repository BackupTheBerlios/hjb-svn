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
package hjb.http.cmd;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.*;

import org.jmock.Mock;

import hjb.jms.HJBProvider;
import hjb.jms.HJBRoot;
import hjb.misc.HJBClientException;
import hjb.misc.HJBConstants;
import hjb.misc.HJBNotFoundException;
import hjb.misc.MessageProducerArguments;
import hjb.msg.HJBMessage;
import hjb.testsupport.BaseHJBTestCase;
import hjb.testsupport.MockHJBRuntime;

public class JMSArgumentFinderTest extends BaseHJBTestCase {

    public void testFindsHJBMessageIfOneIsPresent() {
        decodedParameters.put(HJBConstants.MESSAGE_TO_SEND,
                              "testMessage");
        String[] keysToBeExcludedFromTheHeader = new String[] {
                HJBConstants.MESSAGE_TO_SEND,
                HJBConstants.DELIVERY_MODE,
                HJBConstants.TIME_TO_LIVE,
                HJBConstants.DESTINATION_URL,
                HJBConstants.PRIORITY,
        };
        for (int i = 0; i < keysToBeExcludedFromTheHeader.length; i++) {
            decodedParameters.put(keysToBeExcludedFromTheHeader[i],
                                  "should be ignored");
        }
        HJBMessage message = new JMSArgumentFinder().findHJBMessage(decodedParameters);
        assertNotNull(message);
        for (int i = 0; i < keysToBeExcludedFromTheHeader.length; i++) {
            String key = keysToBeExcludedFromTheHeader[i];
            assertNull(message.getHeader(key));
        }
    }

    public void testThrowsExceptionIfExpectedHJBMessageIsNotPresentOrInvalid() {
        try {
            new JMSArgumentFinder().findHJBMessage(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.MESSAGE_TO_SEND, null);
        try {
            new JMSArgumentFinder().findHJBMessage(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.MESSAGE_TO_SEND, new Object());
        try {
            new JMSArgumentFinder().findHJBMessage(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
    }

    public void testFindEnvironmentIncludesProviderNameAndAllParameters() {
        decodedParameters.put("anEnvironmentParameter", new Object());
        decodedParameters.put("anotherEnvironmentParameter", new Object());
        Hashtable environment = new JMSArgumentFinder().findEnvironment(decodedParameters,
                                                                        "testProvider");
        assertEquals("testProvider",
                     environment.get(HJBProvider.HJB_PROVIDER_NAME));
        assertTrue(environment.containsKey("anEnvironmentParameter"));
        assertTrue(environment.containsKey("anotherEnvironmentParameter"));
    }

    public void testFindsMessageSelectorIfOneIsPresent() {
        assertNull(new JMSArgumentFinder().findMessageSelector(decodedParameters));
        decodedParameters.put(HJBConstants.MESSAGE_SELECTOR,
                              "a selector");
        assertNotNull(new JMSArgumentFinder().findMessageSelector(decodedParameters));
    }

    public void testFindsClientIdIfOneIsPresent() {
        assertNull(new JMSArgumentFinder().findMessageSelector(decodedParameters));
        decodedParameters.put(HJBConstants.CLIENT_ID, "testClientId");
        assertNotNull(new JMSArgumentFinder().findClientId(decodedParameters));
    }

    public void testFindsDisableTimestampsCorrectly() {
        assertFalse(new JMSArgumentFinder().findDisableTimestamps(decodedParameters));
        decodedParameters.put(HJBConstants.DISABLE_TIMESTAMPS,
                              Boolean.FALSE);
        assertFalse(new JMSArgumentFinder().findDisableTimestamps(decodedParameters));
        decodedParameters.put(HJBConstants.DISABLE_TIMESTAMPS,
                              Boolean.TRUE);
        assertTrue(new JMSArgumentFinder().findDisableTimestamps(decodedParameters));
    }

    public void testFindsDisableMessageIdsCorrectly() {
        assertFalse(new JMSArgumentFinder().findDisableTimestamps(decodedParameters));
        decodedParameters.put(HJBConstants.DISABLE_MESSAGE_IDS,
                              Boolean.FALSE);
        assertFalse(new JMSArgumentFinder().findDisableMessageIds(decodedParameters));
        decodedParameters.put(HJBConstants.DISABLE_MESSAGE_IDS,
                              Boolean.TRUE);
        assertTrue(new JMSArgumentFinder().findDisableMessageIds(decodedParameters));
    }

    public void testFindsIsRecursiveListingCorrectly() {
        assertFalse(new JMSArgumentFinder().findIsRecursiveListing(decodedParameters));
        decodedParameters.put(HJBConstants.LISTING_RECURSE,
                              new Object());
        assertTrue(new JMSArgumentFinder().findIsRecursiveListing(decodedParameters));
    }

    public void testFindsNoLocalCorrectly() {
        assertFalse(new JMSArgumentFinder().findDisableTimestamps(decodedParameters));
        decodedParameters.put(HJBConstants.CONSUMER_NOLOCAL,
                              Boolean.FALSE);
        assertFalse(new JMSArgumentFinder().findNoLocal(decodedParameters));
        decodedParameters.put(HJBConstants.CONSUMER_NOLOCAL,
                              Boolean.TRUE);
        assertTrue(new JMSArgumentFinder().findNoLocal(decodedParameters));
    }

    public void testReturnsNullIfDeliveryModeIsNotAvailable() {
        assertNull(new JMSArgumentFinder().findDeliveryMode(decodedParameters));
    }

    public void testReturnsNullIfDeliveryModeIsInvalid() throws Exception {
        decodedParameters.put(HJBConstants.DELIVERY_MODE, new Object());
        assertNull(new JMSArgumentFinder().findDeliveryMode(decodedParameters));
        decodedParameters.put(HJBConstants.DELIVERY_MODE, new Integer(5));
        assertNull(new JMSArgumentFinder().findDeliveryMode(decodedParameters));
    }

    public void testValidDeliveryModeValuesOk() throws Exception {
        int validDeliveryModes[] = {
                DeliveryMode.PERSISTENT, DeliveryMode.NON_PERSISTENT,
        };
        for (int i = 0; i < validDeliveryModes.length; i++) {
            decodedParameters.clear();
            decodedParameters.put(HJBConstants.DELIVERY_MODE,
                                  new Integer(validDeliveryModes[i]));
            assertEquals(new Integer(validDeliveryModes[i]),
                         new JMSArgumentFinder().findDeliveryMode(decodedParameters));
        }
    }

    public void testReturnsNullForInvalidPriorities() {
        int invalidPriorities[] = {
                -1, 10,
        };
        for (int i = 0; i < invalidPriorities.length; i++) {
            decodedParameters.clear();
            decodedParameters.put(HJBConstants.PRIORITY,
                                  new Integer(invalidPriorities[i]));
            assertNull(new JMSArgumentFinder().findPriority(decodedParameters));
        }
        decodedParameters.clear();
        decodedParameters.put(HJBConstants.PRIORITY, new Object());
        assertNull(new JMSArgumentFinder().findPriority(decodedParameters));
    }

    public void testFindsValidPrioritysWhenPresent() {
        int validPriorities[] = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9
        };
        for (int i = 0; i < validPriorities.length; i++) {
            decodedParameters.clear();
            decodedParameters.put(HJBConstants.PRIORITY,
                                  new Integer(validPriorities[i]));
            assertEquals(new Integer(validPriorities[i]),
                         new JMSArgumentFinder().findPriority(decodedParameters));
        }
    }

    public void testReturnsNullIfTimeoutIsNotPresentOrIsInvalid() {
        assertNull(new JMSArgumentFinder().findTimeout(decodedParameters));
        decodedParameters.put(HJBConstants.TIMEOUT, new Object());
        assertNull(new JMSArgumentFinder().findTimeout(decodedParameters));
    }

    public void testReturnsTimeoutIfItIsPresentAndValid() throws Exception {
        decodedParameters.put(HJBConstants.TIMEOUT, new Long(1000));
        assertEquals(new Long(1000),
                     new JMSArgumentFinder().findTimeout(decodedParameters));
    }

    public void testReturnsNullIfTimeToLiveIsNotPresentOrInvalid() {
        assertNull(new JMSArgumentFinder().findTimeToLive(decodedParameters));
        decodedParameters.put(HJBConstants.TIME_TO_LIVE, new Object());
        assertNull(new JMSArgumentFinder().findTimeToLive(decodedParameters));
    }

    public void testReturnsTimeToLiveIfItIsPresentAndValid() throws Exception {
        decodedParameters.put(HJBConstants.TIME_TO_LIVE, new Long(1000));
        assertEquals(new Long(1000),
                     new JMSArgumentFinder().findTimeToLive(decodedParameters));
    }

    public void testFindsProducerArgumentsIfTheyArePresent() {
        decodedParameters.put(HJBConstants.TIME_TO_LIVE, new Long(1000));
        decodedParameters.put(HJBConstants.PRIORITY, new Integer(1));
        decodedParameters.put(HJBConstants.DELIVERY_MODE,
                              new Integer(DeliveryMode.NON_PERSISTENT));
        MessageProducerArguments expectedArguments = new MessageProducerArguments(false,
                                                                                  false,
                                                                                  new Long(1000),
                                                                                  new Integer(1),
                                                                                  new Integer(DeliveryMode.NON_PERSISTENT));
        MessageProducerArguments actualArguments = new JMSArgumentFinder().findProducerArguments(decodedParameters);
        assertEquals(expectedArguments.getTimeToLive(),
                     actualArguments.getTimeToLive());
        assertEquals(expectedArguments.getDeliveryMode(),
                     actualArguments.getDeliveryMode());
        assertEquals(expectedArguments.getPriority(),
                     actualArguments.getPriority());
        assertEquals(expectedArguments.isDisableMessageIds(),
                     actualArguments.isDisableMessageIds());
        assertEquals(expectedArguments.isDisableTimestamps(),
                     actualArguments.isDisableTimestamps());
    }

    public void testReturnsDefaultAcknowledgeModeIfItsNotPresentOrInvalid() {
        assertEquals(HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE,
                     new JMSArgumentFinder().findAcknowledgementMode(decodedParameters));
        decodedParameters.put(HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
                              new Object());
        assertEquals(HJBConstants.DEFAULT_ACKNOWLEDGEMENT_MODE,
                     new JMSArgumentFinder().findAcknowledgementMode(decodedParameters));
    }

    public void testReturnsAcknowledgmentModeOkIfItsPresentAndValid()
            throws Exception {
        int validAcknowledegementModes[] = new int[] {
                Session.CLIENT_ACKNOWLEDGE,
                Session.DUPS_OK_ACKNOWLEDGE,
                Session.SESSION_TRANSACTED,
        };
        for (int i = 0; i < validAcknowledegementModes.length; i++) {
            decodedParameters.put(HJBConstants.SESSION_ACKNOWLEDGEMENT_MODE,
                                  new Integer(validAcknowledegementModes[i]));
            assertEquals(validAcknowledegementModes[i],
                         new JMSArgumentFinder().findAcknowledgementMode(decodedParameters));
        }

    }

    public void testFindsTransactedCorrectly() {
        assertFalse(new JMSArgumentFinder().findTransacted(decodedParameters));
        decodedParameters.put(HJBConstants.SESSION_TRANSACTED,
                              Boolean.FALSE);
        assertFalse(new JMSArgumentFinder().findTransacted(decodedParameters));
        decodedParameters.put(HJBConstants.SESSION_TRANSACTED,
                              Boolean.TRUE);
        assertTrue(new JMSArgumentFinder().findTransacted(decodedParameters));
    }

    public void testThrowsExceptionIfExpectedSubscriberIsInvalid() {
        try {
            new JMSArgumentFinder().findSubscriberName(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.SUBSCRIBER_NAME, null);
        try {
            new JMSArgumentFinder().findSubscriberName(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.SUBSCRIBER_NAME, new Object());
        try {
            new JMSArgumentFinder().findSubscriberName(decodedParameters);
            fail("Should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
    }

    public void testFindsSubscriberIfOneIsPresentAndValid() throws Exception {
        decodedParameters.put(HJBConstants.SUBSCRIBER_NAME,
                              "testSubscriberName");
        assertEquals("testSubscriberName",
                     new JMSArgumentFinder().findSubscriberName(decodedParameters));
    }

    public void testFindsRequiredDestinationIfItIsAvailable() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        assertNotNull(new JMSArgumentFinder().findRequiredDestination(decodedParameters,
                                                                      root,
                                                                      "testProvider"));
    }

    public void testThrowsExceptionIfExpectedDestinationUrlIsInvalid()
            throws Exception {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foobar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        try {
            new JMSArgumentFinder().findRequiredDestination(decodedParameters,
                                                            root,
                                                            "testProvider");
            fail("should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.DESTINATION_URL, new Object());
        try {
            new JMSArgumentFinder().findRequiredDestination(decodedParameters,
                                                            root,
                                                            "testProvider");
            fail("should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
    }

    public void testThrowsExceptionIfSessionProviderDoesNotMatchSessionProvider() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foobar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        try {
            new JMSArgumentFinder().findRequiredDestination(decodedParameters,
                                                            root,
                                                            "sessionProvider");
            fail("should have thrown an HJBClientException");
        } catch (HJBClientException e) {}
    }

    public void testThrowsExceptionIfRequiredDestinationIsNotAvailable() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foobar/testProvider/destination/notThere");
        mockHJB.make1Destination(root,
                                 "sessionProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        try {
            new JMSArgumentFinder().findRequiredDestination(decodedParameters,
                                                            root,
                                                            "sessionProvider");
            fail("should have thrown an HJBClientException");
        } catch (HJBNotFoundException e) {}
    }

    public void testFindsOptionalDestinationIfOneIsPresent() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        assertNotNull(new JMSArgumentFinder().findOptionalDestination(decodedParameters,
                                                                      root,
                                                                      "testProvider"));
    }

    public void testReturnsNullIfOptionalDestinationIsNotPresent()
            throws Exception {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/notThere");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        assertNull(new JMSArgumentFinder().findOptionalDestination(decodedParameters,
                                                                   root,
                                                                   "testProvider"));
    }

    public void testFindsQueueIfOneIsPresentAndValid() {
        Mock mockDestination = mock(Queue.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Queue) mockDestination.proxy());
        assertNotNull(new JMSArgumentFinder().findQueue(decodedParameters,
                                                        root,
                                                        "testProvider"));
    }

    public void testThrowsExceptionIfQueueIsNotPresentOrIsInvalid() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/notHere");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        try {
            assertNotNull(new JMSArgumentFinder().findQueue(decodedParameters,
                                                            root,
                                                            "testProvider"));
            fail("should have thrown HJBNotFoundException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        try {
            assertNotNull(new JMSArgumentFinder().findQueue(decodedParameters,
                                                            root,
                                                            "testProvider"));
            fail("should have thrown HJBNotFoundException");
        } catch (HJBClientException e) {}
    }

    public void testFindsTopicIfOneIsPresentAndValid() {
        Mock mockDestination = mock(Topic.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Topic) mockDestination.proxy());
        assertNotNull(new JMSArgumentFinder().findTopic(decodedParameters,
                                                        root,
                                                        "testProvider"));
    }

    public void testThrowsExceptionIfTopicIsNotPresentOrIsInvalid() {
        Mock mockDestination = mock(Destination.class);
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/notHere");
        mockHJB.make1Destination(root,
                                 "testProvider",
                                 "testDestination",
                                 (Destination) mockDestination.proxy());
        try {
            assertNotNull(new JMSArgumentFinder().findTopic(decodedParameters,
                                                            root,
                                                            "testProvider"));
            fail("should have thrown HJBNotFoundException");
        } catch (HJBClientException e) {}
        decodedParameters.put(HJBConstants.DESTINATION_URL,
                              "/foo/bar/testProvider/destination/testDestination");
        try {
            assertNotNull(new JMSArgumentFinder().findTopic(decodedParameters,
                                                            root,
                                                            "testProvider"));
            fail("should have thrown HJBNotFoundException");
        } catch (HJBClientException e) {}
    }

    protected void setUp() throws Exception {
        testRootPath = File.createTempFile("test", null).getParentFile();
        root = new HJBRoot(testRootPath, defaultTestClock());
        mockHJB = new MockHJBRuntime();
        decodedParameters.clear();
    }

    private Map decodedParameters = new HashMap();
    private HJBRoot root;
    private File testRootPath;
    private MockHJBRuntime mockHJB;
}
