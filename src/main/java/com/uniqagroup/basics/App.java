package com.uniqagroup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Hello world!
 *
 */
@Configuration
@ComponentScan
@PropertySource("classpath:app.properties")
public class App {
    public static void main( String[] args ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        ((AnnotationConfigApplicationContext) context).registerBean("contract:", Contract.class, ()->
                new Contract(context.getBean(BankData.class)));
        Person person = (Person)context.getBean("person");
        System.out.println(person);
    }

//    @Bean(initMethod = "", destroyMethod = "")
//    Person person() {
//        return new Person(name);
//    }

    @Bean
    public BankData bankData() {
        return new BankData();
    }

    @Bean
    public Contract contract() {
        return new Contract(bankData());
    }

}

@Component
class Runner {

    @Autowired
    Environment environment;

    @Autowired
    public void setEnvironemt(Environment environemt) {
        this.environment = environemt;
    }


    Runner(Environment environment) {
        this.environment = environment;
    }

    @EventListener
    public void onStart(ContextRefreshedEvent e) {
        System.out.println("env: " + environment.getProperty("user.username"));
    }
}

@Data
@AllArgsConstructor
class Message {
    private String message;
}

@Component
class eventPub {

    @Autowired
    ApplicationEventPublisher publisher;

//    @PostConstruct
//    public void init() {
//        publisher.publishEvent(new Message("test"));
//    }

    @EventListener
    public void onContextRefreshed(ContextRefreshedEvent e) {
        publisher.publishEvent(new Message("test"));
    }
}

//@Component
//@Service
//@Repository
//@Configuration
//class Register implements BeanDefinitionRegistryPostProcessor {
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        registry.registerBeanDefinition(BeanDefinitionBuilder.genericBeanDefinition(""));
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//
//    }
//}

class Contract {
    private final BankData bankData;

    Contract(BankData bankData) {
        this.bankData = bankData;
    }
}

class BankData {

}

@Component
@ToString
class Person {
    @Value("Michal")
    private String name;

    @PostConstruct
    public void init() {
        System.out.println("postConstruct");
    }

    @PreDestroy
    public void end() {
        System.out.println("end");
    }

    @EventListener
    public void messageListener(Message message) {
        System.out.println("message received: " + message.getMessage());
    }
}


