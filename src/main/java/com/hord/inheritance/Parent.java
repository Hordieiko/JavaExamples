package com.hord.inheritance;

import java.util.Objects;

public class Parent {

    private String name;

    private int i = 10;

    public Parent(String name) {
        this.name = name;
    }

    public class Nested {
        public int getPrivate() {
            return i;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void testThis() {
        this.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return Objects.equals(name, parent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        System.out.println("Parent");
        return "Parent{" +
                "name='" + name + '\'' +
                '}';
    }
}
