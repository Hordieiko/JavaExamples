package com.hord.clmExamples;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

    private Set<String> roles;

    private long id;

    private String firstName;

    private String lastName;

    public Set<String> getRoles() {
        return roles;
    }

    public void addRole(String role) {
        if (Objects.isNull(this.roles)) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public void addRoles(Set roles) {
        if (Objects.isNull(this.roles)) {
            this.roles = new HashSet<>();
        }
        this.roles.addAll(roles);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "roles=" + roles +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
