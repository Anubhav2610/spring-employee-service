package com.agyat.testapplication.TestApplicationApp.services.impl;

import com.agyat.testapplication.TestApplicationApp.services.DataService;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DataServiceImplDev implements DataService {
    @Override
    public String getData() {
        return "Dev Data";
    }
}
