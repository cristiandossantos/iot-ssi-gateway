/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

import org.hyperledger.aries.webhook.EventHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author 07429377980
 */
public class WebhookController {
    private EventHandler handler = new EventHandler.DefaultEventHandler();

   @PostMapping("/webhook/topic/{topic}")
   public void handleWebhookEvent(
           @PathVariable String topic,
           @RequestBody String payload) {
      handler.handleEvent(topic, payload);
   }
}
