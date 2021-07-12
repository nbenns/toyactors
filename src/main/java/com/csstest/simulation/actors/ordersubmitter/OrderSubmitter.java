package com.csstest.simulation.actors.ordersubmitter;

import com.csstest.simulation.Utils;
import com.csstest.simulation.actors.Actor;
import com.csstest.simulation.actors.shelfmanager.SHCommand;
import lombok.val;

public class OrderSubmitter extends Actor<OSCommand, OSProps> {
    private OrderSubmitter() {
        super();
    }

    public static Actor<OSCommand, OSProps> make() {
        return new OrderSubmitter();
    }

    @Override
    protected void onStart(OSProps osProps) {
        val orders = osProps.orders;
        val queue = osProps.queue;
        val delay = 1000 / osProps.perSecondRate;

        while (orders.size() > 0) {
            Utils.sleep(delay);

            val order = orders.get(0);

            val msg = new SHCommand.SubmitOrder(order);

            queue.offer(msg);
            orders.remove(0);
            System.out.println("OrderSubmitter: Submitted Order: " + order.id);

        }

        val msg = new SHCommand.OrdersCompleted();
        queue.offer(msg);
        System.out.println("OrderSubmitter: All Orders submitted, exiting.");
    }

    @Override
    protected boolean isComplete() {
        return true;
    }

    @Override
    protected void receiveMessage(OSCommand msg) {

    }
}
