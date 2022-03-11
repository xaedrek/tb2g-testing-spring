package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("base-test")
//@Primary
public class YannyConfig {

    @Bean
    YannyWordProducer laurelWordProducer(){
        return new YannyWordProducer();
    }
}
