/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;

import org.hyperledger.aries.api.present_proof.PresentProofRequest.ProofRequest.ProofRestrictions;
import org.hyperledger.aries.api.present_proof.PresentProofRequestHelper;
import org.hyperledger.aries.api.present_proof.ProofRequestPresentationBuilder;

/**
 *
 * @author 07429377980
 */


public class PresentProof {
    
    public void PresentProofRequest2(String connectionId, BaseAgent agent, Credential credential) throws IOException{
        
       
        ProofRequestPresentationBuilder builder = new ProofRequestPresentationBuilder(agent.initializeAgent());

        PresentProofRequest presentProofRequest = PresentProofRequestHelper.buildForEachAttribute(
        connectionId,
        MyCredential.class,
        ProofRestrictions
            .builder()
            .schemaId("WgWxqztrNooG92RXvxSTWv:2:schema_name:1.0")
            .build());

        Optional<ProofRequestPresentationBuilder.BuiltPresentationRequest> base64 = builder.buildRequest(presentProofRequest);
    
        //credentialDefinitionId cred = 
        
    //        PresentProof proofRequest = PresentProofRequestHelper.buildForEachAttribute(connectionId,  credential.,
   // ProofRestrictions.builder()
 //       .credentialDefinitionId("")
  //      .build());
//ac.presentProofSendRequest(proofRequest);
}

    private static class ProofRequest {

        public ProofRequest() {
        }
    }
        
        

    
   public class MyCredential {
    private String name;
    private String email;

    // Construtor
    public MyCredential(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters e setters (opcional, dependendo das necessidades do seu projeto)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

    
    }
