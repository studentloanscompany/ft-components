package uk.co.slc.sfd.cp.ft.jsf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * File: SpringPropertysource.java
 * 
 * Configuration for resolving properties against any underlying source.
 * 
@author slc
 *
 */
@PropertySource({ "classpath:/profiles/default.properties", "classpath:/profiles/${env:dev}.properties" })
@Configuration
public class SpringPropertysource {

    @Autowired
    private Environment environment;

    /**
     * Return the property value associated with the given key, or {@code null}
     * if the key cannot be resolved.
     * 
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment the environment to set
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
