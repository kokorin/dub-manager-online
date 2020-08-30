package dmo.server.util.retrofit;

import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@RequiredArgsConstructor
public class JaxbConverterFactory extends Converter.Factory {
    private final JAXBContext jaxbContext;

    public static JaxbConverterFactory create(Class<?>... types) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(types);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context");
        }

        return new JaxbConverterFactory(jaxbContext);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class && ((Class<?>) type).isAnnotationPresent(XmlRootElement.class)) {
            return new ResponseConverter<>(jaxbContext, (Class<?>) type);
        }
        return null;
    }

    public static class ResponseConverter<T> implements Converter<ResponseBody, T> {
        private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        private final JAXBContext jaxbContext;
        private final Class<T> type;

        public ResponseConverter(JAXBContext jaxbContext, Class<T> type) {
            this.jaxbContext = jaxbContext;
            this.type = type;

            // Prevent XML External Entity attacks (XXE).
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        }

        @Override
        public T convert(ResponseBody responseBody) throws IOException {
            Object parsed;

            try (Reader reader = responseBody.charStream()) {
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                XMLStreamReader streamReader = xmlInputFactory.createXMLStreamReader(reader);
                parsed = unmarshaller.unmarshal(streamReader);
            } catch (JAXBException | XMLStreamException e) {
                throw new RuntimeException("Failed to parse response", e);
            }

            if (parsed == null) {
                return null;
            }

            if (type.isAssignableFrom(parsed.getClass())) {
                @SuppressWarnings("unchecked")
                T result = (T) parsed;
                return result;
            }

            throw new RuntimeException("Can't convert " + parsed + " to " + type);
        }
    }
}
