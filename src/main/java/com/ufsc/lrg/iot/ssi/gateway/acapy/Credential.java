/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;


import com.ufsc.lrg.iot.ssi.gateway.model.IotDevice;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.api.issue_credential_v1.IssueCredentialRecordsFilter;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialProposalRequest;

//import org.hyperledger.aries.api.issue_credential_v2.V2IssueCredentialRecordsFilter;
//import org.hyperledger.aries.api.issue_credential_v2.V2CredentialExchangeFree;
//import org.hyperledger.aries.api.issue_credential_v2.V20CredProposal;

/**
 *
 * @author 07429377980
 */
public class Credential {
        AriesClient baseAgent;
        
    public Credential(AriesClient agent){
        baseAgent = agent;
    }
        
    public void issueCredential(IotDevice device, String connId, String credDefId)throws IOException{
        Map<String, String> attributeValues = new HashMap<>();
        //attributeValues.put("id", device.getDevice_id());
        attributeValues.put("Operator", device.getOperator());
        attributeValues.put("device_did", device.getDID());
        attributeValues.put("collection_did", device.getEmission_did());
        attributeValues.put("device_type", device.getDevice_type());
        attributeValues.put("data", device.getData());
        //attributeValues.put("device_id", device.getDevice_id());
        attributeValues.put("location", device.getLocation());
        attributeValues.put("timestamp", device.getTimestamp().toString());

        
        List<CredentialAttributes> attributes = CredentialAttributes.fromMap(attributeValues);
        CredentialPreview credPrev2 = new CredentialPreview(attributes);
        
        String connectionId = connId;
        String credentialdefinitionId = credDefId;
        Boolean autoIssue= true;
                
        Optional<V1CredentialExchange> response = baseAgent.issueCredentialSend(
                    V1CredentialProposalRequest.builder()
                            .connectionId(connectionId)
                            .credentialDefinitionId(credentialdefinitionId)
                            .autoRemove(true)
                            .credentialProposal(credPrev2)
                            .build());
        
        System.out.println(response);
    }
    
    // --------------------------------------------Get All Credentials exchanges-------------------------------------------- 
    public Optional<List<V1CredentialExchange>> getAllCredentials()throws IOException{
        Optional<List<V1CredentialExchange>> credentialEx = baseAgent.issueCredentialRecords(IssueCredentialRecordsFilter.builder().build());
        return credentialEx;

    }

}