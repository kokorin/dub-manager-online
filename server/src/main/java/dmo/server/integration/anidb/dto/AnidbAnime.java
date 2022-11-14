package dmo.server.integration.anidb.dto;

import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@XmlRootElement(name = "anime")
@ToString(exclude = {"titles", "episodes"})
public class AnidbAnime implements AnidbApiResponse {
    @XmlAttribute
    public Long id;

    @XmlElement
    public Type type;

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

    @XmlType(name = "AnidbAnimeType")
    public enum Type {
        @XmlEnumValue("Movie")
        MOVIE,
        @XmlEnumValue("OVA")
        OVA,
        @XmlEnumValue("TV Series")
        TV_SERIES,
        @XmlEnumValue("TV Special")
        TV_SPECIAL,
        @XmlEnumValue("Web")
        WEB,
        @XmlEnumValue("Music Video")
        MUSIC_VIDEO,
        @XmlEnumValue("Other")
        OTHER,
        @XmlEnumValue("unknown")
        UNKNOWN;
    }
}
