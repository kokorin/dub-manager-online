package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@ToString
public class AnidbEpisodeTitle {

    @XmlAttribute(name = "lang", namespace = XMLConstants.XML_NS_URI)
    public String lang;

    @XmlValue
    public String text;
}
