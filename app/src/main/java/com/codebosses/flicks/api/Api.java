package com.codebosses.flicks.api;

import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.moviespojo.MoviesMainObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    //        String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
//    String XWWWORMURLENCODED = "application/x-www-form-urlencoded";
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

    Api WEB_SERVICE = new Retrofit.Builder()
            .baseUrl(EndpointUrl.MOVIE_DB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(Api.class);


    @GET("movie/upcoming")
    Call<MoviesMainObject> getUpcomingMovies(@Query(EndpointKeys.API_KEY) String api_key, @Query(EndpointKeys.LANGUAGE) String language, @Query(EndpointKeys.PAGE) int page, @Query(EndpointKeys.REGION) String region);


}