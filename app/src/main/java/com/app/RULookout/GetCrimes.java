package com.app.RULookout;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;

/**
 * Created by TRoc9 on 8/9/2017.
 */

public interface GetCrimes {

    //@GET("{call}")
    //Call<List<CrimeData>> fetchCrimes(@Path("call") String call);
    @GET("{fullUrl}")
    Call<List<CrimeData>> fetchCrimes(@Path(value = "fullUrl", encoded = true) String fullUrl);
}
