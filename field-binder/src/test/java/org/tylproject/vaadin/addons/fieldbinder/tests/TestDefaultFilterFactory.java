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
