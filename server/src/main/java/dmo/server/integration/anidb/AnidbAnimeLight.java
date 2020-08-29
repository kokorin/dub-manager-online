package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@ToString(exclude = "titles")
public class AnidbAnimeLight {
    @XmlAttribute(name = "aid")
    public Long id;

    @XmlElement(name = "title")
    public List<AnidbAnimeTitle> titles;
}
