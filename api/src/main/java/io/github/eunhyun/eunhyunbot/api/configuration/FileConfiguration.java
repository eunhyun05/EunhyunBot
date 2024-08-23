package io.github.eunhyun.eunhyunbot.api.configuration;

import java.io.IOException;

public interface FileConfiguration {

    <T> T get(String path, Class<T> clazz);

    void set(String path, Object value);

    String getString(String path);

    void setString(String path, String value);

    char getChar(String path);

    void setChar(String path, char value);

    byte getByte(String path);

    void setByte(String path, byte value);

    short getShort(String path);

    void setShort(String path, short value);

    int getInt(String path);

    void setInt(String path, int value);

    long getLong(String path);

    void setLong(String path, long value);

    float getFloat(String path);

    void setFloat(String path, float value);

    double getDouble(String path);

    void setDouble(String path, double value);

    boolean getBoolean(String path);

    void setBoolean(String path, boolean value);

    void save() throws IOException;
}