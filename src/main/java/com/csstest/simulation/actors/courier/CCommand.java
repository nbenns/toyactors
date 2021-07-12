package com.csstest.simulation.actors.courier;

import com.csstest.simulation.domain.Order;
import lombok.*;

public abstract class CCommand {
    /**
     * The ShelfManager will tell the Courier "HandingOverOrder" when it is providing the Order for delivery.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class HandingOverOrder extends CCommand {
        public Order order;
    }

    /**
     * The ShelfManager will tell the Courier "NoOrderFound" when it cannot find the Order it wants to deliver.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class NoOrderFound extends CCommand {
    }
}
