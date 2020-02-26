package com.hord.composition;

public class MainTest {

    public static void main(String[] args) {
        Car car1 = new Car("Car1", "1234");
        Engine engine = car1.getEngine();
        engine.setModel("5678");

        System.out.println(car1.getEngine().getModel());
    }
}
