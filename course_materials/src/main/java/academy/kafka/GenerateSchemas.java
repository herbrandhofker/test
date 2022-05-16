package academy.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import academy.kafka.entities.Person;
import academy.kafka.utils.SchemaUtils;

public class GenerateSchemas {  

    public static void main(String args[]) throws JsonProcessingException {
        JsonNode schema =SchemaUtils.getSchema(Person.class);
        System.out.println("\nschema of " + Person.class.getSimpleName() + ": " + schema.toPrettyString());
        schema =SchemaUtils.getKjetlandSchema(Person.class);
        System.out.println("\nKjetlandSchema schema of " + Person.class.getSimpleName() + ": " + schema.toPrettyString());

    }
}
