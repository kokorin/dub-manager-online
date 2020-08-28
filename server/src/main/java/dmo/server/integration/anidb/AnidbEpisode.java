package dmo.server.integration.anidb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlValue;
import java.util.Date;
import java.util.List;

public class AnidbEpisode {
    @XmlAttribute
    public Long id;

    @XmlAttribute
    public Date update;

    @XmlElement
    public EpNo epno;

    @XmlElement
    public Long length;

    @XmlElement(name = "airdate")
    public Date airDate;

    @XmlElement(name = "title")
    public List<AnidbEpisodeTitle> titles;

    public static class EpNo {
        @XmlAttribute
        private Type type;

        @XmlValue
        private String number;

        public Type getType() {
            return type;
        }

        public Long getNumber() {
            String value = number.replaceFirst("^\\w", "");
            return Long.valueOf(value);
        }
    }

    public enum Type {
        @XmlEnumValue("1")
        REGULAR,
        @XmlEnumValue("2")
        SPECIAL,
        @XmlEnumValue("3")
        CREDIT,
        @XmlEnumValue("4")
        TRAILER,
        @XmlEnumValue("5")
        PARODY,
        @XmlEnumValue("6")
        OTHER
    }
}
