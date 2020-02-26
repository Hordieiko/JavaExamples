package com.hord.inheritance;

public class Child extends Parent {

    public Child(String name) {
        super(name);
        int aPrivate = new Parent(name).new Nested().getPrivate();
    }

    public interface Animal {
        default public String identifyMyself() {
            return "I am an animal.";
        }
    }
    public interface EggLayer {
        default public String identifyMyself() {
            return "I am able to lay eggs.";
        }
    }
    public interface FireBreather extends Animal { }

    public class Dragon implements Animal,EggLayer {
        @Override
        public String identifyMyself() {
            return EggLayer.super.identifyMyself();
        }
    }

    public static void main (String... args) {
        Dragon myApp = new Child(args.toString()).new Dragon();
        System.out.println(myApp.identifyMyself());
    }

    @Override
    public void testThis() {
        super.testThis();
    }

    @Override
    public String toString() {
        String child = "Child " + this.getName();
        System.out.println(child);
        return child;
    }
}
