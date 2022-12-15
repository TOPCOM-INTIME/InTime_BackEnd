package com.topcom.intime.service;

import com.topcom.intime.Dto.Schedule.SaveScheduleDto;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.repository.ReadyPatternRepository;
import com.topcom.intime.repository.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@WebAppConfiguration
class ScheduleServiceTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ReadyPatternRepository readyPatternRepository;

    //개인 일정 추가 Test//
    @Test
    void save_scheduleTest(){
        Schedule scd= new Schedule();
        scd.setName("대학 전공 강의 들으러 가기");
        scd.setTime(600);
        scd.setSourceName("범계역");
        scd.setDestName("아주대");
        scd.setStartTime(Timestamp.valueOf("2022-12-15 09:00:00"));
        scd.setReadyTime(Timestamp.valueOf("2022-12-15 07:00:00"));
        scd.setEndTime(Timestamp.valueOf("2022-12-15 10:15:00"));
        scd.setStatus("PRE");
        scheduleRepository.save(scd);
        assertThat(scd).isNotNull();
        scheduleRepository.delete(scd);
    }

    //일정 업데이트 Test//
    @Test
    void update_scheduleTest(){
        Random rnd= new Random();
        Schedule schedule=scheduleRepository.findById(9).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find Schedule by id: " + 9);
        });
        int newTime=rnd.nextInt(300);
        schedule.setName("유튜브 시청");
        schedule.setTime(newTime);
        scheduleRepository.save(schedule);
        assertThat(schedule.getName()).isEqualTo("유튜브 시청");
    }

}
