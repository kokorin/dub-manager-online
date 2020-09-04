package dmo.client.event {
import dmo.client.domain.PageDtoOfAnimeLightDto;

import org.apache.royale.events.Event;

public class AnimeListEvent extends Event {
    public var page:PageDtoOfAnimeLightDto;

    public static const ANIME_LIST_REQUESTED:String = "ANIME_LIST_REQUESTED";
    public static const ANIME_LIST_RECEIVED:String = "ANIME_LIST_RECEIVED";

    public function AnimeListEvent(type:String, animeList:PageDtoOfAnimeLightDto = null) {
        super(type);
        this.page = animeList;
    }
}
}
