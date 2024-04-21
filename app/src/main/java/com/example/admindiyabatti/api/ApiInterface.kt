package com.example.blinkit.api

import com.example.admindiyabatti.models.Notification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {


    @Headers(
        "Content-Type: application/json",
        "Authorization: Key=AAAAwQRu-kQ:APA91bET-U4pvGrrwER4mAOfsUgFq1EDsqVwnQ2UhYN_OAQCC2Abbma0cTQUoNeW1vo_nIMCk4kHeQqKWLFjl-YMGS--oBxYqRq2YN3vjKQuDvt9W5cyd0cXklC5MFWgo58DmqSaDVv6"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: Notification): Call<Notification>
}