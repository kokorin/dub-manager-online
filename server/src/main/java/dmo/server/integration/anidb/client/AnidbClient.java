package dmo.server.integration.anidb.client;

import dmo.server.integration.anidb.dto.AnidbAnime;
import dmo.server.integration.anidb.dto.AnidbAnimeTitlesList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AnidbClient {

    @GET("https://anidb.net/api/anime-titles.xml.gz")
    // To trick anidb
    @Headers("User-Agent: Mozilla")
    Call<AnidbAnimeTitlesList> getAnimeList();

    @GET("http://api.anidb.net:9001/httpapi?request=anime&protover=1")
    Call<AnidbAnime> getAnime(@Query("aid") Long id,
                              @Query("client") String client,
                              @Query("clientver") String clientver);
}
