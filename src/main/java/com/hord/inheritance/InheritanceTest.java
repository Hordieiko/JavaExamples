package com.hord.inheritance;

public class InheritanceTest {

    public static void main(String[] args) {
        InheritanceTest main = new InheritanceTest();
        Object objectC = main.getObjectC();
        if (objectC instanceof C)
            System.out.println("instanceof C");

        A castToA = (A) objectC;
        String display = castToA.getDisplay();
        System.out.println("Display: " + display);
    }

    Object getObjectC() {
        return new C();
    }
}

class C extends B {

}

class B extends A {
    @Override
    public String getDisplay() {
        return "B";
    }
}

class A {
    public String getDisplay() {
        return "A";
    }
}
