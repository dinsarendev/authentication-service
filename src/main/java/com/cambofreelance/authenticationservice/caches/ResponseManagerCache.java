package com.cambofreelance.authenticationservice.caches;


import com.cambofreelance.authenticationservice.models.ResponseCode;
import java.util.Hashtable;
import java.util.List;

public class ResponseManagerCache {
    private static Hashtable<String, ResponseCode> responseCodes;
    private static Hashtable<String, String> generalCaches;

    public static void initRespCodeCache(List<ResponseCode> respCodes) {
        generalCaches = new Hashtable<>();
        if (!respCodes.isEmpty()) {
            responseCodes = new Hashtable<>();
            respCodes.forEach(value -> responseCodes.put(value.getCode(), value));
        }
    }

    public static ResponseCode getRespCode(String code) {
        return responseCodes.get(code);
    }

    public static String get(String key) {
        return generalCaches.get(key);
    }

    public static void add(String key, String value) {
        generalCaches.put(key, value);
    }
}

