package com.koreait.picsum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PicsumService {
    @GET("v2/list")
    Call<List<PicsumVO>> getPicsumList();

}
