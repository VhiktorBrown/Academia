package com.theelitedevelopers.academia.core.data.remote;

import com.theelitedevelopers.academia.core.data.request.Notification;
import com.theelitedevelopers.academia.core.data.request.NotificationMessage;

import org.json.JSONObject;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("send")
    @Headers("Content-Type: application/json")
    Single<Response<JSONObject>> sendNotification(@HeaderMap Map<String, String> headers, @Body Notification notification);
}
