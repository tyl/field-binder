/*
 * Copyright (c) 2015 - Tyl Consulting s.a.s.
 *
 *   Authors: Edoardo Vacchi
 *   Contributors: Marco Pancotti, Daniele Zonca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tylproject.vaadin.addon.fieldbinder.behavior;

import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation.
 *
 * Supports:
 *   <ul>
 *       <li>numeric ranges, less-than, greater-than</li>
 *       <li>simple string patterns</li>
 *       <li>for any other datatype, it falls back to equality</li>
 *   </ul>
 *
 */
public class DefaultFilterFactory implements FilterFactory {

    @Override
    public Container.Filter createFilter(Class<?> targetType, Object targetPropertyId, Object pattern) {
        if (pattern == null) return null;

        if (String.class.isAssignableFrom(targetType) || java.lang.Enum.class.isAssignableFrom(targetType)) {
            return filterForString(targetPropertyId, pattern.toString());
        } else
        if (isNumberClass(targetType)) {
            return filterForNumber(targetPropertyId, targetType, pattern.toString());
        } else
        if (Date.class.isAssignableFrom(targetType) || DateTime.class.isAssignableFrom(targetType)) {
            Container.Filter filter = filterForCompareDate(targetPropertyId, pattern.toString());
            if (filter != null) return filter;
            return filterForDateRange(targetPropertyId, pattern.toString());
        } else {
            // fallback to equality
            return new Compare.Equal(targetPropertyId, pattern);
        }
    }

    private boolean isNumberClass(Class<?> targetType) {
        return Number.class.isAssignableFrom(targetType)
            || targetType == int.class
            || targetType == long.class
            || targetType == byte.class
            || targetType == double.class
            || targetType == float.class
//            || targetType == char.class
            ;
    }


    /**
     *
     * Simple string pattern matching.
     *
     * It uses a glob-like syntax and maps onto Vaadin's {@link com.vaadin.data.util.filter.SimpleStringFilter}
     * Supports the input patterns:
     *
     * <ul>
     *   <li>ABC   matches ABCA, ABCAB, ABCACC...</li>
     *   <li>ABC*  is equivalent to ABC</li>
     *   <li>*ABC* matches XABCY, ABCY, ZZABCYY...</li>
     *   <li>*ABC  is equivalent to *ABC* (internal Vaadin limitation)</li>
     * </ul>
     */
    protected static final String WILDCARD = "*";
    protected Container.Filter filterForString(Object propertyId, String pattern) {
        // if ends with one WILDCARD
        if (pattern.endsWith(WILDCARD)) {
            // strip it, and check recursively (to strip any other *'s)
            return filterForString(propertyId, pattern.substring(0, pattern.length()-1));
        } else if (pattern.startsWith(WILDCARD)) {
            // if it starts with a WILDCARD, then strip it
            // and look for the pattern anywhere in the word
            do {
                pattern = pattern.substring(1, pattern.length());
            } while (pattern.startsWith(WILDCARD));
            return new SimpleStringFilter(propertyId, pattern, true, false);
        }

        return new SimpleStringFilter(propertyId, pattern, true, true);
    }

    protected Container.Filter filterForNumber(Object propertyId, Class<?> targetType, String pattern) {
        Container.Filter filter = numberEqual(propertyId, pattern);
        if (filter != null) return filter;

        filter = numericRange(propertyId, targetType, pattern);

        if (filter != null) return filter;

        filter = numberCompare(propertyId, targetType, pattern);

        if (filter != null) return filter;

        throw new Validator.InvalidValueException(
                String.format("'%s' is not an accepted numeric search pattern", pattern));

    }


