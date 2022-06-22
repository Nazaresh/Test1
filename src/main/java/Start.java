import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Providers;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.glassfish.jersey.message.filtering.spi.FilteringHelper.EMPTY_ANNOTATIONS;

public class Start
{
    @Context
    protected static Providers providers;

    public static void main(String[] args) throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper() ;
        String metadata = objectMapper.writeValueAsString(new Metadata(TestType.FIRST, "firstParam"));

        File doc = new File(Start.class.getResource("/Picture.jpg").getFile());

        final Map<String, String> parameters = new HashMap<>();
        parameters.put("boundary", "junior");
        final MediaType mediaType = new MediaType("multipart", "form-data", parameters);
        String boundary = parameters.get("boundary");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        MultivaluedMap<String, Object> map = new MultivaluedHashMap<>();
        map.add("Token","myToken");
        map.add("Sig", "mySig");

        FormDataMultiPart multiPart = new FormDataMultiPart();
        FormDataBodyPart part1 = new FormDataBodyPart("metadata", metadata, MediaType.APPLICATION_JSON_TYPE);
        multiPart.getBodyParts().add(part1);
        FormDataBodyPart part2 = new FileDataBodyPart("content", doc);
        multiPart.getBodyParts().add(part2);

        new MultiPartWriter(providers).writeTo(multiPart, multiPart.getClass(), multiPart.getClass(), EMPTY_ANNOTATIONS, mediaType, map, baos);

        System.out.println(baos);
    }
}
