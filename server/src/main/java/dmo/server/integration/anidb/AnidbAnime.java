package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.List;

@XmlRootElement
@ToString
public class AnidbAnime {
    @XmlAttribute
    public Long id;

    @XmlElement
    public String type;

    @XmlElement(name = "episodecount")
    public Long episodeCount;

    @XmlElement(name = "startdate")
    public LocalDate startDate;

    @XmlElement(name = "enddate")
    public LocalDate endDate;

    @XmlElementWrapper(name = "titles")
    @XmlElement(name = "title")
    public List<AnidbAnimeTitle> titles;

    @XmlElementWrapper(name = "episodes")
    @XmlElement(name = "episode")
    public List<AnidbEpisode> episodes;


}
