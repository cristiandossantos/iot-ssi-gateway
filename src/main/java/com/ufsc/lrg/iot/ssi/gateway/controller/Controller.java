/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.controller;



import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.ufsc.lrg.iot.ssi.gateway.acapy.BaseAgent;
import com.ufsc.lrg.iot.ssi.gateway.acapy.Credential;
import com.ufsc.lrg.iot.ssi.gateway.acapy.CredentialDefinition;
import com.ufsc.lrg.iot.ssi.gateway.acapy.Invitation;
import com.ufsc.lrg.iot.ssi.gateway.acapy.Schema;
import com.ufsc.lrg.iot.ssi.gateway.indySdk.DIDController;
import com.ufsc.lrg.iot.ssi.gateway.model.IotDevice;
import com.ufsc.lrg.iot.ssi.gateway.repository.DataStorage;


import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.AriesClient;


/**
 *
 * @author 07429377980
 */
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    
    private final DataStorage mds;
    private final MongoCollection<Document> collection;
    private final BaseAgent baseAgent;
    private final AriesClient ariesClient ;
    private final String schema = null;
    private final String definedCredencial = null;
    private IotDevice device;
            
    public Controller() throws IOException {
        baseAgent = new BaseAgent();
        ariesClient = baseAgent.initializeAgent();
        mds = new DataStorage();
        collection = mds.readDeviceData();
                device = new IotDevice();
    }
    
    public void createinvitation() throws IOException{
        Invitation invitation = new Invitation(ariesClient);
        invitation.createInvitation();
    }
    
    public void receiveInvitation(String invitation_Id, String recipientKeys) throws IOException {
        Invitation invitation = new Invitation(ariesClient);
        invitation.ReceiveInvitation(invitation_Id, recipientKeys);
    }
        
    public void createSchema(String schemaName, String schemaVersion, String didSchema) throws IOException {
        Schema schema = new Schema(ariesClient);
        schema.GenerateSchema(schemaName, schemaVersion);
    }
    
    public void CreateCredentialDefinition(String schema) throws IOException {
        CredentialDefinition credentialDefinition = new CredentialDefinition(ariesClient);
        credentialDefinition.defineCredential(schema);
    }
    
    public void credentialsIssueV1(String defCredId, String connId ) throws IOException {
        String id = "650939e196135f39d5c7b285";
        Document doc = findByIdData(id);
        IotDevice dispositivo = createDeviceFromDocument(doc);
        Credential credential = new Credential(ariesClient);
        credential.issueCredential(device, connId, defCredId);
    }
    
    public List<ConnectionRecord> getConnections() throws IOException{
        Invitation invitation = new Invitation(ariesClient);
        return invitation.getConnections();
    }
    
    public List<String> getSchemas() throws IOException{
        Schema schema = new Schema(ariesClient);
        Optional<List<String>> schemas = schema.getAllSchemas(ariesClient);
        List<String> listaSchemas = schemas.get(); // Obtém a lista de strings
        return listaSchemas;
    } 
    
    public List<String> getCredentialsDefinitions() throws IOException{
        CredentialDefinition def_cred = new CredentialDefinition(ariesClient);
        List<String> credentialsDefList = def_cred.listAllCredentialsDefinitions();
        return credentialsDefList;
    } 
    
    public List<V1CredentialExchange> getCredentialsExchange() throws IOException{
        Credential credential = new Credential(ariesClient);
        Optional<List<V1CredentialExchange>> credentialExchange = credential.getAllCredentials();
        List<V1CredentialExchange> credentials = credentialExchange.get();
        return credentials;
    }
 
    public FindIterable<Document> listAll() {
        FindIterable<Document> documents = collection.find();
        for (Document document : documents) {
            for (String key : document.keySet()) {
                Object value = document.get(key);
                System.out.println(key + ": " + value);
            }
            System.out.println("----------------------------------------");
        }
        return documents;
    }
    
    public Document findByIdData(String id) {
        Bson filter = Filters.eq("_id", new ObjectId(id));
        Document document = (Document) collection.find(filter).first();
        return document;
    }
    
    public FindIterable<Document> getAllIoTData() {
        FindIterable<Document> documents = collection.find();
        return documents;
    }
    
    public Document findByIdDevice(String id) {
        Bson filter = Filters.eq("_id", new ObjectId(id));
        Document document = (Document) collection.find(filter).first();
        return document;
    }
 
    public void registerDevice(String device_id) throws Exception {
        String deviceDID = DIDController.createDIDfromDevice(device_id);
        device = new IotDevice();
        device.setDevice_hash(HashGenerator.generateHash(device_id));
        device.setDID(deviceDID);
        mds.registerDevice(device);
    }  

    public String GetDeviceById(String device_id) {
        String did_device = null;
        try {
            Document device_registered = mds.getDocumentById(device_id);
            did_device = device_registered.get("did").toString();
        } catch (Exception e) {
            System.err.println("Registro não encontrado " + e);

            try {
                did_device = DIDController.createDIDfromDevice(device_id);
            } catch (Exception ex) {
                System.err.println("Falha ao registrar dispositivo " + ex);
            }
        }

        return did_device;
    }
    
    
    private IotDevice createDeviceFromDocument(Document doc) throws IOException {
        //Device device = new IoTDevice();
        device.setDevice_id(doc.getObjectId("_id").toHexString());
        //device.setDID(createDid());
        device.setDevice_id(doc.getString("device_id"));
        device.setData(doc.getString("data"));
        device.setDevice_hash(doc.getString("hash_id"));
        device.setDevice_type(doc.getString("device_type"));
        device.setLocation(doc.getString("location"));
        //device.setTimestamp(doc.getDate("timestamp"));
        return device;
    }        

    public void runPerformanceTests(String def_cred_id, String connId) throws IOException {
        //String id = "64b05ad49647cb226327bbd5";
        //Document doc = findById(id);
        //Device dispositivo = createDeviceFromDocument();
        String conn_id = connId;
        String definedCredencial = def_cred_id;
    }
     
  
    public void IssueCredentialIoT(IotDevice device, String defCredId, String connId ) throws IOException {
        Credential credential = new Credential(ariesClient);
        credential.issueCredential(device, connId, defCredId);
    }
        
    public String createPrivateDid() throws IOException{
        String DID = baseAgent.createDid();
        return DID;
    }
       
    public List<String> getDid() throws IOException{
        return baseAgent.getDID();
    }
                      
    public String getHash(String data){
        return HashGenerator.generateHash(data);
    }
                
                
}
    
