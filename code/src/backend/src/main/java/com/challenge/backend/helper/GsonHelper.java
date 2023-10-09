package com.challenge.backend.helper;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    public static final Gson gson = new Gson();
    public static final Gson gsonWithIdStrategy = new GsonBuilder().addSerializationExclusionStrategy(GsonHelper.getIdOnlyStrategy()).create();
    public static final Gson gsonExcludeFields = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static ExclusionStrategy getIdOnlyStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return !f.getName().equals("id");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }
}