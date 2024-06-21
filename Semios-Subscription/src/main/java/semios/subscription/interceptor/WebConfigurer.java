package semios.subscription.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fjtan
 */
@Slf4j
@Configuration
public class WebConfigurer implements WebMvcConfigurer {


    @Autowired
    private LogMdcInterceptor logMdcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logMdcInterceptor).addPathPatterns("/**");
    }


}
