/*
 * Copyright (c) 2014 - Tyl Consulting s.a.s.
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
        if (String.class.isAssignableFrom(targetType) || java.lang.Enum.class.isAssignableFrom(targetType)) {
            return filterForString(targetPropertyId, pattern.toString());
        } else if (Number.class.isAssignableFrom(targetType)) {
            return filterForNumber(targetPropertyId, pattern.toString());
        } else if (Date.class.isAssignableFrom(targetType) || DateTime.class.isAssignableFrom(targetType)) {
            return filterForDateRange(targetPropertyId, pattern.toString());
        } else {
            // fallback to equality
            return new Compare.Equal(targetPropertyId, pattern);
        }
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

    protected Container.Filter filterForNumber(Object propertyId, String pattern) {
        Container.Filter filter = numberEqual(propertyId, pattern);
        if (filter != null) return filter;

        filter = intRange(propertyId, pattern);

        if (filter != null) return filter;

        filter = intCompare(propertyId, pattern);

        if (filter != null) return filter;

        throw new Validator.InvalidValueException(
                String.format("'%s' is not an accepted numeric search pattern", pattern));

    }


    protected final Pattern intComparePattern = Pattern.compile("^((?:<|>)=?)(\\d+)$");
    protected Container.Filter intCompare(Object propertyId, String pattern) {
        Matcher matcher = intComparePattern.matcher(pattern);
        if (!matcher.find()) return null;

        String op = matcher.group(1);
        int value = Integer.parseInt(matcher.group(2));

        switch (op) {
            case ">": return new Compare.Greater(propertyId, value);
            case ">=": return new Compare.GreaterOrEqual(propertyId, value);
            case "<": return new Compare.Less(propertyId, value);
            case "<=": return new Compare.LessOrEqual(propertyId, value);
        }

        return null;
    }

    protected Container.Filter numberEqual(Object propertyId, String pattern) {
        try {
            int i = Integer.parseInt(pattern);
            return new Compare.Equal(propertyId, i);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    protected final Pattern intRangePattern = Pattern.compile("^(\\d+)-(\\d+)$");
    protected Container.Filter intRange(Object propertyId, String pattern) {
        Matcher matcher = intRangePattern.matcher(pattern);
        if (!matcher.find()) return null;
        String left = matcher.group(1);
        String right = matcher.group(2);
        try {
            int i = Integer.parseInt(left);
            int j = Integer.parseInt(right);

            if (i>j) {
                throw new Validator.InvalidValueException(
                        String.format("The given range '%s' is invalid", pattern));
            }

            return new And(
                    new Compare.GreaterOrEqual(propertyId, i),
                    new Compare.LessOrEqual(propertyId, j)
            );

        } catch (NumberFormatException ex) {
            return null;
        }

    }


    // experimental date range filters.
    // Currently not supported nor exposed due to internal
    // limitations of the DateField (cannot access raw text value)

    protected Container.Filter filterForDateRange(Object propertyId, String pattern) {
        return new And(new Compare.GreaterOrEqual(propertyId, leftRange(pattern)),
                new Compare.LessOrEqual(propertyId, rightRange(pattern)));
    }

    protected final static String dateRangeSepRegex = "\\.\\.";
    protected final static Pattern dateSepPattern = Pattern.compile("[/.-]");
    protected final static Pattern fullDatePattern = Pattern.compile("(\\d\\d)(?:"+dateSepPattern+")(\\d\\d)(\\2((\\d\\d)?\\d\\d))?");
    protected final static Pattern monthDatePattern = Pattern.compile("(\\d\\d)(?:"+dateSepPattern+")((\\d\\d)?\\d\\d)");
    protected final static Pattern yearPattern = Pattern.compile("\\d{4}");


    private static enum RangeEndpoint {
        Min, Max;
    }

    private static class Range<T> {
        private final T left;
        private final T right;

        public Range(T left, T right) {
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public T getRight() {
            return right;
        }
    }


    protected Date leftRange(String pattern) {
        String[] range = pattern.split(dateRangeSepRegex);
        return stringToDate(range[0], RangeEndpoint.Min);
    }

    protected Date rightRange(String pattern) {
        String[] range = pattern.split(dateRangeSepRegex);
        if (range.length == 1) return stringToDate(range[0], RangeEndpoint.Max);
        return stringToDate(range[1], RangeEndpoint.Max);
    }

    private static Date stringToDate(String pattern, RangeEndpoint rangeEndpoint) {

        System.out.println(pattern);

        Matcher m = fullDatePattern.matcher(pattern);
        if (m.matches()) return fullDatePatternToDate(m, rangeEndpoint);

        m = monthDatePattern.matcher(pattern);
        if (m.matches()) return monthDatePatternToDate(m, rangeEndpoint);

        m = yearPattern.matcher(pattern);
        if (m.matches()) return yearToDate(m, rangeEndpoint);

        return null;
    }

    private static Date fullDatePatternToDate(Matcher m, RangeEndpoint rangeEndpoint) {
        DateTime t = DateTime.now();

        String maybeDay = m.group(1);
        String maybeMonth = m.group(3);
        String maybeYear = m.group(5);
        int maybeMonthVal, maybeDayVal, maybeYearVal;

        maybeDayVal = Integer.parseInt(maybeDay);
        maybeMonthVal = Integer.parseInt(maybeMonth);
        maybeYearVal = t.year().get();

        DateTime dt ;

        if (maybeYear == null) {

            // if month > 12 then it's a 2-digits year
            if (maybeMonthVal > 12) {
                // not necessarily true! depends on the locale
                maybeYearVal = maybeMonthVal ;
                maybeMonthVal  = maybeDayVal;
                maybeDayVal   = 1;

                dt = new DateTime(maybeYearVal, maybeMonthVal, maybeDayVal, 0,0);
                if (rangeEndpoint == RangeEndpoint.Max) dt = dt.dayOfMonth().withMaximumValue();
            } else {

                dt = new DateTime(maybeYearVal, maybeMonthVal, maybeDayVal, 0,0);
            }

        } else {
            maybeYearVal = Integer.parseInt(maybeYear);
            if (maybeYearVal < 100) {
                if (maybeYearVal > t.year().get()) {
                    maybeYearVal += 1900;
                } else {
                    maybeYearVal += 2000;
                }
            }

            dt = new DateTime(maybeYearVal, maybeMonthVal, maybeDayVal, 0,0);

        }



        return dt.toDate();
    }

    private static Date monthDatePatternToDate(Matcher m, RangeEndpoint rangeEndpoint) {
        int y = Integer.parseInt(m.group(2));
        int mm = Integer.parseInt(m.group(1));

        DateTime dt = new DateTime(y, mm, 1, 0, 0);
        return rangeEndpoint == RangeEndpoint.Min ? dt.toDate()
            : new DateTime(y, mm, dt.dayOfMonth().getMaximumValue(), 23, 59, 59).toDate();
    }


    private static Date yearToDate(Matcher m, RangeEndpoint rangeEndpoint) {
        int y = Integer.parseInt(m.group(0));
        if (rangeEndpoint == RangeEndpoint.Min) return new DateTime(y, 1, 1, 0, 0).toDate();
        else return new DateTime(y, 12, 31, 23, 59, 59).toDate();

    }
}
