package dmo.client.service {
import mx.logging.ILogger;
import mx.logging.Log;

import org.apache.royale.events.Event;
import org.apache.royale.events.IEventDispatcher;
import org.apache.royale.net.HTTPConstants;
import org.apache.royale.net.HTTPService;

public class AnimeService {
    [Dispatcher]
    public var dispatcher:IEventDispatcher;

    private static const LOGGER:ILogger = Log.getLogger("dmo.client.service.AnimeService");

    public function AnimeService() {
    }

    public function loadAnime(id:Number):void {
        function completeHandler(event:Event):void {
            trace("HUI: !");
            LOGGER.error("loadAnime");
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/666/"
        httpService.send();
    }

    public function loadAnimeList():void {
        function completeHandler(event:Event):void {
            trace("HUI: !");
            LOGGER.error("loadAnime");
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/?page=0&size=10"
        httpService.send();
    }
}
}
