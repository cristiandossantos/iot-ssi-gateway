/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.indySdk;



import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 07429377980
 */
public class DidManager {
     private Map<String, String> deviceToDidMap; // Mapa para associar o DID a cada dispositivo
     
     public DidManager() {
        deviceToDidMap = new HashMap<>();
    }
        public String getDidForDevice(String deviceAddress) {
        return deviceToDidMap.get(deviceAddress);
    }
     
     
     public String assignDidToDevice(String deviceAddress, String generatedDid) {
        String existingDid = deviceToDidMap.get(deviceAddress);
        if (existingDid != null) {
            return existingDid; // Se o dispositivo já tiver um DID, retorna o mesmo DID
        } else {
            // Gera um novo DID
            String newDid = generatedDid;

            // Associa o novo DID ao dispositivo no mapa
            deviceToDidMap.put(deviceAddress, newDid);
 
            return newDid;
        }
    }
     
     
     
}