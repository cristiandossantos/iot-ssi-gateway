/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ufsc.lrg.iot.ssi.gateway.broker;


import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.net.ssl.KeyManagerFactory;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
public class SslUtil {
    
 @SuppressWarnings("deprecation")
        //It will return SSLSocketFactory
        public static SSLSocketFactory getSocketFactory (final String 
       caCrtFile, final String crtFile, final String keyFile,                                                     
                   final String password) throws Exception
        {
            try{
                Security.addProvider(new BouncyCastleProvider());

                X509Certificate caCert = (X509Certificate)SslUtil.getCertificate(caCrtFile);
                X509Certificate cert = (X509Certificate)SslUtil.getCertificate(crtFile);
                FileReader fileReader = new FileReader(keyFile);
                PEMParser parser = new PEMParser(fileReader);
                PEMKeyPair kp = (PEMKeyPair) parser.readObject();

                PrivateKeyInfo info = kp.getPrivateKeyInfo();

                PrivateKey rdKey = new JcaPEMKeyConverter().setProvider("BC")
                        .getPrivateKey(info);

                // CA certificate is used to authenticate server
                KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
                caKs.load(null, null);
                caKs.setCertificateEntry("ca-certificate", caCert);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(caKs);

                // client key and certificates are sent to server so it can authenticate us
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(null, null);
                ks.setCertificateEntry("certificate", cert);
                ks.setKeyEntry("private-key", rdKey, password.toCharArray(), new java.security.cert.Certificate[]{cert});
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(ks, password.toCharArray());

                // finally, create SSL socket factory
                SSLContext context = SSLContext.getInstance("TLSv1.3");
                context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                return context.getSocketFactory();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        
        
         public static java.security.cert.X509Certificate getCertificate(String pemfile) throws Exception
        {
            java.security.cert.X509Certificate cert = null;
            try {
                FileReader fRd = new FileReader(pemfile);
                final PemReader certReader = new PemReader(fRd);
                final PemObject certAsPemObject = certReader.readPemObject();
                if (!certAsPemObject.getType().equalsIgnoreCase("CERTIFICATE")) {
                    throw new Exception("Certificate file does not contain a certificate but a " + certAsPemObject.getType());
                }
                final byte[] x509Data = certAsPemObject.getContent();
                final CertificateFactory fact = CertificateFactory.getInstance("X509");
                cert = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(x509Data));
                if (!(cert instanceof X509Certificate)) {
                    throw new Exception("Certificate file does not contain an X509 certificate");
                }

            } catch (FileNotFoundException e) {
                throw new IOException("Can't find file " + pemfile);
            }catch (Exception e) {
                System.out.println("#Exceotion :"+e.getMessage());
            }
            return cert;
        }

    //retuen keyPair Object form client key
        public KeyPair decodeKeys(byte[] privKeyBits,byte[] pubKeyBits) 
              throws InvalidKeySpecException, NoSuchAlgorithmException {
              KeyFactory keyFactory=KeyFactory.getInstance("RSA");
              PrivateKey privKey=keyFactory.generatePrivate(new 
              PKCS8EncodedKeySpec(privKeyBits));
              PublicKey pubKey=keyFactory.generatePublic(new 
              X509EncodedKeySpec(pubKeyBits));
              return new KeyPair(pubKey,privKey);
            }
    }
        
        
        
        
        
        