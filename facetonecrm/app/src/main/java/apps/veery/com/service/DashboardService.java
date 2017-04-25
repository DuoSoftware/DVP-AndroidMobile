package apps.veery.com.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sajith on 3/14/17.
 */

public class DashboardService {
    public static final String BASE_URL = "http://dashboardservice.app.veery.cloud/";

    public static Retrofit getClient() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }
}
