package dmo.server.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class XmlLocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String value) throws Exception {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value);
    }

    @Override
    public String marshal(LocalDate value) throws Exception {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
