package com.sitepark.ies.contentrepository.core.domain.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Deserializes documents without a specific field designated for Polymorphic Type identification,
 * when the document contains a field registered to be unique to that type
 */
public class UniquePropertyPolymorphicDeserializer<T> extends StdDeserializer<T> {

  @Serial private static final long serialVersionUID = 1L;

  // the registry of unique field names to Class types
  private final Map<String, Class<? extends T>> registry;

  public UniquePropertyPolymorphicDeserializer(Class<T> clazz) {
    super(clazz);
    this.registry = new HashMap<>();
  }

  public void register(String uniqueProperty, Class<? extends T> clazz) {
    this.registry.put(uniqueProperty, clazz);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser,
   * com.fasterxml.jackson.databind.DeserializationContext)
   */
  @Override
  public T deserialize(JsonParser jp, DeserializationContext context) throws IOException {
    Class<? extends T> clazz = null;

    ObjectMapper mapper = (ObjectMapper) jp.getCodec();
    ObjectNode obj = mapper.readTree(jp);
    Iterator<Entry<String, JsonNode>> elementsIterator = obj.fields();

    while (elementsIterator.hasNext()) {
      Entry<String, JsonNode> element = elementsIterator.next();
      String name = element.getKey();
      if (registry.containsKey(name)) {
        clazz = registry.get(name);
        break;
      }
    }

    if (clazz == null) {
      context.reportInputMismatch(
          this, "No registered unique properties found for polymorphic deserialization");
    }

    return mapper.treeToValue(obj, clazz);
  }
}
