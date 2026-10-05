package com.oasys.engine.communication.packets;

import com.google.gson.*;

/**
 * This class represents the converter. It contains the Deserialization and Serialization of Json files.
 */
public abstract class Packet {
    private static final Gson gson = new GsonBuilder().serializeNulls().create();

    /**
     * Deserializes a Json String into an object of the packet classes.
     */
    public static Packet convertFromJson(String json) throws JsonParseException{
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        if (jsonObject.get("type") == null) {
            throw new JsonParseException("This json file doesn't contain type.");
        }
        String type = jsonObject.get("type").getAsString();
        String className = Character.toUpperCase(type.charAt(0)) + type.substring(1);

        Class<? extends Packet> cls = null;
        try {
            cls = (Class<? extends Packet>) Class.forName("com.oasys.engine.communication.packets." + className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Invalid Packet type.");
        }
        if (jsonObject.get("content") == null) {
            throw new JsonParseException("This json file doesn't contain content.");
        }
        JsonElement content = jsonObject.get("content");
        Packet packet =  gson.fromJson(content, cls);
        return packet;
    }

    /**
     * Serializes an object of a packet class into a Json String.
     */
    public String convertTojson() {
        String content = gson.toJson(this, this.getClass());
        String className = this.getClass().getSimpleName();
        String type = Character.toLowerCase(className.charAt(0)) + className.substring(1);
        return "{\"type\": \"" + type + "\", \"content\":" + content + "}";
    }
}


