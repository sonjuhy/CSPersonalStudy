package com.study.CSStudy.studySpring.component;

import org.springframework.stereotype.Component;

@Component(value = "sampleComponent") // component value = sampleComponent, value 값으로 bean 등록
public class SampleComponent {
    /**
     * Component Annotation : SingleTon class bean
     * 개발자가 직접 작성한 class 를 Bean 으로 등록하기 위해 주로 사용하는 Annotation
     * */
    public SampleComponent(){
        /*
         * Spring Framework 가 처음 시작 되면서 ComponentScan 을 통해 생성을 함.
         * 그래서 Spring Framework 가 시작할 때 하단의 print 함수가 실행이 됨
         * */
        System.out.println("this is sample component");
    }

//    @Component
    public void testForComponentAnnotationDeclaration(){
        /*
         * class bean 답게 method 에 선언시, 아래와 같은 에러가 발생함.
         * */
        System.out.println("Component declaration error : '@Component' not applicable to method");
    }
}
