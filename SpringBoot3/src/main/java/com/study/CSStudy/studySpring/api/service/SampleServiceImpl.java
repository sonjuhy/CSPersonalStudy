package com.study.CSStudy.studySpring.api.service;

import com.study.CSStudy.studySpring.bean.SampleBean;
import com.study.CSStudy.studySpring.component.SampleNonConfiguration;
import com.study.CSStudy.studySpring.configuration.SampleConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    private SampleBean sampleBean;
    @Autowired
    private SampleConfiguration configuration;
    @Autowired
    private SampleNonConfiguration nonConfiguration;

    @Override
    public void testBean() {
        System.out.println(sampleBean.declareBean());
        System.out.println(sampleBean.unDeclareBean());
    }

    @Override
    public void testComponent() {

    }

    @Override
    public void testConfiguration() {
        System.out.println("configuration hashCode : "+configuration.getTestClass().hashCode());
        System.out.println("nonConfiguration hashCode"+nonConfiguration.getTestClassOnNoneConfiguration().hashCode());
        /*
        * result :
        *    Spring Framework 실행 될때 로그
        *    This is SubTestClass Constructor mode: nonConfiguration
        *    This is SubTestClass Constructor mode: nonConfiguration
        *    This is TestClass Constructor
        *    hashCode : 530254639
        *    This is SubTestClass Constructor mode: configuration
        *    This is TestClass Constructor
        *    hashCode : 190960840
        *
        *    이 메소드가 실행이 되었을 때 로그
        *    configuration hashCode : 2060637909
        *    This is SubTestClass Constructor mode: nonConfiguration
        *    This is TestClass Constructor
        *    hashCode : 1608832055
        *    nonConfiguration hashCode302209311
        *
        * Configuration 에서 선언한 Bean 은 싱글톤 Bean 이기에 한번만 생성이 실행이 되었지만,
        * NonConfiguration(@Component) 에서 선언한 Bean 은 싱글톤 Bean 이 아니기 때문에 호출한 순서 만큼 생성이 되었음.
        * */
    }
}
