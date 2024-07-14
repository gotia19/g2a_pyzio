/*
 * ISC License
 * Copyright 2017, Gray Watson
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.capgemini.stk.framework.actions.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import static org.junit.Assert.fail;

public final class GenerateOTP {
    private static final int    DEFAULT_TIME_STEP_SECONDS = 30;
    private static final String blockOfZeros;
    private static final int    NUM_DIGITS_OUTPUT         = 6;

    static {
        char[] chars = new char[NUM_DIGITS_OUTPUT];
        Arrays.fill(chars, '0');
        blockOfZeros = new String(chars);
    }

    private GenerateOTP() {
    }

    public static String generateCurrentNumberString(String base32Secret) {
        try {
            return generateNumberString(base32Secret, System.currentTimeMillis(), DEFAULT_TIME_STEP_SECONDS);
        }
        catch (GeneralSecurityException e) {
            fail("Generate OTP error: " + e.getMessage());
        }
        return null;
    }

    private static String generateNumberString(String base32Secret, long timeMillis, int timeStepSeconds) throws GeneralSecurityException {
        long number = generateNumber(base32Secret, timeMillis, timeStepSeconds);
        return zeroPrepend(number, NUM_DIGITS_OUTPUT);
    }

    private static byte[] decodeBase32(String str) {
        int    numBytes    = ((str.length() * 5) + 7) / 8;
        byte[] result      = new byte[numBytes];
        int    resultIndex = 0;
        int    which       = 0;
        int    working     = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int  val;
            if (ch >= 'a' && ch <= 'z') {
                val = ch - 'a';
            }
            else if (ch >= 'A' && ch <= 'Z') {
                val = ch - 'A';
            }
            else if (ch >= '2' && ch <= '7') {
                val = 26 + (ch - '2');
            }
            else if (ch == '=') {
                // special case
                which = 0;
                break;
            }
            else {
                throw new IllegalArgumentException("Invalid base-32 character: " + ch + " in: " + str);
            }
            /*
             * There are probably better ways to do this but this seemed the most straightforward.
             */
            switch (which) {
                case 0:
                    // all 5 bits is top 5 bits
                    working = (val & 0x1F) << 3;
                    which = 1;
                    break;
                case 1:
                    // top 3 bits is lower 3 bits
                    working |= (val & 0x1C) >> 2;
                    result[resultIndex++] = (byte) working;
                    // lower 2 bits is upper 2 bits
                    working = (val & 0x03) << 6;
                    which = 2;
                    break;
                case 2:
                    // all 5 bits is mid 5 bits
                    working |= (val & 0x1F) << 1;
                    which = 3;
                    break;
                case 3:
                    // top 1 bit is lowest 1 bit
                    working |= (val & 0x10) >> 4;
                    result[resultIndex++] = (byte) working;
                    // lower 4 bits is top 4 bits
                    working = (val & 0x0F) << 4;
                    which = 4;
                    break;
                case 4:
                    // top 4 bits is lowest 4 bits
                    working |= (val & 0x1E) >> 1;
                    result[resultIndex++] = (byte) working;
                    // lower 1 bit is top 1 bit
                    working = (val & 0x01) << 7;
                    which = 5;
                    break;
                case 5:
                    // all 5 bits is mid 5 bits
                    working |= (val & 0x1F) << 2;
                    which = 6;
                    break;
                case 6:
                    // top 2 bits is lowest 2 bits
                    working |= (val & 0x18) >> 3;
                    result[resultIndex++] = (byte) working;
                    // lower 3 bits of byte 6 is top 3 bits
                    working = (val & 0x07) << 5;
                    which = 7;
                    break;
                case 7:
                    // all 5 bits is lower 5 bits
                    working |= (val & 0x1F);
                    result[resultIndex++] = (byte) working;
                    which = 0;
                    break;
            }
        }
        if (which != 0) {
            result[resultIndex++] = (byte) working;
        }
        if (resultIndex != result.length) {
            result = Arrays.copyOf(result, resultIndex);
        }
        return result;
    }

    private static long generateNumber(String base32Secret, long timeMillis, int timeStepSeconds) throws java.security.NoSuchAlgorithmException, java.security.InvalidKeyException {

        byte[] key = decodeBase32(base32Secret);

        byte[] data  = new byte[8];
        long   value = timeMillis / 1000 / timeStepSeconds;
        for (int i = 7; value > 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }

        // encrypt the data with the key and return the SHA1 of it in hex
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        // if this is expensive, could put in a thread-local
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        // take the 4 least significant bits from the encrypted string as an offset
        int offset = hash[hash.length - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = offset; i < offset + 4; ++i) {
            truncatedHash <<= 8;
            // get the 4 bytes at the offset
            truncatedHash |= (hash[i] & 0xFF);
        }
        // cut off the top bit
        truncatedHash &= 0x7FFFFFFF;

        // the token is then the last 6 digits in the number
        truncatedHash %= 1000000;

        return truncatedHash;
    }

    private static String zeroPrepend(long num, int digits) {
        String numStr = Long.toString(num);
        if (numStr.length() >= digits) {
            return numStr;
        }
        StringBuilder sb        = new StringBuilder(digits);
        int           zeroCount = digits - numStr.length();
        sb.append(blockOfZeros, 0, zeroCount);
        sb.append(numStr);
        return sb.toString();
    }
}