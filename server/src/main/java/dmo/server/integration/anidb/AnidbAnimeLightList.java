package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@ToString
public class AnidbAnimeLightList {
    @XmlElement(name = "anime")
    public List<AnidbAnimeLight> animeList;
}
