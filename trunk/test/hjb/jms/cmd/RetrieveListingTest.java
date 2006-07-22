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

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import hjb.jms.info.JMSObjectListing;
import hjb.misc.HJBException;

public class RetrieveListingTest extends MockObjectTestCase {

    public void testRetrieveListingThrowsOnNullJMSObjectListing() {
        try {
            new RetrieveListing(null, "fooPrefix", "fooDescription", false);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testRetrieveListingThrowsOnNullDescription() {
        Mock mockJMSObjectListing = mock(JMSObjectListing.class);
        try {
            new RetrieveListing((JMSObjectListing) mockJMSObjectListing.proxy(),
                                "fooPrefix",
                                null,
                                false);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testRetrieveListingThrowsOnNullPrefix() {
        Mock mockJMSObjectListing = mock(JMSObjectListing.class);
        try {
            new RetrieveListing((JMSObjectListing) mockJMSObjectListing.proxy(),
                                null,
                                "fooDescription",
                                false);
            fail("should have thrown an exception");
        } catch (IllegalArgumentException e) {}
    }

    public void testExecuteSetsTheListing() {
        Mock mockJMSObjectListing = mock(JMSObjectListing.class);
        mockJMSObjectListing.expects(once())
            .method("getListing")
            .withAnyArguments()
            .will(returnValue("listingTextOfAText"));
        RetrieveListing command = new RetrieveListing((JMSObjectListing) mockJMSObjectListing.proxy(),
                                                      "testPrefix",
                                                      "testDescription",
                                                      false);
        command.execute();
        assertTrue(command.isExecutedOK());
        assertTrue(command.isComplete());
        assertNull(command.getFault());
        assertNotNull(command.getStatusMessage());
        assertNotNull(command.getTheOutput());
        try {
            command.execute();
            fail("should have thrown an exception");
        } catch (HJBException e) {}
    }
}
