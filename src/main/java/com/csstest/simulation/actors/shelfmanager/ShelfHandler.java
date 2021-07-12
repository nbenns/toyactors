package com.csstest.simulation.actors.shelfmanager;

import com.csstest.simulation.actors.Actor;
import com.csstest.simulation.actors.courier.CCommand;
import com.csstest.simulation.actors.couriermanager.CMCommand;
import com.csstest.simulation.domain.Order;
import com.csstest.simulation.domain.Shelf;
import com.csstest.simulation.domain.ShelfLayout;
import com.csstest.simulation.domain.Temperature;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShelfHandler extends Actor<SHCommand, SHProps> {
    private final Map<Temperature, Shelf> mainShelves = new HashMap<>();
    private Shelf overflowShelf;

    private ConcurrentLinkedQueue<CMCommand> courierMgrQueue;

    private boolean ordersRemaining = true;

    private ShelfHandler() {
        super();
    }

    public static Actor<SHCommand, SHProps> make() {
        return new ShelfHandler();
    }

    protected Stream<Shelf> allShelves() {
        val shelfList = new ArrayList<>(mainShelves.values());
        shelfList.add(overflowShelf);

        return shelfList.stream();
    }

    protected void moveOrderToAnotherShelf(String orderId, Shelf origin, Shelf destination) {
        val order = origin.removeOrder(orderId);
        destination.addOrder(order);
    }

    protected Optional<Order> findMovableOrder() {
        return mainShelves
                .values()
                .stream()
                .filter(Shelf::hasSpace)
                .map(shelf ->
                        shelf
                                .temperatureFilter
                                .flatMap(overflowShelf::firstOrderOfTemp)
                )
                .dropWhile(Optional::isEmpty)
                .limit(1)
                .collect(toOptional())
                .flatMap(Function.identity());
    }

    protected boolean addOrderToShelf(Order order) {
        val matchingShelf = mainShelves.get(order.temp);

        if (matchingShelf.hasSpace()) {
            System.out.println("ShelfManager: Adding Order " + order.id + " to " + matchingShelf.getName() + " Shelf");
            return matchingShelf.addOrder(order);
        }
        else {
            if (!overflowShelf.hasSpace()) {
                findMovableOrder()
                        .ifPresentOrElse(
                                movableOrder -> {
                                    val destShelf = mainShelves.get(movableOrder.temp);
                                    moveOrderToAnotherShelf(movableOrder.id, overflowShelf, destShelf);
                                    System.out.println("ShelfManager: Moved Order " + movableOrder.id + " from Overflow to " + destShelf.getName());
                                },
                                () -> {
                                    val droppedOrder = overflowShelf.dropAtRandom();
                                    System.out.println("ShelfManager: Dropped Order " + droppedOrder.id + " from Overflow Shelf to make room for " + order.id);
                                }
                        );
            }

            System.out.println("ShelfManager: Adding Order " + order.id + " to Overflow Shelf");
            return overflowShelf.addOrder(order);
        }
    }

    private static <T> Collector<T, ?, Optional<T>> toOptional() {
        return Collectors
                .collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            if (list.size() >= 1) return Optional.of(list.get(0));
                            else return Optional.empty();
                        }
                );
    }

    protected Optional<Order> findOrderById(String orderId) {
        return allShelves()
                .filter(shelf -> shelf.hasOrder(orderId))
                .limit(1)
                .map(shelf -> shelf.removeOrder(orderId))
                .collect(toOptional());
    }

    protected Boolean shelvesEmpty() {
        return allShelves().filter(Shelf::isEmpty).count() == allShelves().count();
    }

    @Override
    protected void onStart(SHProps props) {
        mainShelves.put(Temperature.HOT, new Shelf(Optional.of(Temperature.HOT), props.shelfLayout.hotCapacity));
        mainShelves.put(Temperature.COLD, new Shelf(Optional.of(Temperature.COLD), props.shelfLayout.coldCapacity));
        mainShelves.put(Temperature.FROZEN, new Shelf(Optional.of(Temperature.FROZEN), props.shelfLayout.frozenCapacity));

        overflowShelf  = new Shelf(Optional.empty(), props.shelfLayout.overflowCapacity);

        this.courierMgrQueue = props.courierMgrQueue;
    }

    @Override
    protected boolean isComplete() {
        return !ordersRemaining && shelvesEmpty();
    }

    @Override
    protected void onComplete() {
        val msg = new CMCommand.AllOrdersDelivered();
        courierMgrQueue.offer(msg);

        System.out.println("ShelfManager: All Orders picked up, notified CourierManager. Exiting.");
    }

    @Override
    protected void receiveMessage(SHCommand msg) {
        if (msg instanceof SHCommand.SubmitOrder) {
            val submitOrder = (SHCommand.SubmitOrder) msg;
            val res = addOrderToShelf(submitOrder.order);

            if (res) {
                val pickupMsg = new CMCommand.ReadyForDelivery(submitOrder.order.id);
                courierMgrQueue.offer(pickupMsg);
            }
            else {
                System.out.println("ShelfManager: Couldn't add Order " + submitOrder.order.id + " to any shelf.");
            }
        }
        else if (msg instanceof SHCommand.OrdersCompleted) {
            ordersRemaining = false;
            System.out.println("ShelfManager: Notified no Orders are remaining.");
        }
        else if (msg instanceof SHCommand.PickingUpOrder) {
            val pickingUp = (SHCommand.PickingUpOrder) msg;
            val orderId = pickingUp.orderId;

            val possibleOrder = findOrderById(orderId);
            possibleOrder.ifPresentOrElse(
                    order -> {
                        val res = new CCommand.HandingOverOrder(order);
                        pickingUp.courierQueue.offer(res);
                        System.out.println("ShelfManager: Sending Order " + orderId + " to Courier");
                    },
                    () -> {
                        val res = new CCommand.NoOrderFound();
                        pickingUp.courierQueue.offer(res);
                        System.out.println("ShelfManager: Requested Order from Courier not found: " + orderId);
                    }
            );
        }
        else {
            System.out.println("ShelfManager: Unknown command: " + msg);
        }
    }
}
