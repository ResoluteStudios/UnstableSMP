package com.resolutestudios.unstablesmp.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

    private final Logger logger;
    private final String packageName;

    public ExceptionHandler(Logger logger, String packageName) {
        this.logger = logger;
        this.packageName = packageName;
    }

    /**
     * Logs an exception with filtered stack trace to hide plugin internals
     */
    public void logException(String message, Throwable throwable) {
        // Log generic error message
        logger.log(Level.SEVERE, message);
        
        // Filter stack trace to remove plugin package references
        StackTraceElement[] originalTrace = throwable.getStackTrace();
        StackTraceElement[] filteredTrace = filterStackTrace(originalTrace);
        
        // Create a new exception with filtered trace
        Throwable filtered = new Throwable(throwable.getMessage());
        filtered.setStackTrace(filteredTrace);
        
        // Log the filtered exception
        logger.log(Level.SEVERE, "Error details:", filtered);
    }

    /**
     * Filters stack trace to hide plugin package references
     */
    private StackTraceElement[] filterStackTrace(StackTraceElement[] original) {
        return java.util.Arrays.stream(original)
            .filter(element -> !element.getClassName().startsWith(packageName))
            .map(element -> {
                // Obfuscate class and method names
                String className = element.getClassName();
                String methodName = element.getMethodName();
                
                // Replace with generic names
                if (className.contains(packageName)) {
                    className = "PluginClass";
                    methodName = "pluginMethod";
                }
                
                return new StackTraceElement(
                    className,
                    methodName,
                    element.getFileName() != null ? "Source.java" : null,
                    -1 // Hide line numbers
                );
            })
            .toArray(StackTraceElement[]::new);
    }

    /**
     * Handles uncaught exceptions globally
     */
    public static void setupGlobalHandler(Logger logger, String packageName) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            ExceptionHandler handler = new ExceptionHandler(logger, packageName);
            handler.logException("An unexpected error occurred in thread " + thread.getName(), throwable);
        });
    }
}
