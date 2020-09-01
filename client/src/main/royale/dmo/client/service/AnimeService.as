package dmo.client.service {

public class AnimeService {
    [Dispatcher]
    public var dispatcher:IEventDispatcher;

    public function AnimeService() {
    }

    public function loadAnime(id:Number):void {
        //function completeHandler(event:Event):void {
        Alert.show("loadAnime");
        /* }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "http://localhost:8080/api/v1/anime/666/"
        httpService.send(); */
    }

    public function loadAnimeList():void {
        //function completeHandler(event:Event):void {
        Alert.show("loadAnimeList");
        /* }

        const httpService:HTTPService = new HTTPService();
        httpService.addEventListener(HTTPConstants.COMPLETE, completeHandler);
        httpService.method = HTTPConstants.GET;
        httpService.url = "http://localhost:8080/api/v1/anime/?page=0&size=10"
        httpService.send(); */
    }
}
}
