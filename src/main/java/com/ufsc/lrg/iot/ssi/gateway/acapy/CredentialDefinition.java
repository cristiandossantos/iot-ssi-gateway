/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.hyperledger.aries.api.credential_definition.CredentialDefinitionFilter;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionResponse;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionsCreated;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionRequest;
import org.hyperledger.aries.AriesClient;
/**
 *
 * @author 07429377980
 */
public class CredentialDefinition {
    AriesClient baseAgent;
    
    public CredentialDefinition (AriesClient agent){
        baseAgent = agent;
    }
    
    public void defineCredential(String schema_Id)throws IOException{
        String schemaId = schema_Id; 
        CredentialDefinitionRequest request = CredentialDefinitionRequest.builder()
                .schemaId(schemaId)
                .tag("evidence2")
                .build();
        
        Optional<CredentialDefinitionResponse> credentialDefinition = baseAgent.credentialDefinitionsCreate(request);
        String definitionId = credentialDefinition.get().getCredentialDefinitionId();
    }
    
    public List<String> listAllCredentialsDefinitions() throws IOException{
        Optional<CredentialDefinitionsCreated> credentialDefinition = baseAgent.credentialDefinitionsCreated(CredentialDefinitionFilter.builder().build());
        if (credentialDefinition.isPresent()) {
            CredentialDefinitionsCreated credentialDefinitionsCreated = credentialDefinition.get();
        List<String> credentialDefinitionsList = credentialDefinitionsCreated.getCredentialDefinitionIds();
        return credentialDefinitionsList;
        } else {
        return null;
    }
    }
    
    
    
}