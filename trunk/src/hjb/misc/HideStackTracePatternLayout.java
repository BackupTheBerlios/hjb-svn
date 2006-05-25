package hjb.misc;

import org.apache.log4j.PatternLayout;

/**
 * <code>HideStackTracePatternLayout</code> extends <code>PatternLayout</code>
 * by inhibiting the display of error message stack traces.
 * 
 * @author Tim Emiola
 */
public class HideStackTracePatternLayout extends PatternLayout {

    /**
     * Overrides the superclass to always return <code>false</code>, so that
     * error message stack dumps are not printed out.
     */
    public boolean ignoresThrowable() {
        return false;
    }

}
