package com.csstest.simulation.actors.shelfmanager;

import com.csstest.simulation.actors.courier.CCommand;
import com.csstest.simulation.domain.Order;
import lombok.*;

import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class SHCommand {
    /**
     * The OrderSubmitter will tell the ShelfManager "SubmitOrder" when a new Order is ready to process.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class SubmitOrder extends SHCommand {
        public Order order;
    }

    /**
     * The OrderSubmitter will tell the ShelfManager "OrdersCompleted" when it has run out of Orders to process.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class OrdersCompleted extends SHCommand { }

    /**
     * A Courier will tell the ShelfManager "PickingUpOrder" when it has arrived to pick up an Order for delivery.
     */
    @Value
    @AllArgsConstructor
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString(includeFieldNames = true)
    public static class PickingUpOrder extends SHCommand {
        public String orderId;
        public ConcurrentLinkedQueue<CCommand> courierQueue;
    }
}
