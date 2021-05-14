package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            // If the field is private then the value would be true.
            // Then it will access the body of the if statement and
            // change it to true.
            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(target, toInject);
            if(wasPrivate) {
                field.setAccessible(false);
            }


        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}