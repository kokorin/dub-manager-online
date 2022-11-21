package dmo.server.integration.anidb.dto;

import lombok.ToString;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "error")
@ToString
public class AnidbError {

    @XmlAttribute(name = "code")
    public Integer code;

    @XmlValue
    public String message;
}
