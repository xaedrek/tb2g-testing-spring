package org.springframework.samples.petclinic.sfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("externalized")
@Primary
@Component
public class PropertiesWordProducer implements WordProducer{
    @Value("$say.word")
    public void setWord(String word) {
        this.word = word;
    }

    private String word;
    @Override
    public String getWord() {
        return word;
    }
}