    final String Digits     = "(\\p{Digit}+)";
    final String HexDigits  = "(\\p{XDigit}+)";
    // an exponent is 'e' or 'E' followed by an optionally
    // signed decimal integer.
    final String Exp        = "[eE][+-]?"+Digits;
    final String fpRegex    =
            ("[\\x00-\\x20]*"+  // Optional leading "whitespace"
                    "[+-]?(" + // Optional sign character
                    "NaN|" +           // "NaN" string
                    "Infinity|" +      // "Infinity" string

                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from section 3.10.2 of
                    // The Java™ Language Specification.

                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                    // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\.("+Digits+")("+Exp+")?)|"+

                    // Hexadecimal strings
                    "((" +
                    // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "(\\.)?)|" +

                    // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                    ")[pP][+-]?" + Digits + "))" +
                    "[fFdD]?))" +
                    "[\\x00-\\x20]*");// Optional trailing "whitespace"

    protected final Pattern fpPattern = Pattern.compile(fpRegex);
    protected final Pattern numberComparePattern = Pattern.compile("^((?:<|>)=?)(" + fpRegex + ")$");
    protected Container.Filter numberCompare(Object propertyId, Class<?> numberType, String pattern) {
        Matcher matcher = numberComparePattern.matcher(pattern);
        if (!matcher.find()) return null;

        String op = matcher.group(1);

        Number numberValue = parseNumericValue(matcher.group(2), numberType);

        switch (op) {
            case ">": return new Compare.Greater(propertyId, numberValue);
            case ">=": return new Compare.GreaterOrEqual(propertyId, numberValue);
            case "<": return new Compare.Less(propertyId, numberValue);
            case "<=": return new Compare.LessOrEqual(propertyId, numberValue);
        }

        return null;
    }

    private Number parseNumericValue(String numericString, Class<?> numberType) {
        try {

            if (numberType == BigDecimal.class) {
                return new BigDecimal(numericString);
            } else if (numberType == BigInteger.class) {
                return new BigInteger(numericString);
            }

            // otherwise

            Double value = Double.parseDouble(numericString);

            if (numberType == double.class || numberType == Double.class) {
                return value;
            } else if (numberType == int.class || numberType == Integer.class) {
                return value.intValue();
            } else if (numberType == long.class || numberType == Long.class) {
                return value.longValue();
            } else if (numberType == byte.class || numberType == Byte.class) {
                return value.byteValue();
            } else if (numberType == float.class || numberType == Float.class) {
                return value.floatValue();
            } else return null;

        } catch(NumberFormatException ex) {
            return null;
        }
    }

