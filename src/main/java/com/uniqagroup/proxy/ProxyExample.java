package com.uniqagroup.proxy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
public class ProxyExample {
    static Map<Integer, Integer> cache = new HashMap<>();

//    public static void main(String[] args) {
//        ExtremeCalculation extremeCalculationOriginal = new ExtremeCalculation();
//
//        Math extremeCalculationProxy = (Math) Proxy.newProxyInstance(
//                ProxyExample.class.getClassLoader(),
//                new Class[]{Math.class},
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//
//                        Integer integer = cache.get(args[0]);
//                        if (integer != null) {
//                            return integer;
//                        }
//
//                        Object retVal = method.invoke(extremeCalculationOriginal, args);
//
//                        cache.put((Integer) args[0], (Integer)retVal);
//
//                        return retVal;
//                    }
//                }
//        );
//
//        Contract contract = new Contract(extremeCalculationProxy);
//        contract.calculatePremium();
//    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ProxyExample.class);
    }
}


@EnableCaching
@Configuration
class config {

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("cm");
    }
}


@Component
class Init {
    @Autowired
    Contract contract;

    @EventListener
    public void init(ContextRefreshedEvent e) {
        contract.calculatePremium();
    }

}

@Component
class Contract {
    private  final ExtremeCalculation extremeCalculation;

    Contract(ExtremeCalculation extremeCalculation) {
        this.extremeCalculation = extremeCalculation;
    }

    public void calculatePremium() {
        extremeCalculation.doCalculation();
        extremeCalculation.doCalculation();
    }
}

@Component
class ExtremeCalculation {

    @Autowired
    ApplicationContext applicationContext;

    public void doCalculation() {
        // works but don't do it this way :-)
        ExtremeCalculation bean = applicationContext.getBean(ExtremeCalculation.class);
        System.out.println(bean.calculate(1));
    }

    @Cacheable("cm")
    public Integer calculate(Integer a) {
        System.out.println("doing hard calculation");
        return a + 1;
    }
}

interface Math {
    Integer calculate(Integer a);
}
