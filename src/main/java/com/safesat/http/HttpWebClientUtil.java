package com.safesat.http;


import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class HttpWebClientUtil {
    private static String publicCertLocation;
    
    public static HttpClient httpClient(){
        SSLConnectionSocketFactory sslServerSocketFactory = new SSLConnectionSocketFactory(getSSLContext());
        return HttpClientBuilder.create().useSystemProperties().setSSLSocketFactory(sslServerSocketFactory).build();
    }
    
    private static SSLContext getSSLContext() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            //FIXME
            //final X509Certificate x509Certificate = loadCertificate();
            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    //FIXME
                    //verifyServerTrusted(chain, x509Certificate);
                }
                
                public X509Certificate[] getAcceptedIssuers() {
                    //FIXME
                    //return new X509Certificate[] { x509Certificate };
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[] { tm }, null);
            return sslContext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

     private static void verifyServerTrusted(X509Certificate[] chain, X509Certificate x509Certificate) throws CertificateException {
         try {
             for (X509Certificate c : chain){
                     c.verify(x509Certificate.getPublicKey());
                     c.checkValidity();
             }
         } catch (InvalidKeyException e) {
             throw new CertificateException("Invalid key", e);
         } catch (NoSuchAlgorithmException e) {
             throw new CertificateException("NoSuchAlgorithmException", e);
         } catch (NoSuchProviderException e) {
             throw new CertificateException("NoSuchProviderException", e);
         } catch (SignatureException e) {
             throw new CertificateException("SignatureException", e);
         } catch (Exception e ){
             throw new CertificateException("Certificate not trusted", e);
         }
     }
     
     private static X509Certificate loadCertificate() {
            InputStream finStream  = null;
            try {
                finStream = new FileInputStream(publicCertLocation);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");   
                X509Certificate x50Cert = (X509Certificate)cf.generateCertificate(finStream);
                return x50Cert;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e){
                e.printStackTrace();
            }finally{
                try {
				if (finStream != null) {
					finStream.close();
				}
                    finStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
     public static void setPublicCertLocation(String publicCertLocation) {
         HttpWebClientUtil.publicCertLocation = publicCertLocation;
     }
     
    private HttpWebClientUtil(){}
}



