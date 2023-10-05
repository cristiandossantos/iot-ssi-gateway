/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

/**
 *
 * @author 07429377980
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hyperledger.acy_py.generated.model.SchemaGetResult;
import org.hyperledger.aries.api.schema.SchemaSendRequest;
import org.hyperledger.aries.api.schema.SchemaSendResponse;
import org.hyperledger.aries.api.schema.SchemasCreatedFilter;
import org.hyperledger.aries.AriesClient;

/**
 *
 * @author 07429377980
 */
public class Schema {
    AriesClient baseAgent;
    String did;
    
    public Schema(AriesClient agent){
    baseAgent = agent;
   }
    
    public Optional<SchemaSendResponse> GenerateSchema(String schemaName, String schemaVersion )throws IOException{ 
        did = baseAgent.walletDidPublic().get().getDid();
        List<String> attributes = attributesAddSchema();
        
        
        SchemaSendRequest schemaRequest = SchemaSendRequest.builder()
            .schemaName(schemaName)
            .schemaVersion(schemaVersion)
            .attributes(attributes)
            .build();
        
        
        Optional<SchemaSendResponse> schemaResponse = baseAgent.schemas(schemaRequest);
        
        return schemaResponse;
   }
    
    private static List<String> attributesAddSchema() {
       List<String> attributes = new ArrayList<>();
       attributes.add("Operator");
       attributes.add("device_did");
       attributes.add("device_type");
       //attributes.add("hash_data");
       attributes.add("data");
       attributes.add("collection_did");
       attributes.add("location");
       attributes.add("timestamp");
       
       
        return attributes;

    }

    // --------------------------------------------Get All Schemas--------------------------------------------     
    public  Optional<List<String>> getAllSchemas(AriesClient ariesclient )throws IOException{
        Optional<List<String>> schemas = ariesclient.schemasCreated(SchemasCreatedFilter.builder().build());
        return  schemas;
    }
    // --------------------------------------------Get Schemas-------------------------------------------- 
    public void getSchemasById()throws IOException{ 
    
        String schemaId = "PBVp4eZ9gLxJMoqi5vEJY0:2:Netuno:2.0"; 
        String schemaName = "default";
        String schemaVersion = "2.0";
        String schemaDid = "ZgWxqztrNooG92RXvxSCAV";
        //List<String> attributes = attributesAddSchema();  
         Object filter2 = SchemaGetResult.builder().build().getSchema();
 }
}