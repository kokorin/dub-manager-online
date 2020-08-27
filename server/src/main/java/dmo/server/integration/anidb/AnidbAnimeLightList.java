package dmo.server.integration.anidb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class AnidbAnimeLightList {
    @XmlElement(name = "anime")
    public List<AnidbAnimeLight> animeList;
}
