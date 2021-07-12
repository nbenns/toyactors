package com.csstest.simulation.actors.couriermanager;

import com.csstest.simulation.actors.shelfmanager.SHCommand;
import lombok.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@Value
@AllArgsConstructor
@Getter(AccessLevel.NONE)
@Setter(AccessLevel.NONE)
@ToString(includeFieldNames = true)
public class CMProps {
    public ConcurrentLinkedQueue<SHCommand> smQueue;
}
