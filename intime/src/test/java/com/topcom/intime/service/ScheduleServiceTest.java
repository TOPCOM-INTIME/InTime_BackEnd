package com.topcom.intime.service;

import com.topcom.intime.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
class ScheduleServiceTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void saveScheduleTest(){

    }
}
