package org.tylproject.vaadin.addon.fieldbinder.strategies;

import com.vaadin.data.Container;
import com.vaadin.data.Validator;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by evacchi on 05/12/14.
 */
public class DefaultFilterFactory implements FilterFactory {

    @Override
    public Container.Filter createFilter(Class<?> targetType, Object targetPropertyId, Object pattern) {
            if (String.class.isAssignableFrom(targetType)) {
            return new SimpleStringFilter(targetPropertyId, pattern.toString(), true, true);
        } else
        if (Number.class.isAssignableFrom(targetType)) {
            return filterForNumber(targetPropertyId, pattern.toString());
        } else {
            throw new UnsupportedOperationException("Unsupported value type: "+targetType.getCanonicalName());
        }
    }


    private Container.Filter filterForNumber(Object propertyId, String pattern) {
        Container.Filter filter = numberEqual(propertyId, pattern);
        if (filter != null) return filter;

        filter = intRange(propertyId, pattern);

        if (filter != null) return filter;

        filter = intCompare(propertyId, pattern);

        if (filter != null) return filter;

        throw new Validator.InvalidValueException(
                String.format("'%s' is not an accepted numeric search pattern", pattern));

    }


    private final Pattern intComparePattern = Pattern.compile("^((?:<|>)=?)(\\d+)$");
    private Container.Filter intCompare(Object propertyId, String pattern) {
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

    private Container.Filter numberEqual(Object propertyId, String pattern) {
        try {
            int i = Integer.parseInt(pattern);
            return new Compare.Equal(propertyId, i);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private final Pattern intRangePattern = Pattern.compile("^(\\d+)-(\\d+)$");
    private Container.Filter intRange(Object propertyId, String pattern) {
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
}
