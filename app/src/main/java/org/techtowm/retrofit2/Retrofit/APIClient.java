package org.techtowm.retrofit2.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
www.marinexsports.com 에 요청을 보낼것임

- Interceptor code
private val commonNetworkInterceptor = object : Interceptor {
   override fun intercept(chain: Interceptor.Chain): Response {
       1) Common Header with API Access Token
       val newRequest = chain.request().newBuilder()
               .addHeader("token", SPUtil.accessToken).build()
       2) General Response from Server (Unwrapping data)
       val response = chain.proceed(newRequest)
       3) Parse body to json
       val rawJson = response.body?.string() ?: "{}"
       4) Wrap body with gson
       val type = object : TypeToken<ResponseWrapper<*>>() {}.type
       val res = try {
            val r = gson.fromJson<ResponseWrapper<*>>(rawJson, type) ?: throw JsonSyntaxException("Parse Fail")

            if(!r.success)
                ResponseWrapper<Any>(-999, false, "Server Logic Fail : ${r.message}", null)
            else
                r
        } catch (e: JsonSyntaxException) {
             ResponseWrapper<Any>(-999, false, "json parsing fail : $e", null)
        } catch (t: Throwable) {
             ResponseWrapper<Any>(-999, false, "unknown error : $t", null)
        }
        5) get data json from data
        val dataJson = gson.toJson(res.data)
        6) return unwrapped response with body
        return response.newBuilder()
                 .message(res.message)
                .body(dataJson.toResponseBody())
                .build()
      }
}
 */
public class APIClient {

    public static String Base_url = "http://ginjae.cafe24.com/";
    private static APIClient apiClient;
    private static Retrofit retrofit;

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        /*로그*/
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        /*타임아웃 시간*/
        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        return builder.build();
    }

    static Gson gson = new GsonBuilder()
            .setDateFormat("E, yyyy-MM-dd")
            .setLenient()
            .create();

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_url)
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(createOkHttpClient())
                    .build();
        }
        return retrofit;
    }
    /*
    addConvertorFactory : JSON 을 자바에서 사용할 수 있도록 데이터를 파싱해준다.
     */
    public static synchronized APIClient getInstance() {
        if(apiClient == null) {
            apiClient = new APIClient();
        }
        return apiClient;
    }

    public APIService getApiInterface() {
        return retrofit.create(APIService.class);
    }
}