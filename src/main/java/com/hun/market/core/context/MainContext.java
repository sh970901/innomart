package com.hun.market.core.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainContext implements ApplicationContextAware {

    public static final String LEADER = "Leader";
    public static final String FOLLOWER = "Follower";

    public static final String NO_TRANSACTIONS = "There are no transactions";
    public static final String DATA_SOURCE_AROUND = "within(com.hun..service..*) && execution(* com.hun..*Service*.*(..))";
    public static final String CONFIGRURATION_PROPERTIES_PREFIX = "baobab.scan";

    private static ApplicationContext applicationContext;

    @Override public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MainContext.applicationContext = applicationContext;
    }

    static  {
        System.setProperty("java.net.preferIPv4Stack", "true");

        java.security.Security.setProperty("networkaddress.cache.ttl" , "1");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl" , "3");

        //init();
    }


    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }
}
