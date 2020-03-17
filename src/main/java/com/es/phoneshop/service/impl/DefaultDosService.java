package com.es.phoneshop.service.impl;

import com.es.phoneshop.service.DosService;

import java.util.HashMap;
import java.util.Map;

public class DefaultDosService implements DosService {

    private static final int MAX_REQUEST_COUNT = 20;
    private Map<String, Integer> requestCount;

    private DefaultDosService() {
        requestCount = new HashMap<>();
    }

    public static DefaultDosService getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final DefaultDosService instance = new DefaultDosService();
    }

    @Override
    public synchronized boolean isAllowed(String ip) {
        Integer count = requestCount.getOrDefault(ip, 0);
        requestCount.put(ip, count + 1);
        return count <= MAX_REQUEST_COUNT;
    }
}
