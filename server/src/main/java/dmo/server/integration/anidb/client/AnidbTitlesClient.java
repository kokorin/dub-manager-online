package dmo.server.integration.anidb.client;

import dmo.server.integration.anidb.dto.AnidbAnimeTitlesList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AnidbTitlesClient {

    @GET("api/anime-titles.xml.gz")
    // To trick anidb
    @Headers("User-Agent: Mozilla")
    Call<AnidbAnimeTitlesList> getAnimeList();
}
