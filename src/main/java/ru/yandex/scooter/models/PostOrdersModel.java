package ru.yandex.scooter.models;

public class PostOrdersModel {

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation; //расхождение в API-doc: у поля в тексте указан тип String, в примере запроса - int
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public PostOrdersModel(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

}