package com.topcom.intime.service;

import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.ScheduleRepository;
import com.topcom.intime.repository.UserRepository;
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
    private UserRepository userRepository;

    @Test
    //스케줄 추가 테스트
    void saveScheduleTest(){
        User user=userRepository.findByUsername("jaehyeok").orElseThrow(()->{
            return new IllegalArgumentException("Failed to find User by username");
        });
        Schedule schedule= Schedule.builder()
                .user(user)
                .name("TDD 테스트")
                .time(60)
                .sourceName("아주대")
                .destName("향촌롯데아파트")
                .startTime(Timestamp.valueOf("2022-12-12 13:00:00"))
                .readyTime(Timestamp.valueOf("2022-12-12 11:00:00"))
                .endTime(Timestamp.valueOf("2022-12-12 14:00:00"))
                .status("PRE")
                .build();
        scheduleRepository.save(schedule);
        assertThat(schedule).isNotNull();
        scheduleRepository.delete(schedule);
    }

    @Test
    //일정 업데이트 test
    void updateScheduleTest(){
        Random rnd= new Random();
        int newTime= rnd.nextInt(100);
        Schedule schedule=scheduleRepository.findById(43).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find Schedule by id: " + 43);
        });
        schedule.setName("최종 발표");
        schedule.setTime(newTime);
        scheduleRepository.save(schedule);
        assertThat(schedule.getName()).isEqualTo("최종 발표");

    }


}
