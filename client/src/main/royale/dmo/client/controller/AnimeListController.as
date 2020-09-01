package dmo.client.controller {
import dmo.client.service.AnimeService;

public class AnimeListController {

    [Inject]
    public var animeService:AnimeService;

    public function onClick():void {
        animeService.loadAnimeList();
    }
}
}