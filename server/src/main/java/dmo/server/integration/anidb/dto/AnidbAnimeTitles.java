package dmo.server.integration.anidb.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

@ToString(exclude = "titles")
@NoArgsConstructor
@AllArgsConstructor
public class AnidbAnimeTitles {
    @XmlAttribute(name = "aid")
    public Long id;

    @XmlElement(name = "title")
    public Set<AnidbAnimeTitle> titles;
}
