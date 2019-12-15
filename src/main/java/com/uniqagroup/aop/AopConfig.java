package com.uniqagroup.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AopConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
    }

//    @Bean
//    Resou messageSource() {
//        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
//        resourceBundleMessageSource.
//    }

}

@Aspect
@Component
class AnimalAspect {
    @Around(value = "execution(* *.make*())")
    public void animalAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("doing sound");
        joinPoint.proceed();
        System.out.println("did sound");
    }
}

interface Animal {

}

@Component()
//@Primary
class Dog implements Animal {
    public void makeSound() {
        System.out.println("hav");
    }
}

@Component
class Cat implements Animal {
    public void makeSound() {
        System.out.println("miau");
    }
}

@Component
class Test {

//    @Autowired
//    List<Animal> animal;
    @Autowired
    Cat cat;

    @Autowired
    ObjectProvider<Animal> objectProvider;

    @EventListener
    public void init(ContextRefreshedEvent e) {
        System.out.println(cat);
        cat.makeSound();
    }

}
