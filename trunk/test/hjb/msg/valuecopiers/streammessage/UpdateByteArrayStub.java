package hjb.msg.valuecopiers.streammessage;

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
            System.arraycopy(getBytesToAdd(),
                             0,
                             original,
                             0,
                             numberCopied);
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
