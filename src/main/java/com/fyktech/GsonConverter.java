package com.fyktech;

import com.devsmart.ubjson.UBArray;
import com.devsmart.ubjson.UBValue;
import com.devsmart.ubjson.UBValueFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GsonConverter {

    public static UBValue fromJson(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }
        if (jsonElement.isJsonNull()) {
            return UBValueFactory.createNull();
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
            if (jsonPrimitive.isBoolean()) {
                return UBValueFactory.createBool(jsonPrimitive.getAsBoolean());
            }
            if (jsonPrimitive.isString()) {
                return UBValueFactory.createString(jsonPrimitive.getAsString());
            }

            if (jsonPrimitive.isNumber()) {
                Number number = jsonPrimitive.getAsNumber();
                if (number instanceof BigInteger || number instanceof BigDecimal) {
                    throw new IllegalStateException("Big Integer and BigDecimal not supported");
                }
                if (number instanceof Long || number instanceof Integer
                        || number instanceof Short || number instanceof Byte) {
                    return UBValueFactory.createInt(number.longValue());
                }

                if (number instanceof Float) {
                    return UBValueFactory.createFloat32(number.floatValue());
                }
                if (number instanceof Double) {
                    return UBValueFactory.createFloat64(number.doubleValue());
                }
            }

        }
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            Map<String, UBValue> tempMap = new TreeMap();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                tempMap.put(entry.getKey(), fromJson(entry.getValue()));
            }
            return UBValueFactory.createObject(tempMap);
        }

        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            List<UBValue> ubValueList = new ArrayList<UBValue>();
            while (iterator.hasNext()) {
                ubValueList.add(fromJson(iterator.next()));
            }
            return UBValueFactory.createArray(ubValueList);
        }

        throw new IllegalStateException("Format unknown: cannot convert " + jsonElement + " to UBValue");
    }

    public static JsonElement toJson(UBValue ubValue) {
        if (ubValue == null) {
            return null;
        }
        if (ubValue.isNull()) {
            return JsonNull.INSTANCE;
        }


        if (ubValue.isBool()) {
            return new JsonPrimitive(ubValue.asBool());
        }
        if (ubValue.isString()) {
            return new JsonPrimitive(ubValue.asString());
        }

        if (ubValue.isNumber()) {
            if (ubValue.isInteger()) {
                return new JsonPrimitive(ubValue.asInt());
            }
            return new JsonPrimitive(ubValue.asFloat64());
        }

        if (ubValue.isObject()) {
            JsonObject jsonObject = new JsonObject();
            for (Map.Entry<String, UBValue> entry : ubValue.asObject().entrySet()) {
                jsonObject.add(entry.getKey(), toJson(entry.getValue()));
            }
            return jsonObject;
        }

        if (ubValue.isArray()) {
            UBArray ubArray = ubValue.asArray();
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < ubArray.size(); i++) {
                jsonArray.add(toJson(ubArray.get(i)));
            }
            return jsonArray;
        }
        throw new IllegalStateException("Format unknown: cannot convert " + ubValue + " to jsonElement");
    }

    public static String toJsonString(UBValue ubValue) {
        return toJson(ubValue).toString();
    }

    public static String toJsonString(JsonElement jsonElement) {
        return jsonElement.toString();
    }
}
