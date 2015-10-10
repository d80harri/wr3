package net.d80harri.wr.ui.core;

import java.util.Map;

import ma.glasnost.orika.Converter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringAwareBeanMapper extends ConfigurableMapper implements ApplicationContextAware {
 
    private MapperFactory factory;
 
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureFactoryBuilder(final DefaultMapperFactory.Builder factoryBuilder) {
        // customize the factoryBuilder as needed
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(final MapperFactory factory) {
        this.factory = factory;
        // customize the factory as needed
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        addAllSpringBeans(applicationContext);
    }
 
    /**
     * Adds all managed beans of type {@link Mapper} or {@link Converter} to the parent {@link MapperFactory}.
     *
     * @param applicationContext The application context to look for managed beans in.
     */
    @SuppressWarnings("rawtypes")
    private void addAllSpringBeans(final ApplicationContext applicationContext) {
		final Map<String, Converter> converters = applicationContext.getBeansOfType(Converter.class);
        for (final Converter converter : converters.values()) {
            addConverter(converter);
        }
 
        final Map<String, Mapper> mappers = applicationContext.getBeansOfType(Mapper.class);
        for (final Mapper mapper : mappers.values()) {
            addMapper(mapper);
        }
    }
 
    /**
     * Add a {@link Converter}.
     *
     * @param converter The converter.
     */
    public void addConverter(final Converter<?, ?> converter) {
        factory.getConverterFactory().registerConverter(converter);
    }
 
    /**
     * Add a {@link Mapper}.
     *
     * @param mapper The mapper.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addMapper(final Mapper<?, ?> mapper) {
        factory.classMap(mapper.getAType(), mapper.getBType())
                .byDefault()
                .customize((Mapper) mapper)
                .register();
    }
}