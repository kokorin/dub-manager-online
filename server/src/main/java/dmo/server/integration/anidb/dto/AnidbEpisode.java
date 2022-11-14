package dmo.server.integration.anidb.dto;

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

    @XmlElement(name = "title")
    public List<AnidbEpisodeTitle> titles;

    public Type getType() {
        return epno.getType();
    }

    public Long getNumber() {
        return epno.getNumber();
    }

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
         *
         * @return episode number
         * @see <a href="https://wiki.anidb.net/Content:Episodes#Type">Episode Types</a>
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
        /**
         * Normal or Regular.
         */
        @XmlEnumValue("1")
        REGULAR,
        @XmlEnumValue("2")
        SPECIAL,
        /**
         * Opening/Ending
         */
        @XmlEnumValue("3")
        CREDIT,
        /**
         * Trailer/Promo/Commercial.
         */
        @XmlEnumValue("4")
        TRAILER,
        @XmlEnumValue("5")
        PARODY,
        @XmlEnumValue("6")
        OTHER
    }
}
