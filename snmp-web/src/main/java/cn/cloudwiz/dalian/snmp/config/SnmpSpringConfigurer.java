package cn.cloudwiz.dalian.snmp.config;

import cn.cloudwiz.dalian.commons.projection.web.ProjectingJackson2HttpMessageConverter;
import cn.cloudwiz.dalian.commons.projection.web.ProxyingHandlerMethodArgumentResolver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SnmpSpringConfigurer implements WebMvcConfigurer, PageableHandlerMethodArgumentResolverCustomizer {

    @Autowired
    private ProjectionFactory proxyFactory;
    @Autowired
    @Qualifier("mvcConversionService")
    private ObjectFactory<ConversionService> conversionService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration cors = registry.addMapping("/**");
        cors.allowCredentials(true);
        cors.allowedHeaders("*");
        cors.allowedOrigins("*");
        cors.allowedMethods("GET", "POST", "DELETE", "PUT");
        cors.maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        ProxyingHandlerMethodArgumentResolver resolver = new ProxyingHandlerMethodArgumentResolver(conversionService);
        resolver.setProxyFactory(proxyFactory);
        argumentResolvers.add(resolver);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new ProjectingJackson2HttpMessageConverter(proxyFactory));
    }

    @Override
    public void customize(PageableHandlerMethodArgumentResolver pageableResolver) {
        pageableResolver.setFallbackPageable(Pageable.unpaged());
    }

}
