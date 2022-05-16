package academy.kafka.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDefault;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaOptions;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaOptions.Item;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

import academy.kafka.entities.Person;

public class SchemaUtils {
    public static final ObjectMapper JACKSON_MAPPER = KafkaUtils.getJacksonMapper();
    private static final JsonSchemaGenerator kjetlandgenerator = new JsonSchemaGenerator(JACKSON_MAPPER);

    public static JsonNode getKjetlandSchema(Class<?> clazz) throws JsonProcessingException {
        return kjetlandgenerator.generateJsonSchema(clazz);
    }

    static String mapTypeToString(Class<?> type) {

        String simpleClassName = type.getSimpleName();
        switch (simpleClassName) {
            case "String":
                return "string";
            case "LocalDate":
                return "string";
            case "Integer":
            case "int":
                return "integer";
            case "Boolean":
            case "boolean":
                return "boolean";
            default:
                return "string";
        }
    }

    public static void main(String args[]) throws JsonProcessingException {
        JsonNode node = getSchema(Person.class);
        String json = JACKSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        System.out.println(json);
    }

    static JsonNode getValue(String typeString, String value) {
        if (typeString.equals("integer")) {
            Integer i = Integer.valueOf(value);
            return new IntNode(i);
        }
        if (typeString.equals("numeric")) {
            Integer i = Integer.valueOf(value);
            return new IntNode(i);
        }

        return new TextNode(value);
    }

    // See https://github.com/FasterXML/jackson-annotations/wiki/Jackson-Annotations
    public static JsonNode getSchema(Class<?> clazz) {
        ArrayNode req = JACKSON_MAPPER.createArrayNode();
        ObjectNode node = JACKSON_MAPPER.createObjectNode();
        node.put("$schema", "http://json-schema.org/draft-07/schema");
        node.put("$id", clazz.getCanonicalName());
        node.put("type", "object");

        Annotation[] classAnnotations = clazz.getAnnotations();
        for (Annotation annotation : classAnnotations) {
            if (annotation instanceof JsonSchemaDescription) {
                node.put("description", ((JsonSchemaDescription) annotation).value());
                continue;
            }
            if (annotation instanceof JsonSchemaTitle) {
                node.put("title", ((JsonSchemaTitle) annotation).value());
                continue;
            }
        }

        node.set("required", req);

        ObjectNode propertyNode = JACKSON_MAPPER.createObjectNode();
        node.set("properties", propertyNode);

        for (Field fld : clazz.getDeclaredFields()) {
            if (fld.toString().contains("static")) {
                continue;
            }
            Class<?> type = fld.getType();
            String typeAsString = "";
            ObjectNode fldAttributeNode = JACKSON_MAPPER.createObjectNode();
            if (type.toString().contains("academy.kafka.entities")) {
                JsonNode child = getSchema(type);
                propertyNode.set(fld.getName(), child);
            } else {
                typeAsString = mapTypeToString(type);
                fldAttributeNode.put("type", typeAsString);
                propertyNode.set(fld.getName(), fldAttributeNode);
            }
            for (Annotation fldAnnotations : fld.getAnnotations()) {
                if (fldAnnotations instanceof JsonProperty) {
                    JsonProperty annJsonProperty = (JsonProperty) fldAnnotations;
                    if (annJsonProperty.required())
                        req.add(fld.getName());
                    if (annJsonProperty.defaultValue() != null) {
                        fldAttributeNode.set("default", getValue(typeAsString, annJsonProperty.defaultValue())); // TODO multiple
                    }
                    continue;
                }
                if (fldAnnotations instanceof JsonSchemaFormat) {
                    fldAttributeNode.put("format", ((JsonSchemaFormat) fldAnnotations).value());
                    continue;
                }
                if (fldAnnotations instanceof JsonSchemaDefault) {
                    fldAttributeNode.put("default", ((JsonSchemaDefault) fldAnnotations).value());
                    continue;
                }
                if (fldAnnotations instanceof JsonSchemaOptions) {
                    for (Item item : ((JsonSchemaOptions) fldAnnotations).items()) {
                        if (item.name().equals("minimum")){
                            fldAttributeNode.set(item.name(), getValue(typeAsString,item.value()));
                        }
                        else
                        fldAttributeNode.put(item.name(), item.value());
                    }
                    continue;
                }
              /*  if (fldAnnotations instanceof JsonFormat) {
                    JsonFormat annJsonFormat = (JsonFormat) fldAnnotations;
                    if (annJsonFormat.shape() != null) {
                        // ((ObjectNode) attNode).put("format", annJsonFormat.value());
                    }
                    if (annJsonFormat.pattern() != null) {
                        fldAttributeNode.put("pattern", annJsonFormat.pattern());
                    }
                }*/
            }
        }
        if (req.size() == 0) {
            node.remove("required");
        }
        return node;
    }
}
