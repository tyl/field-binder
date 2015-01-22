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
        if (pattern == null) return null;

        if (String.class.isAssignableFrom(targetType) || java.lang.Enum.class.isAssignableFrom(targetType)) {
            return filterForString(targetPropertyId, pattern.toString());
        } else if (Number.class.isAssignableFrom(targetType)) {
            return filterForNumber(targetPropertyId, pattern.toString());
        } else if (Date.class.isAssignableFrom(targetType) || DateTime.class.isAssignableFrom(targetType)) {
            Container.Filter filter = filterForCompareDate(targetPropertyId, pattern.toString());
            if (filter != null) return filter;
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

    protected final Pattern intRangePattern = Pattern.compile("^(\\d+)..(\\d+)$");
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
