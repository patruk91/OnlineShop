package com.codecool.model;

public class User {
    private int id;
    private String type;
    private String login;
    private String name;
    private String lastName;
    private Address addresses;

    public User(int id, String login, String type) {
        this.id = id;
        this.login = login;
        this.type = type;
    }

    public User(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public User(int id, String type, String login, String name, String lastName) {
        this(id, type);
        this.login = login;
        this.name = name;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddres() {
        return addresses;
    }

    public void setAddresses(Address addresses) {
        this.addresses = addresses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
