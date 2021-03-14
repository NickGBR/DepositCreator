package ru.interns.deposit.external.cheater.service.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CheaterConf {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("ru.interns.cheaterWebService.wsdl");
        return marshaller;
    }

    @Bean
    public CheaterServiceClient countryClient(Jaxb2Marshaller marshaller) {
        CheaterServiceClient client = new CheaterServiceClient();
        client.setDefaultUri("http://localhost:9999/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}