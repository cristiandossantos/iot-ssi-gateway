/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.controller;



import com.mongodb.client.FindIterable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 07429377980
 */
@RestController
//@RequestMapping(value = "/issuecredentials")
public class ApiRouter {
    Controller controller;
    
    public ApiRouter() throws IOException {
        controller = new Controller();
    }

    @GetMapping(value = "/iot-data")
    public List<Document> getAllDataIoT() {
        FindIterable<Document> documents = controller.getAllIoTData();
        List<Document> dataList = new ArrayList<>();
        
        for (Document document : documents) {
            dataList.add(document);
        }
        
        return dataList;
    }

    @GetMapping("/iot-data/{id}")
    public Document getDataIoTById(@PathVariable String id) {
        Document doc = controller.findByIdData(id);
        return doc;
    }
            
    @GetMapping("/connections")
    public List<ConnectionRecord> getAllConnections() throws IOException {
        List<ConnectionRecord> connectionRecords = controller.getConnections();
        return connectionRecords;
    }

    @GetMapping("/schemas")
    public List<String> getSchemas() throws IOException {
        List<String> schemas = controller.getSchemas();
        return schemas;
    }

    @GetMapping("/credential-definitions")
    public List<String> getAllCredentialDefinitions() throws IOException {
        List<String> credDefList = controller.getCredentialsDefinitions();
        return credDefList;
    }

    @GetMapping(value = "/credentials-exchanges")
    public List<V1CredentialExchange> getIssueCredentials() throws IOException {
        List<V1CredentialExchange> credEx = controller.getCredentialsExchange();
        return credEx;
    }
    
    @GetMapping("/getDID")
    public List<String> getDID() throws IOException {
        List<String> DIDs = controller.getDid();
        return DIDs;
    }

    @PostMapping("/create-invitation")
    public void createInvitation() {
    }

    @PostMapping("/receive-invitation")
    public void receiveInvitation(@RequestParam String invitationId, @RequestParam String key) throws IOException {
        System.out.println("Convite" + invitationId + "key" + key);
        controller.receiveInvitation(invitationId, key);
    }

    @PostMapping("/define-schema")
    public void defineSchema(@RequestParam String schemaName, @RequestParam String schemaVersion, @RequestParam String didSchema) throws IOException {
        controller.createSchema(schemaName, schemaVersion, didSchema);
    }

    @PostMapping("/define-credential")
    public void defineCredential(@RequestParam String schemaId) throws IOException {
        controller.CreateCredentialDefinition(schemaId);
    }

    @PostMapping("/issue-credential")
    public void IssueCredential(@RequestParam String def_cred_id, @RequestParam String conn_id) throws IOException {
        System.err.println(":::"+def_cred_id+"???"+conn_id);
        controller.credentialsIssueV1(def_cred_id, conn_id);
    } 

    @PostMapping("/performance-test")
    public void runPerformanceTests(@RequestParam String def_cred_id, @RequestParam String conn_id) throws IOException {
        controller.runPerformanceTests(def_cred_id, conn_id);
    } 

    @PostMapping("/did-create")
    public void DIDCreate() throws IOException {
        //controller.createDid();
    }
    
    @PostMapping("/register-device")
    public void registerDevice(@RequestParam String device_id ) throws Exception {
        controller.registerDevice(device_id);
    }
}
