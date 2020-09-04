package dmo.client.event {
import org.apache.royale.events.Event;

public class AnimeEvent extends Event {
    public static const ANIME_REQUESTED:String = "ANIME_REQUESTED";
    public static const ANIME_RECEIVED:String = "ANIME_RECEIVED";

    public function AnimeEvent(type:String) {
        super(type);
    }
}
}