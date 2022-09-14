package org.example.di;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BeanFactoryTest {
    private Reflections reflections;
    private  BeanFactory beanFactory;

    //테스트 메소드가 호출되기전 동작하는 setUp 메소드
    @BeforeEach
    void setUp() {
        reflections = new Reflections("org.example");
        //애노테이션을 파라미터로 던져서, 인자로 받고, 애노테이션이 붙은 클래스 타입을 반환해준다.
        Set<Class<?>> preInstantiatedClazz = getTypesAnnotatedWith(Controller.class, Service.class);
        beanFactory = new BeanFactory(preInstantiatedClazz);
    }

    // Class<? extends Annotation>... annotations 애노테이션 클래스 객체가 여러개 들어올 수 있다는 의미다.
    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = new HashSet<>();
        for (Class<? extends Annotation> annotation: annotations){
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    @Test
    void diTest(){
        UserController userController = beanFactory.getBean(UserController.class);

        assertThat(userController).isNotNull();
        assertThat(userController.getUserService()).isNotNull();
    }
}