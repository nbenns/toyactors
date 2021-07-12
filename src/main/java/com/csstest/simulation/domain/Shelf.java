package com.csstest.simulation.domain;

import lombok.val;

import java.util.*;

public class Shelf {
    public final Optional<Temperature> temperatureFilter;
    public final int capacity;

    private final Map<String, Order> storedOrders = new HashMap<>();

    private final Random rand = new Random();

    public String getName() {
        return temperatureFilter.map(Temperature::toString).orElse("Overflow");
    }

    public Shelf(Optional<Temperature> temperatureFilter, int capacity) {
        this.temperatureFilter = temperatureFilter;
        this.capacity = capacity;
    }

    public boolean isEmpty() {
        return storedOrders.size() == 0;
    }

    public boolean hasSpace() {
        return storedOrders.size() < capacity;
    }

    public boolean hasOrder(String orderId) {
        return storedOrders.containsKey(orderId);
    }

    public Optional<Order> firstOrderOfTemp(Temperature temp) {
        return storedOrders
                .values()
                .stream()
                .filter(o -> o.temp == temp)
                .findFirst();
    }

    public Order dropAtRandom() {
        val currentSize = this.storedOrders.size();
        val dropIndex = rand.nextInt(currentSize);
        Order[] orderArray = new Order[currentSize];

        val orderToDrop = storedOrders.values().toArray(orderArray)[dropIndex];

        return storedOrders.remove(orderToDrop.id);
    }

    public boolean addOrder(Order order) {
        val noRoom = !hasSpace();
        val alreadyHasOrder = hasOrder(order.id);

        if (noRoom || alreadyHasOrder) {
            return false;
        } else {
            this.storedOrders.put(order.id, order);
            return true;
        }
    }

    public Order removeOrder(String orderId) {
        return storedOrders.remove(orderId);
    }

    public Boolean isOrderOnShelf(String id) {
        return storedOrders.containsKey(id);
    }

    public String toString() {
        val orders = String.join(", ", this.storedOrders.keySet());
        return "Shelf(" + getName() + "): size " + this.storedOrders.size() + ", Orders: " + orders;
    }
}