    protected Container.Filter numberEqual(Object propertyId, String pattern) {
        try {
            int i = Integer.parseInt(pattern);
            return new Compare.Equal(propertyId, i);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    protected final Pattern numericRangePattern = Pattern.compile("^(.+)\\.\\.(.+)$");
    protected Container.Filter numericRange(Object propertyId, Class<?> numberClass,
    String pattern) {
        Matcher matcher = numericRangePattern.matcher(pattern);
        if (!matcher.find()) return null;
        String left = matcher.group(1);
        String right = matcher.group(2);

        if (fpPattern.matcher(left).matches() && fpPattern.matcher(right).matches()) {

            Number i = parseNumericValue(left, numberClass);
            Number j = parseNumericValue(right, numberClass);

            if (i.doubleValue() > j.doubleValue()) {
                throw new Validator.InvalidValueException(
                        String.format("The given range '%s' is invalid", pattern));
            }

            return new And(
                    new Compare.GreaterOrEqual(propertyId, i),
                    new Compare.LessOrEqual(propertyId, j)
            );
        }

        return null;
    }


    // experimental date range filters.
    protected final Pattern comparePattern = Pattern.compile("^((?:<|>)=?)(.+)");
    protected Container.Filter filterForCompareDate(Object propertyId, String pattern) {
        Matcher matcher = comparePattern.matcher(pattern);
        if (!matcher.find())
            return null;
        String op = matcher.group(1);
        String rest = matcher.group(2);

        Date value = leftRange(rest);

        switch (op) {
            case ">": return new Compare.Greater(propertyId, value);
            case ">=": return new Compare.GreaterOrEqual(propertyId, value);
            case "<": return new Compare.Less(propertyId, value);
            case "<=": return new Compare.LessOrEqual(propertyId, value);
        }

        return null;

    }

    protected Container.Filter filterForDateRange(Object propertyId, String pattern) {
        return new And(new Compare.GreaterOrEqual(propertyId, leftRange(pattern)),
                new Compare.LessOrEqual(propertyId, rightRange(pattern)));
    }

    protected final static String dateRangeSepRegex = "\\.\\.";
    protected final static Pattern dateSepPattern = Pattern.compile("[/.-]");

    // dd..dd
    // dd-mm..dd-mm
    // dd-mm-YYYY..dd-mm-YYYY
    protected final static Pattern fullDatePattern =
            Pattern.compile(
                "^(\\d\\d)(?:(" + dateSepPattern.pattern() + ")(\\d\\d)(?:\\2(\\d\\d\\d\\d))?)?"
            );
    protected final static Pattern monthDatePattern =
            Pattern.compile("(\\d\\d)(?:"+dateSepPattern+")((\\d\\d)?\\d\\d)");
    protected final static Pattern yearPattern = Pattern.compile("\\d{4}");


    private static enum RangeEndpoint {
        Min, Max;
    }

    protected Date leftRange(String pattern) {
        String[] range = pattern.split(dateRangeSepRegex);
        return stringToDate(range[0], RangeEndpoint.Min);
    }

    protected Date rightRange(String pattern) {
        String[] range = pattern.split(dateRangeSepRegex);
        String value = (range.length == 1)? range[0] : range[1];
        return stringToDate(value, RangeEndpoint.Max);
    }

    private static Date stringToDate(String pattern, RangeEndpoint rangeEndpoint) {
        Matcher m = fullDatePattern.matcher(pattern);
        if (m.matches()) return fullDatePatternToDate(pattern, rangeEndpoint);

        m = monthDatePattern.matcher(pattern);
        if (m.matches()) return monthDatePatternToDate(pattern, rangeEndpoint);

        m = yearPattern.matcher(pattern);
        if (m.matches()) return yearToDate(pattern, rangeEndpoint);

        return null;
    }


    private static Date fullDatePatternToDate(String pattern, RangeEndpoint rangeEndpoint) {
        DateTime t = DateTime.now();
        int currentYear = t.getYear();
        int currentMonth = t.getMonthOfYear();

        /*
         * Try to parse a date in format
         *          dd
         *          dd-mm
         *          dd-mm-YYYY
         *
         */

        String[] patternComponents = pattern.split(dateSepPattern.pattern());
        int day, month, year;

        // first element is day
        day = Integer.parseInt(patternComponents[0]);
        month = currentMonth;
        year = currentYear;

        // format is dd-mm
        if (patternComponents.length >= 2) {
            month = Integer.parseInt(patternComponents[1]);
        }
        // format is dd-mm-YYYY
        if (patternComponents.length == 3) {
            year = Integer.parseInt(patternComponents[2]);
        }

        DateTime dt = null;

        switch (rangeEndpoint) {
            case Min:
                dt = new DateTime(year, month, day, 0, 0, 0, 0); break;
            case Max:
                dt = new DateTime(year, month, day, 23, 59, 59, 999); break;
        }

        return dt.toDate();

    }

    private static Date monthDatePatternToDate(String pattern, RangeEndpoint rangeEndpoint) {
        String[] patternComponents = pattern.split(dateSepPattern.pattern());
        int y = Integer.parseInt(patternComponents[1]);
        int mm = Integer.parseInt(patternComponents[0]);

        DateTime dt = new DateTime(y, mm, 1, 0, 0);
        return rangeEndpoint == RangeEndpoint.Min ? dt.toDate()
            : new DateTime(y, mm, dt.dayOfMonth().getMaximumValue(), 23, 59, 59, 999).toDate();
    }


    private static Date yearToDate(String pattern, RangeEndpoint rangeEndpoint) {
        int y = Integer.parseInt(pattern);
        return (rangeEndpoint == RangeEndpoint.Min) ?
             new DateTime(y, 1, 1, 0, 0).toDate()
             : new DateTime(y, 12, 31, 23, 59, 59, 999).toDate();

    }
}
