package com.study.CSStudy.studySpring.configuration;

import com.study.CSStudy.studySpring.api.dto.SampleDto;
import com.study.CSStudy.studySpring.etc.SubTestClass;
import com.study.CSStudy.studySpring.etc.TestClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleConfiguration {

    @Bean
    public SubTestClass getSubTestClass(){
        return new SubTestClass("configuration");
    }

    @Bean
    public TestClass getTestClass(){
        return new TestClass(getSubTestClass());
    }
}
