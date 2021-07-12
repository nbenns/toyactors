package com.csstest.simulation.actors.ordersubmitter;

import com.csstest.simulation.actors.shelfmanager.SHCommand;
import com.csstest.simulation.domain.Order;
import lombok.*;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Value
@AllArgsConstructor
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@ToString(includeFieldNames = true)
public class OSProps {
    public List<Order> orders;
    public ConcurrentLinkedQueue<SHCommand> queue;
    public long perSecondRate;
}
