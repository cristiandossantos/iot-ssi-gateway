/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.model;



import java.sql.Timestamp;

/**
 * Model class representing a device.
 * 
 * @author 07429377980
 */
public class IotDevice {
    private String device_id;
    private String DID;
    private String emission_did;
    private String Operator;

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String Operator) {
        this.Operator = Operator;
    }

    public String getEmission_did() {
        return emission_did;
    }

    public void setEmission_did(String emission_did) {
        this.emission_did = emission_did;
    }
    private String device_type;
    private String device_hash;
    private String location;
    private String data;
    private Timestamp timestamp;

    
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_hash() {
        return device_hash;
    }

    public void setDevice_hash(String device_hash) {
        this.device_hash = device_hash;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    

    
}