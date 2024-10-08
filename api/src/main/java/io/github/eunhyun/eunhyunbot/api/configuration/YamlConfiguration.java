package io.github.eunhyun.eunhyunbot.api.configuration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class YamlConfiguration implements FileConfiguration {

    private Map<String, Object> data = new LinkedHashMap<>();
    private final File file;

    public YamlConfiguration(File file) {
        this.file = file;
        load();
    }

    @Override
    public <T> T get(String path, Class<T> clazz) {
        String[] keys = path.split("\\.");
        Object value = data;

        for (String key : keys) {
            if (!(value instanceof Map)) {
                return null;
            }
            value = ((Map<?, ?>) value).get(key);
        }

        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(String path, Object value) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            Object next = current.get(key);
            if (!(next instanceof Map)) {
                next = new LinkedHashMap<>();
                current.put(key, next);
            }
            current = (Map<String, Object>) next;
        }

        current.put(keys[keys.length - 1], value);
    }

    @Override
    public String getString(String path) {
        return get(path, String.class);
    }

    @Override
    public void setString(String path, String value) {
        set(path, value);
    }

    @Override
    public char getChar(String path) {
        String value = getString(path);
        return value != null && !value.isEmpty() ? value.charAt(0) : '\0';
    }

    @Override
    public void setChar(String path, char value) {
        set(path, Character.toString(value));
    }

    @Override
    public byte getByte(String path) {
        Integer value = get(path, Integer.class);
        return value != null ? value.byteValue() : 0;
    }

    @Override
    public void setByte(String path, byte value) {
        set(path, value);
    }

    @Override
    public short getShort(String path) {
        Integer value = get(path, Integer.class);
        return value != null ? value.shortValue() : 0;
    }

    @Override
    public void setShort(String path, short value) {
        set(path, value);
    }

    @Override
    public int getInt(String path) {
        return get(path, Integer.class) != null ? get(path, Integer.class) : 0;
    }

    @Override
    public void setInt(String path, int value) {
        set(path, value);
    }

    @Override
    public long getLong(String path) {
        return get(path, Long.class) != null ? get(path, Long.class) : 0;
    }

    @Override
    public void setLong(String path, long value) {
        set(path, value);
    }

    @Override
    public float getFloat(String path) {
        Double value = get(path, Double.class);
        return value != null ? value.floatValue() : 0.0f;
    }

    @Override
    public void setFloat(String path, float value) {
        set(path, value);
    }

    @Override
    public double getDouble(String path) {
        return get(path, Double.class) != null ? get(path, Double.class) : 0.0;
    }

    @Override
    public void setDouble(String path, double value) {
        set(path, value);
    }

    @Override
    public boolean getBoolean(String path) {
        return get(path, Boolean.class) != null ? get(path, Boolean.class) : false;
    }

    @Override
    public void setBoolean(String path, boolean value) {
        set(path, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String path, Class<T> clazz) {
        List<?> list = get(path, List.class);
        if (list == null) {
            return null;
        }
        return (List<T>) list.stream()
                .filter(clazz::isInstance)
                .toList();
    }

    @Override
    public void setList(String path, List<?> value) {
        set(path, value);
    }

    @Override
    public void save() throws IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void load() {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                log.warn("파일을 생성하는 도중에 오류가 발생하였습니다. (관리자에게 문의해 주세요.)");
            }
            return;
        }
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(file)) {
            Object loaded = yaml.load(is);
            if (loaded instanceof Map) {
                data = (Map<String, Object>) loaded;
            }
        } catch (IOException | YAMLException e) {
            log.warn("파일을 불러오는 도중에 오류가 발생하였습니다. (관리자에게 문의해 주세요.)", e);
        }
    }

    public static FileConfiguration loadConfiguration(File file) {
        return new YamlConfiguration(file);
    }
}