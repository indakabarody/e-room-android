package com.project.e_room;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("insert.php")
    Call<Value> daftar(@Field("namaPeminjam") String namaPeminjam,
                       @Field("ruang") String ruang,
                       @Field("tanggal") String tanggal,
                       @Field("jamAwal") String jamAwal,
                       @Field("jamAkhir") String jamAkhir,
                       @Field("keperluan") String keperluan);
    @GET("view.php")
    Call<Value> view();
}