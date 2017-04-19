package biz.neustar.omx.atmos;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

 
/**
 * The Class TrimmedPropertyConfigurer.
 */
public class TrimmedPropertyConfigurer extends PropertyPlaceholderConfigurer {
    
    /** The properties map. */
    private static Map<String, String> propertiesMap = new HashMap<>();

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties)
     */
    @Override
    protected String resolvePlaceholder(final String placeholder, final Properties props) {
	final String value = super.resolvePlaceholder(placeholder, props);
	return value == null ? null : value.trim();
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.Properties)
     */
    @Override
    protected void processProperties(final ConfigurableListableBeanFactory beanFactory, final Properties properties) {
	super.processProperties(beanFactory, properties);
	for (final Object propertyKey : properties.keySet()) {
	    propertiesMap.put(propertyKey.toString(), resolvePlaceholder(propertyKey.toString(), properties));
	}
    }

    /**
     * Gets the property.
     *
     * @param name the name
     * @return the property
     */
    public String getProperty(final String name) {
	return propertiesMap.get(name);
    }
}