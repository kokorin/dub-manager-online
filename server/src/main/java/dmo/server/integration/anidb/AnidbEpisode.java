package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@ToString(exclude = "titles")
public class AnidbEpisode {
    @XmlAttribute(name = "id")
    public Long id;

    @XmlElement(name = "epno")
    public EpNo epno;

    @XmlElement(name = "length")
    public Long length;

    @XmlElement(name = "airdate")
    public LocalDate airDate;

    @XmlAttribute(name = "update")
    public LocalDate updateDate;

    @XmlElement(name = "title")
    public List<AnidbEpisodeTitle> titles;

    public static class EpNo {
        @XmlAttribute(name = "type")
        private Type type;

        @XmlValue
        private String number;

        public Type getType() {
            return type;
        }

        /**
         * AniDB episode number may contain not only digits but also leading letter.
         * This letter duplicates type field.
         * @return episode number
         */
        public Long getNumber() {
            if (number == null || number.isEmpty()) {
                return null;
            }

            String value = number;
            if (type != Type.REGULAR) {
                value = value.substring(1);
            }
            return Long.valueOf(value);
        }
    }

    @XmlType(name = "AnidbEpisodeType")
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
