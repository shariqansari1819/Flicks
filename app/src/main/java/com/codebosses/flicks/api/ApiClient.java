package com.codebosses.flicks.api;

import com.codebosses.flicks.endpoints.EndpointUrl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    // Do anything with response here
                    response.header("Content-Type", APPLICATION_JSON_CHARSET_UTF_8);
                    response.header("Accept", APPLICATION_JSON_CHARSET_UTF_8);
                    return response;
                }
            }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).readTimeout(120, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS).retryOnConnectionFailure(true);
            OkHttpClient client = httpClient.build();


            X509TrustManager trustManager = null;

            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                trustManager = (X509TrustManager) trustManagers[0];
            } catch (KeyStoreException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            OkHttpClient.Builder anotherClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS);

            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, new TrustManager[] { trustManager }, null);
                anotherClient.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), trustManager);
                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();
                anotherClient.connectionSpecs(Collections.singletonList(cs));
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }


            retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointUrl.MOVIE_DB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .client(anotherClient.build())
                    .build();
        }
        return retrofit;
    }

}
