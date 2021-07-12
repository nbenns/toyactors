package com.csstest.simulation.actors.courier;

import com.csstest.simulation.Utils;
import com.csstest.simulation.actors.Actor;
import com.csstest.simulation.actors.couriermanager.CMCommand;
import com.csstest.simulation.actors.couriermanager.CMProps;
import com.csstest.simulation.actors.couriermanager.CourierManager;
import com.csstest.simulation.actors.shelfmanager.SHCommand;
import lombok.val;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Courier extends Actor<CCommand, CProps> {
    private ConcurrentLinkedQueue<SHCommand> smQueue;
    private String orderId;
    private long startDelaySeconds;
    private long timeout;
    private Instant startTime;
    private boolean delivered = false;

    private Courier() {
        super();
    }

    public static Actor<CCommand, CProps> make() {
        return new Courier();
    }

    protected void onStart(CProps props) {
        this.smQueue = props.smQueue;
        this.orderId = props.orderId;
        this.startDelaySeconds = props.startDelaySeconds;
        this.timeout = props.timeout;

        System.out.println("Courier: Traveling to destination for " + startDelaySeconds + " seconds.");
        Utils.sleep(startDelaySeconds * 1000);

        val msg = new SHCommand.PickingUpOrder(orderId, getCommandQueue());
        smQueue.offer(msg);

        startTime = Instant.now();
    }

    protected void onComplete() {
        System.out.println("Courier: Exited.");
    }

    protected boolean isComplete() {
        return (startTime.compareTo(Instant.now()) >= timeout) || delivered;
    }

    protected void receiveMessage(CCommand msg) {
        if (msg instanceof CCommand.HandingOverOrder) {
            val sendingOrder = (CCommand.HandingOverOrder) msg;

            if (sendingOrder.order.id.equals(orderId)) {
                System.out.println("Courier: Delivering Order: " + sendingOrder.order);
            }
            else {
                System.out.println("Courier: Received wrong Order: " + sendingOrder.order);
            }

            delivered = true;
        }
        else if (msg instanceof CCommand.NoOrderFound) {
            System.out.println("Courier: Delivering Order failed: " + orderId);
            delivered = true;
        }
        else {
            System.out.println("Courier: Received Unknown Message: " + msg);
        }
    }
}
