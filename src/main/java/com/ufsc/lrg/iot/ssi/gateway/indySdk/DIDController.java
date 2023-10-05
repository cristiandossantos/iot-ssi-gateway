/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.indySdk;


import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.did.Did;

import org.hyperledger.indy.sdk.did.DidResults;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.wallet.Wallet;
import org.json.JSONObject;

import static org.hyperledger.indy.sdk.ledger.Ledger.*;
import org.hyperledger.indy.sdk.ledger.LedgerResults;
/**
 *
 * @author 07429377980
 */
public class DIDController {
    private static final String stewardSeed = "000000000000000000000000Steward1";
    private static String walletName = "IoTWallet";
    private static String poolName = "IoT_Pool";
    private static Pool pool;
    private static Wallet walletHandle;
    private static final String poolConfig = "{\"genesis_txn\": \"C:\\\\Users\\\\07429377980\\\\indy-sdk\\\\cli\\\\docker_pool_transactions_genesis\"}";
    private static final String WALLET_CONFIG = "{ \"id\":\"" + walletName + "\", \"storage_type\":\"" + "default" + "\"}";
    private static final String WALLET_CREDENTIALS = "{\"key\":\"8dvfYSt5d1taSd6yJdpjq4emkwsPDDLYxkNFysFD2cZY\", \"key_derivation_method\":\"RAW\"}";

