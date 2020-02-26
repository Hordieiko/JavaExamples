package com.hord.composition;

public class Car {

    String name;

    Engine engine;

    public Car(String carName, String engineModelName) {
        this.name = carName;
        this.engine = new Engine();
        this.engine.setModel(engineModelName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Engine getEngine() {
        return engine;
    }
}
