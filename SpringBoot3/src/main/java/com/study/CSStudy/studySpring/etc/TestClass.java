package com.study.CSStudy.studySpring.etc;

public class TestClass {
    public TestClass(SubTestClass subTestClass){
        System.out.println("This is TestClass Constructor");
        System.out.println("hashCode : "+ subTestClass.hashCode());
    }

}