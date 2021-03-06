package com.trinhtien2212.mobilefindroomrental.retrofit;

import com.trinhtien2212.mobilefindroomrental.model.Location;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DataClient {

    @Headers("Content-Type: application/json")
    @POST("api/add-location/")
    Call<String> insertData(@Body String location);

    @Headers("Content-Type:application/json")
    @PUT("api/update-location")
    Call<String> updateData(@Query("roomID") String roomID, @Body String location);

    @DELETE("api/delete-location")
    Call<String> deleteData(@Query("roomID") String roomID);
    @GET("api/search-room")
    Call<Object> searchData(@Query("address") String roomID,
                                             @Query("distance") int distance);
    //user
    @GET("api/list-all-users")
    Call<Object> getAllUsers();

    @DELETE("api/delete-user")
    Call<String> deleteUser(@Query("userUid") String userUid);

    @GET("api/get-total-room-filter")
    Call<String> getTotalRoomFilter(@Query("start") String dateStart, @Query("end") String dateEnd);

    @GET("api/get-total-room-sort")
    Call<String> getTotalRoomSort();
}
