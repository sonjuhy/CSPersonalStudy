package com.study.CSStudy.studySpring.component;

import com.study.CSStudy.studySpring.etc.SubTestClass;
import com.study.CSStudy.studySpring.etc.TestClass;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SampleNonConfiguration {
    @Bean
    public SubTestClass getSubTestClassOnNoneConfiguration(){
        return new SubTestClass("nonConfiguration");
    }

    @Bean
    public TestClass getTestClassOnNoneConfiguration(){
        return new TestClass(getSubTestClassOnNoneConfiguration());
    }
}
