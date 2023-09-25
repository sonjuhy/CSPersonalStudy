package clone.webFramework.service;

import clone.webFramework.annotation.MappingPost;
import clone.webFramework.annotation.PathVariable;
import clone.webFramework.annotation.RestController;
import clone.webFramework.annotation.MappingGet;

@RestController("/test")
public class RestControllerClass {

    @MappingGet("/get")
    public String getMappingMethod(){
        System.out.println("getMappingMethod '/get' is working");
        try{
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "getMapping";
    }

    @MappingGet("/getWithParam/{time}/{strParam}")
    public String getMappingSleeping(@PathVariable("strParam") String strParam, @PathVariable("time") int time){
        String str = strParam;
        System.out.println("GetWithParam time : " + time + ", strParam : " +strParam);
        return str;
    }

    @MappingPost("/post")
    public String postMappingMethod(){
        System.out.println("getMappingMethod '/post' is working");
        return "postMapping";
    }
}
