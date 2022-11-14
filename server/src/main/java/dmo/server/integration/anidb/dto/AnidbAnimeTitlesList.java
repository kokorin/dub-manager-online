package dmo.server.integration.anidb.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "animetitles")
public class AnidbAnimeTitlesList {
    @XmlElement(name = "anime")
    public List<AnidbAnimeTitles> animeList;
}
