package com.csstest.simulation.domain;

import lombok.*;

import java.util.Optional;

@Value
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@ToString(includeFieldNames = true)
public class ShelfLayout {
    public int hotCapacity;
    public int coldCapacity;
    public int frozenCapacity;
    public int overflowCapacity;

    public ShelfLayout(Integer hotCapacity, Integer coldCapacity, Integer frozenCapacity, Integer overflowCapacity) {
        this.hotCapacity = Optional.ofNullable(hotCapacity).orElse(10);
        this.coldCapacity = Optional.ofNullable(coldCapacity).orElse(10);
        this.frozenCapacity = Optional.ofNullable(frozenCapacity).orElse(10);
        this.overflowCapacity = Optional.ofNullable(overflowCapacity).orElse(15);
    }

    public ShelfLayout() {
        hotCapacity = 10;
        coldCapacity = 10;
        frozenCapacity = 10;
        overflowCapacity = 15;
    }
}
