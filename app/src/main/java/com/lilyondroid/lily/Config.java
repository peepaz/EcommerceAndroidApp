package com.lilyondroid.lily;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


public class Config {

    // API URL configuration
    public static String LILY_SERVER = "https://138.197.40.125";

    //FIrebase Server App server API key
    public static String FCM_API_KEY = "key=AAAAQU4jmdM:APA91bHZ517PwFoB6XZa_Zl4u2Fwjvc6J_bz10ZqLiP9IBKPB5_8XjaDJe_XrJfoj1OJgY1Igs3Yy5156ODFTwDSwRGhmNFNCVpxjS9HIqgGYecUia-RyCcHhiCq05wUQu5wlkkRlm3j";

    //Tokens for different access points
    public static String CATEGORY_TOKEN = "Token 9465145d11df5c44557a3ddfb257a28ae76beb0f";
    public static String PRODUCT_TOKEN = "Token 9465145d11df5c44557a3ddfb257a28ae76beb0f";

    // change this access similar with accesskey in admin panel for security reason
    public static String AccessKey = "12345";

    // database path configuration
    public static String DBPath = "/data/data/com.lilyondroid.lily/databases/";


    public static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient != null) return okHttpClient;
        return getUnsafeOkHttpClient();
    }

    //Http Client to handle SSL Requests
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

             okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
