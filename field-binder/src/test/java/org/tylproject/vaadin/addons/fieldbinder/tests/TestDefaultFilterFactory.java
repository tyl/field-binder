package org.tylproject.vaadin.addons.fieldbinder.tests;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.tylproject.vaadin.addon.fieldbinder.behavior.DefaultFilterFactory;

import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by evacchi on 22/01/15.
 */
public class TestDefaultFilterFactory {
    DefaultFilterFactory filterFactory = new DefaultFilterFactory();
    Logger log = Logger.getAnonymousLogger();

    private static final String DATE_PROPID = "date";

    @Test
    public void testDateYear() {
        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2014, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2014, 12, 31, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "2014");
        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }


    @Test
    public void testDateYearRange() {
        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2015, 12, 31, 23, 59, 59, 999).toDate()));
        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "2010..2015");

        assertEquals(expected, filter);
    }

    @Test
    public void testDateMonth() {
        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2010, 1, 31, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-2010");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);

    }

    @Test
    public void testDateMonthRange() {
        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2015, 10, 31, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-2010..10-2015");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);

    }

    @Test
    public void testDate_dd_mm_yyyy() {

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-01-2010");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }

    @Test
    public void testDate_dd_mm_yyyy_Range() {

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(2010, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2010, 1, 31, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-01-2010..31-01-2010");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }

    @Test
    public void testDate_dd_mm() {

        DateTime today = DateTime.now();
        int year = today.getYear();

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(year, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(year, 1, 1, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-01");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }


    @Test
    public void testDate_dd_mm_Range() {

        DateTime today = DateTime.now();
        int year = today.getYear();

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(year, 1, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(year, 1, 31, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01-01..31-01");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }


    @Test
    public void testDate_dd() {

        DateTime today = DateTime.now();
        int year = today.getYear(), month = today.getMonthOfYear();

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(year, month, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(year, month, 1, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01");


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }


    @Test
    public void testDate_dd_Range() {

        DateTime today = DateTime.now();
        int year = today.getYear(), month = today.getMonthOfYear();
        int maxDay = today.dayOfMonth().getMaximumValue();

        Container.Filter expected = new And(
                new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(year, month, 1, 0, 0, 0).toDate()),
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(year, month, maxDay, 23, 59, 59, 999).toDate()));

        Container.Filter filter = filterFactory.createFilter(Date.class, DATE_PROPID, "01.." + maxDay);


        log.info(filterToString(expected));
        log.info(filterToString(filter));
        assertEquals(expected, filter);
    }

    @Test
    public void testDate_compare() {
        DateTime today = DateTime.now();
        int year = today.getYear(), month = today.getMonthOfYear();
        int maxDay = today.dayOfMonth().getMaximumValue();

        Container.Filter filter, expected;

        filter = filterFactory.createFilter(Date.class, DATE_PROPID, ">=01");
        expected =
            new Compare.GreaterOrEqual(DATE_PROPID, new DateTime(year, month, 1, 0, 0, 0).toDate());

        assertEquals(expected, filter);


        filter = filterFactory.createFilter(Date.class, DATE_PROPID, ">01-10");
        expected =
                new Compare.Greater(DATE_PROPID, new DateTime(year, 10, 1, 0, 0, 0).toDate());

        assertEquals(expected, filter);


        filter = filterFactory.createFilter(Date.class, DATE_PROPID, "<=11-2010");
        expected =
                new Compare.LessOrEqual(DATE_PROPID, new DateTime(2010, 11, 1, 0, 0, 0).toDate());

        assertEquals(expected, filter);

        filter = filterFactory.createFilter(Date.class, DATE_PROPID, "<2014");
        expected =
                new Compare.Less(DATE_PROPID, new DateTime(2014, 1, 1, 0, 0, 0).toDate());

        assertEquals(expected, filter);


        filter = filterFactory.createFilter(Date.class, DATE_PROPID, "<11-11-2014");
        expected =
                new Compare.Less(DATE_PROPID, new DateTime(2014, 11, 11, 0, 0, 0).toDate());

        assertEquals(expected, filter);
    }




    public String filterToString(Container.Filter filter) {
        if (filter instanceof Compare.LessOrEqual) {
            return String.format("%s <= %s", ((Compare.LessOrEqual) filter).getPropertyId(), ((Compare.LessOrEqual) filter).getValue());
        } else
        if (filter instanceof Compare.GreaterOrEqual) {
            return String.format("%s >= %s", ((Compare.GreaterOrEqual) filter).getPropertyId(), ((Compare.GreaterOrEqual) filter).getValue());
        } else
        if (filter instanceof And) {
            StringBuffer sb = new StringBuffer();
            Collection<Container.Filter> coll = ((And) filter).getFilters();
            for (Container.Filter f: coll) {
                sb.append(filterToString(f));
                sb.append(", ");
            }
            return sb.toString();
        }

        throw new UnsupportedOperationException("unsupported filter");
    }

}
