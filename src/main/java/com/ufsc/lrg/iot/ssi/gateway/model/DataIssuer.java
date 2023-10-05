/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.model;



import com.ufsc.lrg.iot.ssi.gateway.controller.Controller;
import com.ufsc.lrg.iot.ssi.gateway.indySdk.DIDController;
import java.sql.Timestamp;
import org.bson.Document;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author 07429377980
 */
public class DataIssuer {
     Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    IotDevice device = new IotDevice();
    Controller controller;
    String did_evidence;
     String defCredId = "B3qPp5s37rQNYpWSUs9bXC:3:CL:82542:evidence2";
     String connId = "606e314e-5c22-462b-a2ec-d0878515d9d6";
    public void IoTDataCredentialIssuer(String topic, MqttMessage message) {
        System.out.println("message content: " + new String(message.getPayload()));

        // Split the message into parts
        String[] messageParts = new String(message.getPayload()).split("/");
        try {
            
        device = new IotDevice();
        controller = new Controller();
       // controller.getDid();
        // Extract information from the message
        device.setDevice_id(messageParts[0]);
        device.setDevice_type(messageParts[1]);
        device.setLocation(messageParts[2]);
        device.setData(messageParts[3]);
        device.setTimestamp(timestamp);
        device.setDID(controller.GetDeviceById(device.getDevice_id()));
        device.setEmission_did(DIDController.createDIDfromEvidence(device.getDID()));
        device.setOperator("LRG UFSC");
        controller.IssueCredentialIoT(device, defCredId, connId);

         } catch (Exception e) {
        }
        
        
        // Get the current date and time
       

        //String combinedString = device. + device_type + location + data + timestamp;
        // Generate a hash for the device_id (example: implementation required)
        //String hash_data = HashGenerator.generateHash(combinedString);

        try {
            
            
           // 
            
            
            
            

            // Create a document for MongoDB
            Document document = new Document();
            document.append("device_name", device.getDID());
            document.append("did_evidence", did_evidence);
            document.append("did_device", device.getDevice_id());
            //document.append("hash_data", hash_data);
            document.append("device_type", device.getDevice_type());
            document.append("data", device.getData());
            document.append("location", device.getLocation());
            document.append("timestamp", timestamp);

            // Store the document in MongoDB
          //  mds.createDeviceData(document);
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }

        // Print information for debugging purposes
       // System.out.println("type: " + device_type);
      //  System.out.println("mac: " + device_id);
      //  System.out.println("data: " + data);
       // System.out.println("location: " + location);
       // System.out.println("time: " + timestamp);
       // System.out.println("topic: " + topic);
    }
}