    public static String getDid(String device_id) throws Exception {
        //DidResults.
        
               openPool();
               openWallet();
                String trustAnchorDID_json = "{\"seed\": \"" + formatSeedString(device_id) + "\"}";
                DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, trustAnchorDID_json).get();
                String trustAnchorDID = trustAnchorResult.getDid();
                return trustAnchorDID;
                
            

        
        }


    public DIDController() {
        
    }
    
    
        
      public static void demo() throws Exception {
                
        // 1.
        System.out.println("\n1. Creating a new local pool ledger configuration that can be used later to connect pool nodes.\n");
        Pool.createPoolLedgerConfig(poolName, poolConfig).get();

        // 2
        System.out.println("\n2. Open pool ledger and get the pool handle from libindy.\n");
        Pool pool = Pool.openPoolLedger(poolName, "{}").get();

        // 3
        System.out.println("\n3. Creates a new secure wallet\n");
        Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();

        // 4
        System.out.println("\n4. Open wallet and get the wallet handle from libindy\n");
        Wallet walletHandle = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();

        // 5
        System.out.println("\n5. Generating and storing steward DID and Verkey\n");
        String did_json = "{\"seed\": \"" + stewardSeed + "\"}";
        DidResults.CreateAndStoreMyDidResult stewardResult = Did.createAndStoreMyDid(walletHandle, did_json).get();
        String defaultStewardDid = stewardResult.getDid();
        System.out.println("Steward DID: " + defaultStewardDid);
        System.out.println("Steward Verkey: " + stewardResult.getVerkey());

        // 6.
        System.out.println("\n6. Generating and storing Trust Anchor DID and Verkey\n");
        DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
        String trustAnchorDID = trustAnchorResult.getDid();
        String trustAnchorVerkey = trustAnchorResult.getVerkey();
        System.out.println("Trust anchor DID: " + trustAnchorDID);
        System.out.println("Trust anchor Verkey: " + trustAnchorVerkey);

        // 7
        System.out.println("\n7. Build NYM request to add Trust Anchor to the ledger\n");
        String nymRequest = buildNymRequest(defaultStewardDid, trustAnchorDID, trustAnchorVerkey, null, "TRUST_ANCHOR").get();
        System.out.println("NYM request JSON:\n" + nymRequest);

        // 8
        System.out.println("\n8. Sending the nym request to ledger\n");
        String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, nymRequest).get();
        System.out.println("NYM transaction response:\n" + nymResponseJson);

        // 9
        System.out.println("\n9. Generating and storing DID and Verkey to query the ledger with\n");
        DidResults.CreateAndStoreMyDidResult clientResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
        String clientDID = clientResult.getDid();
        String clientVerkey = clientResult.getVerkey();
        System.out.println("Client DID: " + clientDID);
        System.out.println("Client Verkey: " + clientVerkey);

        // 10
        System.out.println("\n10. Building the GET_NYM request to query Trust Anchor's Verkey as the Client\n");
        String getNymRequest = buildGetNymRequest(clientDID, trustAnchorDID).get();
        System.out.println("GET_NYM request json:\n" + getNymRequest);

        // 11
        System.out.println("\n11. Sending the GET_NYM request to the ledger\n");
        String getNymResponse = submitRequest(pool, getNymRequest).get();
        System.out.println("GET_NYM response json:\n" + getNymResponse);

        // 12
        System.out.println("\n12. Comparing Trust Anchor Verkey as written by Steward and as retrieved in Client's query\n");
        String responseData = new JSONObject(getNymResponse).getJSONObject("result").getString("data");
        String trustAnchorVerkeyFromLedger = new JSONObject(responseData).getString("verkey");
        System.out.println("Written by Steward: " + trustAnchorVerkey);
        System.out.println("Queried from Ledger: " + trustAnchorVerkeyFromLedger);
        System.out.println("Matching: " + trustAnchorVerkey.equals(trustAnchorVerkeyFromLedger));

        // 13
        System.out.println("\n13. Close and delete wallet\n");
        walletHandle.closeWallet().get();
        Wallet.deleteWallet(walletName, null).get();

        // 14
        System.out.println("\n14. Close pool\n");
        pool.closePoolLedger().get();

        // 15
        System.out.println("\n15. Delete pool ledger config\n");
        Pool.deletePoolLedgerConfig(poolName).get();
	}

      public static String createDID() throws Exception{
        openPool();
        openWallet();
        
        String did_json = "{\"seed\": \"" + stewardSeed + "\"}";
        
        
        
        DidResults.CreateAndStoreMyDidResult stewardResult = Did.createAndStoreMyDid(walletHandle, did_json).get();
        String defaultStewardDid = stewardResult.getDid();
      
        DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
        String trustAnchorDID = trustAnchorResult.getDid();
        String trustAnchorVerkey = trustAnchorResult.getVerkey();
      
        String nymRequest = buildNymRequest(defaultStewardDid, trustAnchorDID, trustAnchorVerkey, null, "TRUST_ANCHOR").get();
        String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, nymRequest).get();
        System.out.println("NYM transaction response:\n" + nymResponseJson);
      
        JSONObject jsonObject = new JSONObject(nymResponseJson);

            // Obtenha o valor da chave "dest" dentro de "data" dentro de "txn" dentro de "result"
            String didRegistred = jsonObject.getJSONObject("result")
                    .getJSONObject("txn")
                    .getJSONObject("data")
                    .getString("dest");

            walletHandle.closeWallet().get();
            //Wallet.deleteWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
            pool.closePoolLedger().get();
            //Pool.deletePoolLedgerConfig(poolName).get();
            
      return didRegistred;
      }
     
      public static String createDIDfromDevice(String label) throws Exception{
        openPool();
        openWallet();
        
        String did_json = "{\"seed\": \"" + stewardSeed + "\"}";
        
        //String trustAnchorDID_json = "{\"seed\": \"" + formatSeedString(identifier) + "\"}";
        
        DidResults.CreateAndStoreMyDidResult stewardResult = Did.createAndStoreMyDid(walletHandle, did_json).get();
        String defaultStewardDid = stewardResult.getDid();
      
        DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
        String trustAnchorDID = trustAnchorResult.getDid();
        String trustAnchorVerkey = trustAnchorResult.getVerkey();
      
        String nymRequest = buildNymRequest(defaultStewardDid, trustAnchorDID, trustAnchorVerkey, label, "TRUST_ANCHOR").get();
        String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, nymRequest).get();
        System.out.println("NYM transaction response:\n" + nymResponseJson);
      
        JSONObject jsonObject = new JSONObject(nymResponseJson);

            // Obtenha o valor da chave "dest" dentro de "data" dentro de "txn" dentro de "result"
            String didRegistred = jsonObject.getJSONObject("result")
                    .getJSONObject("txn")
                    .getJSONObject("data")
                    .getString("dest");

            walletHandle.closeWallet().get();
            //Wallet.deleteWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
            pool.closePoolLedger().get();
            //Pool.deletePoolLedgerConfig(poolName).get();
            
      return didRegistred;
      }
      
      public static String createDIDfromEvidence(String label) throws Exception{
        openPool();
        openWallet();
        
        String did_json = "{\"seed\": \"" + stewardSeed + "\"}";
                        
        DidResults.CreateAndStoreMyDidResult stewardResult = Did.createAndStoreMyDid(walletHandle, did_json).get();
        String defaultStewardDid = stewardResult.getDid();
      
        DidResults.CreateAndStoreMyDidResult trustAnchorResult = Did.createAndStoreMyDid(walletHandle, "{}").get();
        String trustAnchorDID = trustAnchorResult.getDid();
        String trustAnchorVerkey = trustAnchorResult.getVerkey();
      
        String nymRequest = buildNymRequest(defaultStewardDid, trustAnchorDID, trustAnchorVerkey, label, "TRUST_ANCHOR").get();
        String nymResponseJson = signAndSubmitRequest(pool, walletHandle, defaultStewardDid, nymRequest).get();
        System.out.println("NYM transaction response:\n" + nymResponseJson);
      
        JSONObject jsonObject = new JSONObject(nymResponseJson);

            // Obtenha o valor da chave "dest" dentro de "data" dentro de "txn" dentro de "result"
            String didRegistred = jsonObject.getJSONObject("result")
                    .getJSONObject("txn")
                    .getJSONObject("data")
                    .getString("dest");

            walletHandle.closeWallet().get();
            //Wallet.deleteWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
            pool.closePoolLedger().get();
            //Pool.deletePoolLedgerConfig(poolName).get();
            
      return didRegistred;
      }
      
      protected static Wallet openWallet(){
          try {
              walletHandle = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
          } catch (Exception e) {
              System.out.println("Wallet não existe... Criando wallet...");
              try {
                  Wallet.createWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
                  System.out.println("Abrindo wallet...");
                  walletHandle = Wallet.openWallet(WALLET_CONFIG, WALLET_CREDENTIALS).get();
              } catch (Exception ex) {
                  System.out.println("Erro ao criar o wallet: "+ex);
              }
          }
      return walletHandle;
      }
            
      protected static Pool openPool(){
          
          try {
              pool = Pool.openPoolLedger(poolName, "{}").get();
          } catch (Exception e) {
              System.out.println("Pool não existe... Criando Pool...");
              
              try {
                  Pool.createPoolLedgerConfig(poolName, poolConfig).get();
                    System.out.println("Abrindo pool...");
                  pool = Pool.openPoolLedger(poolName, "{}").get();
              } catch (Exception ex) {
                  System.out.println("Erro ao criar o pool: "+ex);
              }
          }
          
      return pool;
      }
      
      private static String formatSeedString(String inputString){
                
        int desiredLength = 32;
        
        // Usando String.format para adicionar zeros à esquerda
        String resultString = String.format("%" + desiredLength + "s", inputString).replace(' ', '0');
        
        System.out.println(resultString);
      
          return resultString;
      }
    
}
