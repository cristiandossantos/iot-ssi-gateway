/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.repository;



import com.ufsc.lrg.iot.ssi.gateway.controller.Controller;
import com.ufsc.lrg.iot.ssi.gateway.controller.HashGenerator;
import com.ufsc.lrg.iot.ssi.gateway.indySdk.DIDController;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;

/**
 * Utility class for processing MQTT data and registering it in MongoDB.
 */
public class DbUtils {

    private DataStorage mds = new DataStorage();
    Controller controller;
    /**
     * Registers MQTT device data in MongoDB.
     *
     * @param topic   The MQTT topic of the message.
     * @param message The received MQTT message.
     */
    public void RegisterDataDevice(String topic, MqttMessage message) {
        System.out.println("message content: " + new String(message.getPayload()));

        // Split the message into parts
        String[] messageParts = new String(message.getPayload()).split("/");

        // Extract information from the message
        String device_id = messageParts[0];
        String device_type = messageParts[1];
        String location = messageParts[2];
        String data = messageParts[3];

        // Get the current date and time
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String combinedString = device_id + device_type + location + data + timestamp;
        // Generate a hash for the device_id (example: implementation required)
        String hash_data = HashGenerator.generateHash(combinedString);

        try {
            String did_evidence = DIDController.createDIDfromEvidence(hash_data);

            // Create a document for MongoDB
            Document document = new Document();
            document.append("device_name", device_id);
            document.append("did_evidence", did_evidence);
            //document.append("did_device", GetDeviceById(device_id));
            document.append("hash_data", hash_data);
            document.append("device_type", device_type);
            document.append("data", data);
            document.append("location", location);
            document.append("timestamp", timestamp);

            // Store the document in MongoDB
            mds.createDeviceData(document);
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }

        // Print information for debugging purposes
        System.out.println("type: " + device_type);
        System.out.println("mac: " + device_id);
        System.out.println("data: " + data);
        System.out.println("location: " + location);
        System.out.println("time: " + timestamp);
        System.out.println("topic: " + topic);
    }

    
}