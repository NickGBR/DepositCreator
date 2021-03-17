package ru.interns.deposit.util;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.ApacheIgniteDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.service.enums.Errors;

import java.util.Iterator;
import java.util.List;

@Component
public class Cache {

    @Autowired
    public Cache(Ignite ignite) {
        cache = ignite.cache("my-cache2");
    }

    public static IgniteCache<String, ApacheIgniteDTO> cache;

    public void put(String login, ApacheIgniteDTO client) {
        cache.put(login,client);
    }

    public static void addErrors(String login, Errors error) {
        ApacheIgniteDTO apacheIgniteDTO = cache.get(login);
        List<Errors> errors = apacheIgniteDTO.getErrors();
        errors.add(error);
        cache.replace(login, apacheIgniteDTO);
        for (javax.cache.Cache.Entry<String, ApacheIgniteDTO> stringClientDTOEntry : cache) {
            ApacheIgniteDTO object = stringClientDTOEntry.getValue();
            System.out.println(object);
        }
    }

    public static void setServiceStatus(String login, Services services, Status status) {
        ApacheIgniteDTO apacheIgniteDTO = cache.get(login);
        apacheIgniteDTO.getServiceStatus().put(services,status);
        cache.replace(login, apacheIgniteDTO);
        Iterator<javax.cache.Cache.Entry<String, ApacheIgniteDTO>> itr = cache.iterator();
        while(itr.hasNext()) {
            ApacheIgniteDTO object = itr.next().getValue();
            System.out.println(object);
        }
    }

/*    public static ApacheIgniteDTO getUser(String login) {
*//*        BinaryObject bo = (BinaryObject) cache.get(login);
        return bo.deserialize();*//*
        return cache.get(login);
    }*/

    public static List<Errors> getErrors(String login) {
        return cache.get(login).getErrors();
    }

}
