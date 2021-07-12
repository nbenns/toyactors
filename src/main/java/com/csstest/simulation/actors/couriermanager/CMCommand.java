package com.csstest.simulation.actors.couriermanager;

import lombok.*;

public abstract class CMCommand {
    /**
     * The ShelfManager will tell the CourierManager "ReadyForDelivery" when a new Order is ready to be delivered.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class ReadyForDelivery extends CMCommand {
        public String orderId;
    }

    /**
     * The ShelfManager will tell the CourierManager "AllOrdersDelivered" when all orders have been processed
     * from the OrderSubmitter and the shelves are empty.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class AllOrdersDelivered extends CMCommand { }
}
