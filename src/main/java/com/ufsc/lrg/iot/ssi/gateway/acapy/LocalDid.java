/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.acapy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.acy_py.generated.model.DIDCreateOptions;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.wallet.ListWalletDidFilter;
import org.hyperledger.aries.api.wallet.WalletDIDCreate;

/**
 *
 * @author 07429377980
 */
public class LocalDid {
    AriesClient ariesclient;
    BaseAgent agent;
    public LocalDid(){
        try {
        agent = new BaseAgent();
        ariesclient = agent.initializeAgent();
        } catch (Exception e) {
        }
 
    }
      public String createDid() throws IOException{

        DIDCreateOptions.KeyTypeEnum key = DIDCreateOptions.KeyTypeEnum.ED25519;
        DIDCreate.MethodEnum method = DIDCreate.MethodEnum.KEY;

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
        
        ListWalletDidFilter xa = ListWalletDidFilter.builder().keyType(DID.KeyTypeEnum.ED25519).posture(DID.PostureEnum.WALLET_ONLY).method(DID.MethodEnum.KEY).build();
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
