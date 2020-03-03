package com.reedelk.runtime.converter.types.defaulttype;

import com.reedelk.runtime.converter.types.ValueConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FromDefaultConverters {

    public static final Map<Class<?>, ValueConverter<?,?>> ALL;
    static {
        Map<Class<?>, ValueConverter<?, ?>> tmp = new HashMap<>();
        tmp.put(byte[].class, new AsByteArray());
        tmp.put(Byte[].class, new AsByteArray());
        tmp.put(String.class, new AsString());
        tmp.put(Object.class, new AsObject());
        ALL = Collections.unmodifiableMap(tmp);
    }

    private FromDefaultConverters() {
    }
}
