/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.connection.ConnectionAcceptInvitationFilter;
import org.hyperledger.aries.api.connection.ConnectionReceiveInvitationFilter;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.connection.CreateInvitationRequest;
import org.hyperledger.aries.api.connection.CreateInvitationResponse;
import org.hyperledger.aries.api.connection.ReceiveInvitationRequest;

/**
 *
 * @author 07429377980
 */
public class Invitation {
        AriesClient baseAgent;
        
    public Invitation(AriesClient agent){
         baseAgent = agent;
    }
    
    public void createInvitation() throws IOException {
    // informações do agente que está criando o convite
    String label = "My Connection";
    List<String> recipientKeys = Arrays.asList("EYymRn6QZtsWkgN8KjHnsaG4Jum4zAUN4x4NXv2fJYgg");
    
    // cria objeto do tipo CreateInvitationRequest
    CreateInvitationRequest invitationRequest = CreateInvitationRequest.builder()
            .myLabel(label)
            .recipientKeys(recipientKeys)
            .build();
    
    // usa a função "connectionsCreateInvitation" da classe ariesclient, que retorna um Optional<CreateInvitationResponse>
    final Optional<CreateInvitationResponse> connectionInvitation = baseAgent.connectionsCreateInvitation(invitationRequest);
    // imprime o convite criado
    System.out.println("Invitation: " + connectionInvitation);
    
    // cria um objeto anônimo com apenas os parâmetros que você deseja serializar em JSON
    Object jsonObject = new Object() {
        String invitationType = connectionInvitation.get().getInvitation().getAtType();
        String connectionId = connectionInvitation.get().getConnectionId();
        String invitationLabel = connectionInvitation.get().getInvitation().getLabel();
        String serviceEndpoint = connectionInvitation.get().getInvitation().getServiceEndpoint();
        String recipientKey = connectionInvitation.get().getInvitation().getRecipientKeys().get(0);
        String invitationUrl = connectionInvitation.get().getInvitationUrl();
    };
    // imprime o objeto serializado em JSON
    System.out.println("Invitation: " + jsonObject);
}



    // --------------------------------------------------------------------------------------------------------
    // ----------------------------------Receive a new connection invitation-----------------------------------
    // --------------------------------------------------------------------------------------------------------
    public void ReceiveInvitation(String invitation, String key) throws IOException {
    // informações do convite
    String invitationType = "https://didcomm.org/connections/1.0/invitation"; //informação vinda do outro agente - Alice
    String invitationId = invitation; //"89940bcc-786e-4cc0-ad65-c86cdbcd5ee7"; //Do agente de Alice
    String invitationlabel = "alice.agent"; // rótulo para a conexão
    String serviceEndpoint = "http://host.docker.internal:8030"; //Do agente de Alice
    String[] keys = { key }; //Do agente de Alice
    List<String> recipientKeys = Arrays.asList(keys);
    List<String> routingKeys = Arrays.asList("");
    String DID = ""; // seu DID (Decentralized Identifier)
    String imageUrl = "";
    
    // informações para filtrar o convite recebido
    String alias = "FABER_INVITE";
    Boolean autoAccept = true;
    String mediationId = "";
    
    // cria objeto do tipo ReceiveInvitationRequest
    ReceiveInvitationRequest invite2 = ReceiveInvitationRequest.builder()
            .type(invitationType)
            .id(invitationId)
            .recipientKeys(recipientKeys)
            .label(invitationlabel)
            .serviceEndpoint(serviceEndpoint)
            .build();
    
    // cria objeto do tipo ConnectionReceiveInvitationFilter
    ConnectionReceiveInvitationFilter filter = ConnectionReceiveInvitationFilter.builder()
            .alias(alias)
            .autoAccept(autoAccept)
            .mediationId(mediationId)
            .build(); 
    
    // usa a função "connectionsReceiveInvitation" da classe ariesclient, que retorna um Optional<ConnectionRecord>
    Optional<ConnectionRecord> connectionRecord = baseAgent.connectionsReceiveInvitation(invite2, filter);
    
    // pega o objeto ConnectionRecord 
    ConnectionRecord connectionRecordStatus = connectionRecord.get(); 
}

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------Accepts invitation stored in the wallet---------------------------------
    // --------------------------------------------------------------------------------------------------------
    public void acceptInvitation(String connectionId) throws IOException {
    // informações do agente (quem está aceitando o convite) SE FOR CONFIGURADO COMO AUTO ACCEPT NAO E NECESSARIO
    String myEndpoint = "http://10.30.21.23:8020";
    String myLabel = "LAS_VEGAS";
    // cria objeto do tipo ConnectionAcceptInvitationFilter
    ConnectionAcceptInvitationFilter filter = ConnectionAcceptInvitationFilter.builder()
            .myLabel(myLabel)
            .build();
    // usa a função "connectionsAcceptInvitation" da classe ariesclient, que retorna um Optional<ConnectionRecord>
    Optional<ConnectionRecord> connectionRecord = baseAgent.connectionsAcceptInvitation(connectionId, filter);
    
    // pega o objeto ConnectionRecord a partir do Optional
    ConnectionRecord connectionRecordStatus = connectionRecord.get();
}
    
    // --------------------------------------------Get connections---------------------------------------------    
    
    public List<ConnectionRecord> getConnections() throws IOException {
        List<ConnectionRecord>  conn = baseAgent.connections().get();
        // for (ConnectionRecord item : conn) {
       //     System.out.println(item);
       
       return conn;
        }
        

}