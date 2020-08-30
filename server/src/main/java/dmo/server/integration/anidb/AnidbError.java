package dmo.server.integration.anidb;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "error")
@ToString
public class AnidbError implements AnidbApiResponse {

    @XmlAttribute(name = "code")
    public Integer code;

    @XmlValue
    public String message;
}
