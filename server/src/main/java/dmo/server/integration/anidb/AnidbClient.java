package dmo.server.integration.anidb;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import java.util.List;

public interface AnidbClient {

    @GET("/api/anime-titles.xml.gz")
    // To trick anidb
    @Headers("User-Agent: Mozilla")
    Call<AnidbAnimeLightList> getAnimeList();
}
