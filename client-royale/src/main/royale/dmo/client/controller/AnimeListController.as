package dmo.client.controller {
import dmo.client.domain.AnimeLightDto;
import dmo.client.domain.PageDtoOfAnimeLightDto;
import dmo.client.event.AnimeListEvent;

import mx.collections.ArrayCollection;
import mx.logging.ILogger;
import mx.logging.Log;

import org.apache.royale.events.IEventDispatcher;

public class AnimeListController {
    [Dispatcher]
    public var dispatcher:IEventDispatcher;

    [Bindable]
    public var page:PageDtoOfAnimeLightDto;

    private static const LOGGER:ILogger = Log.getLogger("dmo.client.controller.AnimeListController");

    public function loadAnimeList():void {
        dispatcher.dispatchEvent(new AnimeListEvent(AnimeListEvent.ANIME_LIST_REQUESTED))
    }

    [EventHandler(event="AnimeListEvent.ANIME_LIST_RECEIVED")]
    public function onAnimeListReceived(event:AnimeListEvent):void {
        LOGGER.info("RECEIVED {0}", event);
        const tmpPage:PageDtoOfAnimeLightDto = new PageDtoOfAnimeLightDto();
        tmpPage.content = new ArrayCollection();
        const anime:AnimeLightDto = new AnimeLightDto();
        anime.id = 42;
        anime.type = "TEST";
        tmpPage.content.addItem(anime);
        / TODO not working
        //this.page = event.page;
        this.page = tmpPage;
    }
}
}