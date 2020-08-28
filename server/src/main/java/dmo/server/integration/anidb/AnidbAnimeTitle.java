package dmo.server.integration.anidb;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlValue;

public class AnidbAnimeTitle {
    @XmlAttribute
    public Type type;

    @XmlAttribute(name = "lang", namespace = XMLConstants.XML_NS_URI)
    public String lang;

    @XmlValue
    public String text;

    public enum Type {
        @XmlEnumValue("short")
        SHORT,
        @XmlEnumValue("official")
        OFFICIAL,
        @XmlEnumValue("syn")
        SYNONYM,
        @XmlEnumValue("main")
        MAIN,
        @XmlEnumValue("card")
        CARD,
        @XmlEnumValue("kana")
        KANA,
        ;
    }
}
