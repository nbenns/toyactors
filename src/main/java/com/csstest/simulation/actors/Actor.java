package com.csstest.simulation.actors;

import com.csstest.simulation.actors.shelfmanager.SHCommand;
import com.csstest.simulation.actors.shelfmanager.SHProps;
import com.csstest.simulation.actors.shelfmanager.ShelfHandler;
import lombok.Getter;
import lombok.val;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

public abstract class Actor<Command, Props> {
    @Getter(lazy=true)
    private final ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();
    private final Thread thread;
    protected Props props;

    public Actor() {
        thread = new Thread(this::run);
    }

    protected void onStart(Props props) {};
    protected void onComplete() {};
    protected abstract boolean isComplete();

    protected abstract void receiveMessage(Command msg);

    private void run() {
        onStart(props);

        val queue = getCommandQueue();

        while(!isComplete()) {
            val msg = Optional.ofNullable(queue.poll());
            msg.ifPresent(this::receiveMessage);
        }

        onComplete();
    }

    public final Thread start(String name, Props props) {
        this.props = props;
        thread.setName(name);
        thread.start();
        return thread;
    }
}
