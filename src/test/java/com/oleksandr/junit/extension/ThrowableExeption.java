package com.oleksandr.junit.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.IOException;

public class ThrowableExeption implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if(throwable instanceof IOException){
            throw throwable;
        }
        //allows to fail test if its IOException and skip exeption if it's not IOException
    }
}
