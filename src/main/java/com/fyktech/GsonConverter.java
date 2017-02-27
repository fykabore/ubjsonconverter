package com.fyktech;
import com.devsmart.ubjson.UBValue;
import com.devsmart.ubjson.UBValueFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private Gson mGson;
    public GsonConverter(){
        mGson = new GsonBuilder().serializeNulls().setLenient().create();
    }

    public UBValue fromJson(JsonElement jsonElement){
       if(jsonElement==null){
            return null;
        }
        if(jsonElement.isJsonNull()){
            return UBValueFactory.createNull();
        }
        if(jsonElement.isJsonPrimitive()){
            JsonPrimitive jsonPrimitive = (JsonPrimitive)jsonElement;
            if(jsonPrimitive.isBoolean()){
                return UBValueFactory.createBool(jsonPrimitive.getAsBoolean());
            }
            if(jsonPrimitive.isString()){
                return UBValueFactory.createString(jsonPrimitive.getAsString());
            }

            if(jsonPrimitive.isNumber()){
                Number number = jsonPrimitive.getAsNumber();
                if(number instanceof BigInteger || number instanceof BigDecimal){
                    throw new IllegalStateException("Big Integer and BigDecimal not supported");
                }
                if(number instanceof Long || number instanceof Integer
                        || number instanceof Short || number instanceof Byte){
                    return UBValueFactory.createInt(number.longValue());
                }

                if(number instanceof Float){
                    return UBValueFactory.createFloat32(number.floatValue());
                }
                if(number instanceof Double){
                    return UBValueFactory.createFloat64(number.doubleValue());
                }
            }

        }
        if(jsonElement.isJsonObject()){
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            Map<String,UBValue> tempMap = new TreeMap();
            for(Map.Entry<String,JsonElement> entry:entrySet){
                tempMap.put(entry.getKey(),fromJson(entry.getValue()));
            }
            return UBValueFactory.createObject(tempMap);
        }

        if(jsonElement.isJsonArray()){
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Iterator<JsonElement> iterator = jsonArray.iterator();
            List<UBValue> ubValueList = new ArrayList<UBValue>();
            while (iterator.hasNext()){
                ubValueList.add(fromJson(iterator.next()));
            }
            return UBValueFactory.createArray(ubValueList);
        }
        return  null;
    }
}
