package github.andredimaz.plugin.core.utils.objects;

import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MetadataObject {
    private final Map<String, Object> meta = new HashMap<>();

    /**
     * Obtém um valor de metadado associado a uma chave específica.
     *
     * @param key A chave do metadado.
     * @return Um objeto MetadataValue que encapsula o valor associado à chave.
     */
    public MetadataValue getMetadata(String key) {
        return new MetadataValue(meta.get(key));
    }

    /**
     * Define um valor de metadado associado a uma chave específica.
     *
     * @param key   A chave do metadado.
     * @param value O valor do metadado.
     * @return A instância atual de MetadataObject, permitindo o encadeamento de chamadas.
     */
    public MetadataObject setMetadata(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public static class MetadataValue {
        private final Object object;

        private MetadataValue(Object object) {
            this.object = object;
        }

        public String toString() {
            return object instanceof String ? (String) object : String.valueOf(object);
        }

        public int toInt() {
            if (object instanceof Number) {
                return ((Number) object).intValue();
            }
            throw new ClassCastException("Cannot convert object to int");
        }

        public double toDouble() {
            if (object instanceof Number) {
                return ((Number) object).doubleValue();
            }
            throw new ClassCastException("Cannot convert object to double");
        }

        public short toShort() {
            if (object instanceof Number) {
                return ((Number) object).shortValue();
            }
            throw new ClassCastException("Cannot convert object to short");
        }

        public byte toByte() {
            if (object instanceof Number) {
                return ((Number) object).byteValue();
            }
            throw new ClassCastException("Cannot convert object to byte");
        }

        public Color toColor() {
            if (object instanceof Color) {
                return (Color) object;
            }
            throw new ClassCastException("Cannot convert object to Color");
        }

        public Material toMaterial() {
            if (object instanceof Material) {
                return (Material) object;
            }
            throw new ClassCastException("Cannot convert object to Material");
        }

        public Object getObject() {
            return object;
        }
    }
}

