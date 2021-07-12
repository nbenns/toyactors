package com.csstest.simulation.actors.couriermanager;

import com.csstest.simulation.actors.Actor;
import com.csstest.simulation.actors.courier.CProps;
import com.csstest.simulation.actors.courier.Courier;
import com.csstest.simulation.actors.shelfmanager.SHCommand;
import lombok.val;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CourierManager extends Actor<CMCommand, CMProps> {
    private ConcurrentLinkedQueue<SHCommand> smQueue;

    private boolean moreOrders = true;
    private final Random rand = new Random();

    private CourierManager() {
        super();
    }

    public static Actor<CMCommand, CMProps> make() {
        return new CourierManager();
    }

    private int generateStartDelay() {
        return rand.nextInt(5) + 2;
    }

    @Override
    protected void onStart(CMProps cmProps) {
        this.smQueue = cmProps.smQueue;
    }

    @Override
    protected boolean isComplete() {
        return !moreOrders;
    }

    @Override
    protected void onComplete() {
        System.out.println("CourierManager: All Orders picked up, exiting.");
    }

    @Override
    protected void receiveMessage(CMCommand msg) {
        if (msg instanceof CMCommand.ReadyForDelivery) {
            val pickupOrder = (CMCommand.ReadyForDelivery) msg;
            val orderId = pickupOrder.orderId;
            val startDelay = generateStartDelay();
            val cProps = new CProps(smQueue, orderId, startDelay, 30000L);
            val courier = Courier.make();

            System.out.println("CourierManager: Adding courier for Order " + orderId + " arriving in " + startDelay + " seconds");
            courier.start("Courier-" + orderId, cProps);
        }
        else if (msg instanceof CMCommand.AllOrdersDelivered) {
            moreOrders = false;
        }
    }
}
