package dmo.server.util.retrofit;

import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@RequiredArgsConstructor
public class JaxbConverterFactory extends Converter.Factory {
    private final JAXBContext jaxbContext;

    public static JaxbConverterFactory create(Class<?> ...types) {
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

    public static class ResponseConverter extends Converter<ResponseBody, ?> {

    }
}
