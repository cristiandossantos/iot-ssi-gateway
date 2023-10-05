/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.broker;


import com.ufsc.lrg.iot.ssi.gateway.model.DataIssuer;
import com.ufsc.lrg.iot.ssi.gateway.repository.DbUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Classe para subscrição de dados MQTT a partir de um broker.
 */
public class SubscribeMqtt {

    public void processMqttData() throws Exception {
        String broker = "ssl://localhost:8883";
        String topic = "mqtt-iot";
        String clientid = "GHAIFC";
        String caFilePath = "C:\\Users\\07429377980\\Docker-containers\\Mosquitto\\ca.crt";
        String clientCrtFilePath = "C:\\Users\\07429377980\\Docker-containers\\Mosquitto\\client.crt";
        String clientKeyFilePath = "C:\\Users\\07429377980\\Docker-containers\\Mosquitto\\client.key";
        String password = "naufrago";
        int qos = 0;

        try {
            MqttClient client = new MqttClient(broker, clientid, new MemoryPersistence());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);
            options.setSocketFactory(SslUtil.getSocketFactory(caFilePath, clientCrtFilePath, clientKeyFilePath, password));

            client.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                public void messageArrived(String topic, MqttMessage message) {
                    // Processar mensagem MQTT
                    DataIssuer issuer = new DataIssuer();
                    issuer.IoTDataCredentialIssuer(topic, message);
                    DbUtils util = new DbUtils();
                    util.RegisterDataDevice(topic, message);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });

            client.connect(options);
            client.subscribe(topic, qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
        
        
        
