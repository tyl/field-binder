package org.tylproject.vaadin.addon.masterdetail.strategies;

import java.sql.SQLException;

/**
 * Created by evacchi on 24/11/14.
 */
public class MasterDetailStrategyInstantiationException extends RuntimeException {
    public MasterDetailStrategyInstantiationException(SQLException cause) {
        super(cause);
    }
}
