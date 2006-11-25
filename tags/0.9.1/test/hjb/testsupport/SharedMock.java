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
package hjb.testsupport;

import org.jmock.Mock;

/**
 * A <code>SharedMock</code> is used to provide a mock for use by
 * {@link hjb.testsupport.MockInitialContextFactory}
 */
public class SharedMock {

    public static SharedMock getInstance() {
        return instance;
    }

    protected SharedMock() {
    }

    public Mock getCurrentMock() {
        return currentMock;
    }

    public void setCurrentMock(Mock currentMock) {
        this.currentMock = currentMock;
    }

    private Mock currentMock;
    private static SharedMock instance = new SharedMock();
}
