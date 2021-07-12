package com.csstest.simulation.actors.courier;

import com.csstest.simulation.actors.shelfmanager.SHCommand;
import lombok.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Value
@AllArgsConstructor
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
public class CProps {
    public ConcurrentLinkedQueue<SHCommand> smQueue;
    public String orderId;
    public long startDelaySeconds;
    public long timeout;
}
