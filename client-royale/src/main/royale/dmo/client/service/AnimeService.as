package dmo.client.service {
import dmo.client.domain.PageDtoOfAnimeLightDto;
import dmo.client.event.AnimeListEvent;
import dmo.client.mapper.Mapper;

import mx.logging.ILogger;
import mx.logging.Log;

import org.apache.royale.events.Event;
import org.apache.royale.events.IEventDispatcher;
import org.apache.royale.net.HTTPConstants;
import org.apache.royale.net.HTTPService;

public class AnimeService {
    [Dispatcher]
    public var dispatcher:IEventDispatcher;

    private static const MAPPER:Mapper = new Mapper(PageDtoOfAnimeLightDto);

    private static const LOGGER:ILogger = Log.getLogger("dmo.client.service.AnimeService");

    public function AnimeService() {
    }

    public function loadAnime(id:Number):void {
        function completeHandler(event:Event):void {
            LOGGER.error("loadAnime");
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/666/"
        httpService.send();
    }

    [EventHandler(event="AnimeListEvent.ANIME_LIST_REQUESTED")]
    public function loadAnimeList(event:AnimeListEvent):void {
        LOGGER.info("Loading Anime List: {}", event);

        function completeHandler(event:Event):void {
            LOGGER.info("AnimeList Received");
            const page:PageDtoOfAnimeLightDto = MAPPER.fromDto(httpService.json) as PageDtoOfAnimeLightDto;
            dispatcher.dispatchEvent(new AnimeListEvent(AnimeListEvent.ANIME_LIST_RECEIVED, page));
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/?page=0&size=10"
        httpService.send();
    }
}
}
