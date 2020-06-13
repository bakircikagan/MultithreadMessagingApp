package network;

import architecture.Observer;
import architecture.Subject;
import utilities.Utilities;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkNode implements Subject {

    private AtomicBoolean alive;
    private Set<Observer> observers;
    public static final int PORT = 4352;
    private final String name;

    protected NetworkNode(String name) {
        Utilities.require(name != null, "Name cannot be null");
        int len = name.length();
        Utilities.require(0 < len && len < 10, "Name length must be in [1, 9]");
        this.name = name;
        alive = new AtomicBoolean(true);
        observers = new HashSet<>();
    }

    protected void kill() throws IOException {
        alive.set(false);
        notifyObservers();
    }

    public boolean isAlive() {
        return alive.get();
    }

    public String getName() {
        return name;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
    }
}
