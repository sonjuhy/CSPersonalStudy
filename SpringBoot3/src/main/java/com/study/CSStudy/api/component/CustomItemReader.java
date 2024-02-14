package com.study.CSStudy.api.component;

import lombok.Setter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Setter
@Component
public class CustomItemReader implements ItemReader<Integer> {

    private Iterator<Integer> intList;
    public CustomItemReader(List<Integer> list){
        this.intList = list.listIterator();
    }


    @Override
    public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(intList.hasNext()){
            List<Integer> list = (List<Integer>) intList;
            for(int num : list){
                System.out.println(num);
            }
            return intList.next();
        }
        else{
            return null;
        }
    }
}
