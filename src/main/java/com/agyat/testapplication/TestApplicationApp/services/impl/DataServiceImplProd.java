package com.agyat.testapplication.TestApplicationApp.services.impl;

import com.agyat.testapplication.TestApplicationApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
//@Profile("prod")
public class DataServiceImplProd implements DataService {
    @Override
    public String getData() {
        return "Prod Data";
    }
}
