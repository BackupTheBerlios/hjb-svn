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
