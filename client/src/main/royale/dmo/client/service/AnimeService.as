package dmo.client.service {
import org.apache.royale.events.Event;
import org.apache.royale.events.IEventDispatcher;
import org.apache.royale.jewel.Alert;
import org.apache.royale.net.HTTPConstants;
import org.apache.royale.net.HTTPService;

public class AnimeService {
    [Dispatcher]
    public var dispatcher:IEventDispatcher;

    public function AnimeService() {
    }

    public function loadAnime(id:Number):void {
        function completeHandler(event:Event):void {
            Alert.show("loadAnime");
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/666/"
        httpService.send();
    }

    public function loadAnimeList():void {
        function completeHandler(event:Event):void {
            Alert.show("loadAnimeList");
        }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "/api/v1/anime/?page=0&size=10"
        httpService.send();
    }
}
}
