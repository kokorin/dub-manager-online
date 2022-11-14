package dmo.server.integration.anidb.dto;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.XMLConstants;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@ToString
@EqualsAndHashCode
public class AnidbAnimeTitle {
    @XmlAttribute
    public Type type;

    @XmlAttribute(name = "lang", namespace = XMLConstants.XML_NS_URI)
    public String lang;

    @XmlValue
    public String text;

    @XmlType(name = "AnidbAnimeTitleType")
    public enum Type {
        @XmlEnumValue("short")
        SHORT,
        @XmlEnumValue("official")
        OFFICIAL,
        @XmlEnumValue("syn")
        SYN,
        @XmlEnumValue("synonym")
        SYNONYM,
        @XmlEnumValue("main")
        MAIN,
        @XmlEnumValue("card")
        CARD,
        @XmlEnumValue("kana")
        KANA;
    }
}
