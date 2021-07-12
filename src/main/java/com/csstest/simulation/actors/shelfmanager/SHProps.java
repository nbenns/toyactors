package com.csstest.simulation.actors.shelfmanager;

import com.csstest.simulation.actors.couriermanager.CMCommand;
import com.csstest.simulation.domain.ShelfLayout;
import lombok.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Value
@AllArgsConstructor
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@ToString(includeFieldNames = true)
public class SHProps {
    public ShelfLayout shelfLayout;
    public ConcurrentLinkedQueue<CMCommand> courierMgrQueue;
}
