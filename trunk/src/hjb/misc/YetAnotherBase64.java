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

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * <code>YetAnotherBase64</code> translates byte arrays into Base64 encoded
 * strings and vice versa as described in rfc3548.
 * 
 * See <a href="http://www.ietf.org/rfc/rfc3548.txt">RFC 3548</a>
 * 
 * @author Tim Emiola
 */
public class YetAnotherBase64 {

    public YetAnotherBase64() {
        setupTranslationAlphabets();
    }

    public String encode(byte[] input) {
        if (null == input) {
            return "";
        }
        StringBuffer output = new StringBuffer();
        int index = 0;
        while (index < input.length) {
            int remaining = input.length - index;
            switch (remaining) {
            case 2:
                output.append(encode2BytesOnly(input, index));
                index = input.length;
                break;
            case 1:
                output.append(encode1ByteOnly(input, index));
                index = input.length;
                break;
            default:
                output.append(encodeFull3Bytes(input, index));
                index += 3;
            }
        }
        return output.toString();
    }

    public byte[] decode(String input) {
        if (null == input) return new byte[0];
        byte[] asBytes = input.getBytes();
        return decodeSignificantOctets(asBytes, countSignificantOctets(asBytes));
    }

    protected int countSignificantOctets(byte[] asBytes)
            throws IllegalStateException {
        int result = asBytes.length - countIgnored(asBytes);
        if (0 != (result % 4)) {
            throw new IllegalStateException("Could not process as Base64 encoded text,"
                    + " the number of valid octets ["
                    + result
                    + "] was not modulo 4; input was " + new String(asBytes));
        }
        LOG.debug("Number of significant octets: " + result);
        return result;
    }

    protected byte[] decodeSignificantOctets(byte[] asBytes,
                                             int significantOctets) {
        byte[] result = new byte[(significantOctets * 3 / 4)
                - countPadBytes(asBytes)];
        for (int index = 0, byteIndex = 0; index < asBytes.length; byteIndex += 3) {
            index = decodeNext4Bytes(index, asBytes, byteIndex, result);
        }
        return result;
    }

    protected int countPadBytes(byte[] asBytes) {
        int result = 0;
        for (int i = asBytes.length - 4; i < asBytes.length; i++) {
            if (isPad(asBytes[i])) result++;
        }
        LOG.debug("Number of pad bytes: " + result);
        return result;
    }

    protected int countIgnored(byte[] asBytes) {
        int result = 0;
        for (int i = 0; i < asBytes.length; i++) {
            if (isIgnored(asBytes[i])) result++;
        }
        LOG.debug("Number of ignored: " + result);
        return result;
    }

    protected int decodeNext4Bytes(int sourceIndex,
                                   byte[] source,
                                   int targetIndex,
                                   byte[] target) {

        // determine the next 4 valid characters
        int index = sourceIndex;
        for (; isIgnored(source[index]); index++);
        byte firstChar = source[index++];
        for (; isIgnored(source[index]); index++);
        byte secondChar = source[index++];
        for (; isIgnored(source[index]); index++);
        byte thirdChar = source[index++];
        for (; isIgnored(source[index]); index++);
        byte fourthChar = source[index++];

        updateTarget(target,
                     targetIndex,
                     firstChar,
                     secondChar,
                     thirdChar,
                     fourthChar);
        return index;
    }

    protected void updateTarget(byte[] target,
                                int targetIndex,
                                byte firstChar,
                                byte secondChar,
                                byte thirdChar,
                                byte fourthChar) {
        // use an integer to combine the encoded bits:
        // first take the initial two sets of sixbits, then
        // add in the next two if the third and
        // fourth char indicate they are required
        byte firstSix = fromBase64()[firstChar];
        byte secondSix = fromBase64()[secondChar];
        int combinedBits = ((firstSix & 0xFF) << 18)
                | ((secondSix & 0xFF) << 12);

        if (isPad(thirdChar)) {
            target[targetIndex] = (byte) (combinedBits >> 16);
        } else if (isPad(fourthChar)) {
            combinedBits |= ((fromBase64()[thirdChar] & 0xFF) << 6);
            target[targetIndex] = (byte) (combinedBits >>> 16);
            target[targetIndex + 1] = (byte) (combinedBits >>> 8);
        } else {
            combinedBits |= ((fromBase64()[thirdChar] & 0xFF) << 6);
            combinedBits |= ((fromBase64()[fourthChar] & 0xFF));
            target[targetIndex] = (byte) (combinedBits >>> 16);
            target[targetIndex + 1] = (byte) (combinedBits >>> 8);
            target[targetIndex + 2] = (byte) (combinedBits);
        }
    }

    protected char[] encodeFull3Bytes(byte[] source, int start) {
        byte firstByte = source[start];
        byte secondByte = source[start + 1];
        byte thirdByte = source[start + 2];
        int combinedBytes = ((firstByte << 24) >>> 8)
                | ((secondByte << 24) >>> 16) | ((thirdByte << 24) >>> 24);
        return new char[] {
                toBase64()[(combinedBytes >>> 18)],
                toBase64()[(combinedBytes >>> 12) & LOWER_SIX_BITS],
                toBase64()[(combinedBytes >>> 6) & LOWER_SIX_BITS],
                toBase64()[(combinedBytes) & LOWER_SIX_BITS],
        };
    }

    protected char[] encode2BytesOnly(byte[] source, int start) {
        byte firstByte = source[start];
        byte secondByte = source[start + 1];
        int combinedBytes = ((firstByte << 24) >>> 8)
                | ((secondByte << 24) >>> 16);
        return new char[] {
                toBase64()[(combinedBytes >>> 18)],
                toBase64()[(combinedBytes >>> 12) & LOWER_SIX_BITS],
                toBase64()[(combinedBytes >>> 6) & LOWER_SIX_BITS],
                PAD,
        };
    }

    protected char[] encode1ByteOnly(byte[] source, int start) {
        byte firstByte = source[start];
        int combinedBytes = ((firstByte << 24) >>> 8);
        return new char[] {
                toBase64()[(combinedBytes >>> 18)],
                toBase64()[(combinedBytes >>> 12) & LOWER_SIX_BITS],
                PAD,
                PAD,
        };
    }

    protected void setupTranslationAlphabets() {
        setupEncodingAlphabet();
        from = new byte[DECODER_SIZE];
        Arrays.fill(from, Byte.MIN_VALUE);
        for (int i = 0; i < to.length; i++) {
            from[to[i]] = (byte) i;
        }
    }

    protected void setupEncodingAlphabet() {
        to = new char[ENCODER_SIZE];
        for (int i = 0; i <= 25; i++) {
            to[i] = (char) ('A' + i);
        }
        for (int i = 26, j = 0; i <= 51; i++, j++) {
            to[i] = (char) ('a' + j);
        }
        for (int i = 52, j = 0; i <= 61; i++, j++) {
            to[i] = (char) ('0' + j);
        }
        to[62] = '+';
        to[63] = '/';
    }

    protected byte[] fromBase64() {
        return from;
    }

    protected char[] toBase64() {
        return to;
    }

    protected boolean isPad(byte b) {
        return PAD == b;
    }

    protected boolean isIgnored(byte b) {
        return !isPad(b) && (fromBase64()[b] == Byte.MIN_VALUE);
    }

    private byte[] from;
    private char[] to;

    public static final int DECODER_SIZE = 255;
    public static final int ENCODER_SIZE = 64;
    protected static final byte PAD = '=';
    protected static final int LOWER_SIX_BITS = 0x3f;

    private static final Logger LOG = Logger.getLogger(YetAnotherBase64.class);
}
