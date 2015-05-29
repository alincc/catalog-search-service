package no.nb.microservices.catalogsearch.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;

public class ReactorConfig {

    @Autowired
    private Environment env;

    @Bean
    Reactor createReactor(Environment env) {
        return Reactors.reactor().env(env).dispatcher(Environment.THREAD_POOL)
                .get();
    }

}
