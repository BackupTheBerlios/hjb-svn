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

import org.jmock.core.Invocation;
import org.jmock.core.Stub;

public class UpdateByteArrayStub implements Stub {

    public UpdateByteArrayStub(byte[] bytesToAdd) {
        this.bytesToAdd = bytesToAdd;
        if (null == this.bytesToAdd) this.bytesToAdd = new byte[0];
    }

    public Object invoke(Invocation invocation) throws Throwable {
        byte[] original = (byte[]) invocation.parameterValues.get(0);
        int numberCopied = Math.min(original.length, getBytesToAdd().length);
        if (numberCopied <= 0) {
            return new Integer(-1);
        } else {
            System.arraycopy(getBytesToAdd(), 0, original, 0, numberCopied);
            int remainingBytes = getBytesToAdd().length - numberCopied;
            if (remainingBytes <= 0) {
                setBytesToAdd(new byte[0]);
            } else {
                byte[] newBytesToAdd = new byte[remainingBytes];
                System.arraycopy(getBytesToAdd(),
                                 numberCopied,
                                 newBytesToAdd,
                                 0,
                                 remainingBytes);
                setBytesToAdd(newBytesToAdd);
            }
            return new Integer(numberCopied);
        }
    }

    public StringBuffer describeTo(StringBuffer buffer) {
        return buffer.append("adds <" + new String(bytesToAdd) + "> as Bytes");
    }

    protected byte[] getBytesToAdd() {
        return bytesToAdd;
    }

    protected void setBytesToAdd(byte[] bytesToAdd) {
        this.bytesToAdd = bytesToAdd;
    }

    private byte[] bytesToAdd;
}
