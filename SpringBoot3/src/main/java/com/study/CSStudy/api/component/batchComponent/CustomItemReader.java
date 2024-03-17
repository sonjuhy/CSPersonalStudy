package com.study.CSStudy.api.component.batchComponent;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Setter
@Slf4j
@Component
public class CustomItemReader implements ItemReader<String> {

    private Iterator<String> stringIterator;

    public CustomItemReader(List<String> list){
        log.info("generator - has param");
        this.stringIterator = list.listIterator();
    }
    public CustomItemReader(){
        log.info("generator - no param");
        this.stringIterator = Arrays.asList("Sample Data 1","Sample Data 2","Sample Data 3","Sample Data 4").listIterator();
    }


    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(stringIterator.hasNext()){
            String str = stringIterator.next();
            log.info(str);
            return str;
        }
        else{
            return null;
        }
    }
}
