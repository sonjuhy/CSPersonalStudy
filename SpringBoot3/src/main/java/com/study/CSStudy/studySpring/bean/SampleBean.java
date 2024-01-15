package com.study.CSStudy.studySpring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Bean // Bean declaration eror : '@Bean' not applicable to type
@Configuration
public class SampleBean {

    /*
     * withOut declare class Bean and set Autowired, error : Field sampleBean in com.study.CSStudy.studySpring.api.service.SampleServiceImpl required a bean of type 'com.study.CSStudy.studySpring.bean.SampleBean' that could not be found.
     * */

    @Bean // method 에 선언 가능
    public String declareBean(){
        /*
         * Bean Annotation 은 개발자 가 제어 불가능한 외부 라이브러리를 Bean 으로 만들때 사용
         * Configuration 안에 Bean 으로 선언 시, 별도 설정을 하지 않는 이상 싱글톤으로 생성되는 것을 보장함
         * 마찬가지로 Spring Framework가 처음 실행 되면서 ComponentScan을 할 때 해당 메소드를 Bean 등록하면서 아래 코드가 실행이 됨
         * */
//        System.out.println("declareBean");
        return "declareBean";
    }

    @Bean // method 에 선언 가능
    public void declareBeanError(){
        /*
         * Bean Annotation 이 적용된 메소드의 return 값이 void일 경우 Bean 등록이 안됨
         * */
        System.out.println("declareBean");
    }

    public String unDeclareBean(){
        return "unDeclareBean";
    }

    /**
     * 이 메소드는 리턴 타입이 void 이므로, Bean 등록이 안됨.
     * */
    @Bean
    public void unDeclareVoidBean(){
        System.out.println("unDeclareVoidBean");
    }
}
