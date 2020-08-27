package dmo.server.integration.anidb;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class AnidbAnimeTitle {
    @XmlAttribute
    public String type;

    @XmlAttribute(name = "lang", namespace = XMLConstants.XML_NS_URI)
    public String lang;

    @XmlValue
    public String text;
}
