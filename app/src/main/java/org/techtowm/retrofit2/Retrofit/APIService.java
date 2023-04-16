package org.techtowm.retrofit2.Retrofit;

import org.techtowm.retrofit2.Data.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
This Api-Interface actually generate all the code necessary to make network calls
@Query : specifies the query key name with hte value of hte annotated parameter
Method : Get
- list : get all the datas from user table
- add : insert single field from app to mariadb
- select : search a data which is input data from app
 */
public interface APIService {
    @GET("list")
    Call<List<UserDTO>> getData(
    );

    @GET("add")
    Call<UserDTO> insertdata(
            @Query("userID") String id,
            @Query("userPassword") String password,
            @Query("userName") String name,
            @Query("userAge") String age
    );

    @GET("select")
    Call<UserDTO> login(
            @Query("userID") String id,
            @Query("userPassword") String password
    );

    @GET("showallre")
    Call<List<UserDTO>> showASC(
    );

    @GET("selectbymode")
    Call<List<UserDTO>> showReByMode(
            @Query("mode") Integer mode
    );

    @GET("findmyrecord")
    Call<List<UserDTO>> showMyRecord(
            @Query("userID") String userid
    );

    @GET("addrecord")
    Call<List<UserDTO>> addrecord(
            @Query("distance") String distance,
            @Query("speed") String speed,
            @Query("time") String time,
            @Query("userID") String userid,
            @Query("mode") Integer mode
    );

    @GET("getid")
    Call<UserDTO> getID(
            @Query("userName") String name
    );

    @GET("getpw")
    Call<UserDTO> getPW(
            @Query("userID") String id
    );

    @GET("delrecord")
    Call<UserDTO> deleteRecordChecked(
            @Query("no") Integer no
    );
}