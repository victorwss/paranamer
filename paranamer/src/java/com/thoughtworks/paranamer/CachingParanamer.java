package com.thoughtworks.paranamer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

/**
 * Implementation of Paranamer which delegate to another Paranamer implementation, adding caching functionality.
 * 
 * @author Paul Hammant
 * @author Mauro Talevi
 */
public class CachingParanamer implements Paranamer {

    private static final String __PARANAMER_DATA = "v1.0 \n"
        + "com.thoughtworks.paranamer.CachingParanamer CachingParanamer \n"
        + "com.thoughtworks.paranamer.CachingParanamer CachingParanamer com.thoughtworks.paranamer.Paranamer delegate \n"
        + "com.thoughtworks.paranamer.CachingParanamer toString \n"
        + "com.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.Constructor constructor \n"
        + "com.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.Method method \n";

    private final Paranamer delegate;
    private final WeakHashMap methodCache = new WeakHashMap();

    public CachingParanamer() {
        this(new DefaultParanamer());
    }

    public CachingParanamer(Paranamer delegate) {
        this.delegate = delegate;
    }

    public String[] lookupParameterNames(Method method) {
        if(methodCache.containsKey(method)) {
            return (String[]) methodCache.get(method);
        }

        String[] names = delegate.lookupParameterNames(method);
        methodCache.put(method, names);

        return names;
    }

    public String[] lookupParameterNames(Constructor constructor) {
        if(methodCache.containsKey(constructor)) {
            return (String[]) methodCache.get(constructor);
        }

        String[] names = delegate.lookupParameterNames(constructor);
        methodCache.put(constructor, names);

        return names;
    }

    public int areParameterNamesAvailable(ClassLoader classLoader, Class clazz, String ctorOrMethodName) {
        return delegate.areParameterNamesAvailable(classLoader, clazz, ctorOrMethodName);
    }

    public String toString() {
         return new StringBuffer("[CachingParanamer delegate=")
         .append(delegate).append("]").toString();
     }

}