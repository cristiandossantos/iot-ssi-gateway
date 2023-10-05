/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

/**
 *
 * @author 07429377980
 */

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.acy_py.generated.model.DIDCreateOptions;
import org.hyperledger.acy_py.generated.model.DID.PostureEnum;
import org.hyperledger.acy_py.generated.model.DIDCreate.MethodEnum;
import org.hyperledger.acy_py.generated.model.DIDCreateOptions.KeyTypeEnum;
import org.hyperledger.aries.api.wallet.WalletDIDCreate;
import org.hyperledger.aries.api.wallet.ListWalletDidFilter;

/**
 *
 * @author 07429377980
 */
public class BaseAgent {
    
    private final String AGENT_URL = "http://localhost:8021";
    private AriesClient ariesclient;
    public static final String EMPTY_JSON = "{}";
    public String publicDid;

public AriesClient initializeAgent() throws IOException {
    AriesClient ariesClient = AriesClient
        .builder()
        .url("http://localhost:8010") // optional - defaults to localhost:8031
        //.apiKey("secret") // optional - admin api key if set
        //.bearerToken("123.456.789") // optional - jwt token - only when running in multi tenant mode
        .build();
    publicDid = ariesClient.walletDidPublic().get().getDid();
    System.out.println("Public DID information: " + publicDid);
    return ariesClient;
}


     public String createDid() throws IOException{

        KeyTypeEnum key = KeyTypeEnum.ED25519;
        MethodEnum method = MethodEnum.KEY;

        DIDCreateOptions options = DIDCreateOptions.builder().keyType(key).build();

        WalletDIDCreate wdc = WalletDIDCreate.builder().options(options).method(method).build();

              Optional<DID> did_generated = ariesclient.walletDidCreate(wdc);

              
              // Verifique se o DID foi gerado com sucesso
    if (did_generated.isPresent()) {
        // Obtém o DID como uma string e retorna
        String didString = did_generated.get().getDid();
        System.out.println("DID: "+didString);
        return didString;
    } else {
        // Trate o caso em que o DID não foi gerado corretamente
        return "Erro ao criar o DID";
    }
              
    }
 
    public List<String> getDID() throws IOException{
    
    List<String> didStrings = new ArrayList<>();
        
        ListWalletDidFilter xa = ListWalletDidFilter.builder().keyType(DID.KeyTypeEnum.ED25519).posture(PostureEnum.WALLET_ONLY).method(DID.MethodEnum.KEY).build();
                Optional<List<DID>> dids = ariesclient.walletDid(xa);
                
                if (dids.isPresent()) {
                List<DID> didList = dids.get();
                 for (DID did : didList) {
                 // Converte o objeto DID em string e adiciona à lista
                 didStrings.add(did.getDid());
        }
    }
                
        return didStrings;
    }


    
}
