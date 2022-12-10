package com.topcom.intime.service;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.ReadyPattern;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.repository.ReadyPatternRepository;
import com.topcom.intime.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@WebAppConfiguration
class ReadyPatternServiceTest {
    @Autowired
    private ReadyPatternRepository readyPatternRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    //패턴 추가 테스트
    @Test
    void savePatternTest(){
        ReadyPattern readyPattern= ReadyPattern.builder()
                .name("아침 식사")
                .time(600)
                .userId(1)
                .build();
        readyPatternRepository.save(readyPattern);
        assertThat(readyPattern).isNotNull();
        readyPatternRepository.delete(readyPattern);
    }

    //패턴 수정 TEST//
    @Test
    void updatePatternTest(){
        Random random=new Random();
        int newTime=random.nextInt(100);
        ReadyPattern readyPattern = readyPatternRepository.findById(3)
                .orElseThrow(()->{
                    return new IllegalArgumentException("Failed to find ReadyPatten by id: " + 1);
                });
        readyPattern.setName("양치질");
        readyPattern.setTime(newTime);
        readyPatternRepository.save(readyPattern);
        assertThat(readyPattern).isNotNull();
    }

    //패턴 일정에 추가 Test//
    @Test
    void savePatternInScheduleTest(){
        ReadyPattern readyPattern=readyPatternRepository.findById(1).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find ReadyPattern by id : " + 1);
        });
        Schedule schedule=scheduleRepository.findById(1).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find Schedule by id : " + 1);
        });

        ReadyPattern clonePattern= ReadyPattern.builder()
                .name(readyPattern.getName())
                .time(readyPattern.getTime())
                .schedule(schedule)
                .orderInSchedule(1)
                .build();
        readyPatternRepository.save(clonePattern);
        assertThat(clonePattern).isNotNull();
        readyPatternRepository.delete(clonePattern);
    }

}
