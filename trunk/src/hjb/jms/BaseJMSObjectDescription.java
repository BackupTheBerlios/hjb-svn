package hjb.jms;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import hjb.http.cmd.HJBMessageWriter;
import hjb.misc.HJBStrings;
import hjb.msg.codec.OrderedTypedValueCodec;

/**
 * <code>BaseJMSObjectDescription</code> is the base class for the various XXXDescription classes.
 * 
 * It provides useful protected methods to each of its subclasses. 
 *
 * @author Tim Emiola
 */
public class BaseJMSObjectDescription {

    public BaseJMSObjectDescription(int index, String invalidIndexMessage) {
        if (index < 0) {
            throw new IllegalArgumentException(strings().getString(invalidIndexMessage,
                                                                   new Integer(index)));
        }
        this.codec = new OrderedTypedValueCodec();
        this.index = index;
    }
    
    public String toString() {
        return getPathName() + getExtraInformation();
    }
    
    public String longDescription() {
        StringWriter sw = new StringWriter();
        writeLongDescription(sw);
        return sw.toString();
    }

    public void writeLongDescription(Writer aWriter) {
        PrintWriter pw = new PrintWriter(aWriter);
        pw.print(this);
        pw.println();
        new HJBMessageWriter().writeAsText(attributesAsAMap(), pw);
    }

    protected Map attributesAsAMap() {
        return Collections.EMPTY_MAP;
    }

    protected int getIndex() {
        return index;
    }
    
    protected HJBStrings strings() {
        return STRINGS;
    }
    
    protected String getPathName() {
        return "";
    }
    
    protected String getExtraInformation() {
        return "";
    }

    protected OrderedTypedValueCodec getCodec() {
        return codec;
    }

    private final int index;
    private final OrderedTypedValueCodec codec;
    private static final HJBStrings STRINGS = new HJBStrings();
}
