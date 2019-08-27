package notification.aggregator;

//import io.micrometer.core.instrument.util.StringEscapeUtils;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Controller
@Requires(env = "test")
public class SchemaRegistryStubController {

    private Map<String, Schema> map = new HashMap<>();

    /**
     * Register the acro objects you need stubbed.  If there are many of them, this could be externalized into a more
     * scalable pattern on controller load
     */
    public SchemaRegistryStubController() {
        //Need to escape the string with some library that will encode it as json value.
        // The Avro object has the SCHEMA$ static that is the schema on it.
        //map.put("person-object-value", new Schema(1, "{\"schema\":\"" + StringEscapeUtils.escapeJson(PersonObject.SCHEMA$.toString()) + "\"}"));
        //map.put("person-object-key", new Schema(2, "{\"schema\":\"\\\"string\\\"\"}"));
    }

    /**
     * Method that gets a version of an object, version is unique for both key and values
     * @param subject name of schema
     * @param body post body to ignore for now
     * @return id of the schema
     */
    @Post(value = "/subjects/{subject}/versions", consumes = "*/*")
    public HttpResponse<String> getSchemaVersion(@NotBlank String subject, @Body String body) {
        return HttpResponse.ok("{\"id\":" + map.get(subject).id + "}");
    }

    /**
     * Get the schema by id
     * @param id id of the schema
     * @return the schema registry json for the schema
     */
    @Get(value = "/schemas/ids/{id}", consumes = "*/*")
    public HttpResponse<String> getSchema(@NotBlank Integer id) {
        return HttpResponse.ok(map.values()
                .stream()
                .filter(schema -> schema.id.equals(id))
                .findFirst()
                .orElse(new Schema(0, "{\"schema\":\"\"}"))
                .schema);
    }

    /**
     * Schema object for easy of registry and tracking id
     */
    class Schema {
        Integer id;
        String schema;

        Schema(Integer id, String schema) {
            this.id = id;
            this.schema = schema;
        }
    }
}