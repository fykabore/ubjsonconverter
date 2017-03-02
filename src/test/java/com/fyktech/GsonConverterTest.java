package com.fyktech;

import com.devsmart.ubjson.UBValue;
import com.devsmart.ubjson.UBValueFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GsonConverterTest {

    String[] array = new String[]{"Hello", "world", "!"};


    @Test
    public void fromJsonNull() {
        JsonElement testElement = null;
        UBValue expectedResult = null;
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonBoolean() {
        JsonElement testElement = new JsonPrimitive(true);
        UBValue expectedResult = UBValueFactory.createBool(true);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonNullValue() {
        JsonElement testElement = JsonNull.INSTANCE;
        UBValue expectedResult = UBValueFactory.createNull();
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonString() {
        JsonElement testElement = new JsonPrimitive("HelloWorld!");
        UBValue expectedResult = UBValueFactory.createString("HelloWorld!");
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonInt() {
        JsonElement testElement = new JsonPrimitive(256);
        UBValue expectedResult = UBValueFactory.createInt(256);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonFloat() {
        JsonElement testElement = new JsonPrimitive(256.128f);
        UBValue expectedResult = UBValueFactory.createFloat32(256.128f);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonDouble() {
        JsonElement testElement = new JsonPrimitive(256.128);
        UBValue expectedResult = UBValueFactory.createFloat64(256.128);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonArrayPrimitive() {
        JsonArray arrayJson = new JsonArray();
        for (String element : array) {
            arrayJson.add(element);
        }
        JsonElement testElement = arrayJson;
        UBValue expectedResult = UBValueFactory.createArray(array);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonArray() {
        JsonArray arrayJson = new JsonArray();
        for (String element : array) {
            arrayJson.add(new JsonPrimitive(element));
        }
        JsonElement testElement = arrayJson;
        UBValue expectedResult = UBValueFactory.createArray(array);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    public class TestObject {
        int i = 256;
        float f = 256.128f;
        double d = 256.128;
        String s = "Hello world !";
        String[] stringArray = array;
        Object mTestObject = new Integer(20);
        TestObject mNullObject;
        Object[] mTestObjects = new Object[]{new Integer(1), new Integer(2), new Integer(3)};

        public TestObject(int unique) {
            i += unique;
            f += unique;
            d += unique;
            s += unique;
        }
    }

    public Map<String, UBValue> createMap(TestObject testObject) {
        Map map = new HashMap<String, UBValue>();
        map.put("i", UBValueFactory.createInt(testObject.i));
        map.put("d", UBValueFactory.createFloat64(testObject.d));
        map.put("f", UBValueFactory.createFloat32(testObject.f));
        map.put("s", UBValueFactory.createString(testObject.s));
        map.put("stringArray", UBValueFactory.createArray(testObject.stringArray));
        map.put("mTestObject", UBValueFactory.createInt(20));
        UBValue[] ubValues = new UBValue[testObject.mTestObjects.length];
        for (int u = 0; u < testObject.mTestObjects.length; u++) {
            ubValues[u] = UBValueFactory.createValue(testObject.mTestObjects[u]);
        }
        map.put("mTestObjects", UBValueFactory.createArray(ubValues));
        return map;
    }

    @Test
    public void fromJsonObject() {
        TestObject testObject = new TestObject(5);
        JsonElement testElement = new GsonBuilder().create().toJsonTree(testObject);
        Map map = createMap(testObject);
        UBValue expectedResult = UBValueFactory.createObject(map);
        assertEquals(expectedResult, GsonConverter.fromJson(testElement));
    }

    @Test
    public void fromJsonArrayObject() {
        TestObject[] testObjectArray = new TestObject[]{new TestObject(1)
                , new TestObject(2)
                , new TestObject(3)
                , new TestObject(4)};
        List<UBValue> objects = new ArrayList<UBValue>();
        for (TestObject testObject : testObjectArray) {
            Map map = createMap(testObject);
            objects.add(UBValueFactory.createObject(map));
        }
        JsonElement testElement = new GsonBuilder().create().toJsonTree(testObjectArray);
        UBValue expectedResult = UBValueFactory.createArray(objects);
        UBValue actualResult = GsonConverter.fromJson(testElement);
        assertEquals(expectedResult.asArray().size(), actualResult.asArray().size());
        for (int i = 0; i < expectedResult.asArray().size(); i++) {
            assertEquals(expectedResult.asArray().get(i), actualResult.asArray().get(i));
        }
    }

    @Test
    public void toJsonNull() {
        UBValue testElement = null;
        JsonElement expectedResult = null;
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonBoolean() {
        UBValue testElement = UBValueFactory.createBool(true);
        JsonElement expectedResult = new JsonPrimitive(true);
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonNullValue() {
        UBValue testElement = UBValueFactory.createNull();
        JsonElement expectedResult = JsonNull.INSTANCE;
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonString() {
        UBValue testElement = UBValueFactory.createString("HelloWorld!");
        JsonElement expectedResult = new JsonPrimitive("HelloWorld!");
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonInt() {
        UBValue testElement = UBValueFactory.createInt(256);
        JsonElement expectedResult = new JsonPrimitive(256);
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonFloat() {
        UBValue testElement = UBValueFactory.createFloat32(256.128f);
        JsonElement expectedResult = new JsonPrimitive(256.128f);
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonDouble() {
        UBValue testElement = UBValueFactory.createFloat64(256.128);
        JsonElement expectedResult = new JsonPrimitive(256.128);
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonArrayPrimitive() {
        JsonArray arrayJson = new JsonArray();
        for (String element : array) {
            arrayJson.add(element);
        }
        UBValue testElement = UBValueFactory.createArray(array);
        JsonElement expectedResult = arrayJson;
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void toJsonArray() {
        JsonArray arrayJson = new JsonArray();
        for (String element : array) {
            arrayJson.add(new JsonPrimitive(element));
        }
        UBValue testElement = UBValueFactory.createArray(array);
        JsonElement expectedResult = arrayJson;
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }


    @Test
    public void toJsonObject() {
        TestObject testObject = new TestObject(5);
        Map map = createMap(testObject);
        UBValue testElement = UBValueFactory.createObject(map);
        JsonElement expectedResult = new GsonBuilder().create().toJsonTree(testObject);
        assertEquals(expectedResult, GsonConverter.toJson(testElement));
    }

    @Test
    public void tosonArrayObject() {
        TestObject[] testObjectArray = new TestObject[]{new TestObject(1)
                , new TestObject(2)
                , new TestObject(3)
                , new TestObject(4)};
        List<UBValue> objects = new ArrayList<UBValue>();
        for (TestObject testObject : testObjectArray) {
            Map map = createMap(testObject);
            objects.add(UBValueFactory.createObject(map));
        }
        UBValue testElement = UBValueFactory.createArray(objects);
        JsonElement expectedResult = new GsonBuilder().create().toJsonTree(testObjectArray);
        JsonElement actualResult = GsonConverter.toJson(testElement);
        assertEquals(expectedResult.getAsJsonArray().size(), actualResult.getAsJsonArray().size());
        for (int i = 0; i < expectedResult.getAsJsonArray().size(); i++) {
            assertEquals(expectedResult.getAsJsonArray().get(i), actualResult.getAsJsonArray().get(i));
        }
        assertEquals(expectedResult, actualResult);
    }

}