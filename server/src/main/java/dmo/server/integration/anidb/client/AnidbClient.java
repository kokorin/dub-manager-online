package dmo.server.integration.anidb.client;

import dmo.server.integration.anidb.dto.AnidbAnime;
import dmo.server.integration.anidb.dto.AnidbAnimeTitlesList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnidbClient {

    @GET("httpapi?request=anime&protover=1")
    Call<AnidbAnime> getAnime(@Query("aid") Long id,
                              @Query("client") String client,
                              @Query("clientver") String clientver);
}
