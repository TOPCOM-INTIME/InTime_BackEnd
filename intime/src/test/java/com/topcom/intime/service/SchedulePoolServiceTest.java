package com.topcom.intime.service;

import com.topcom.intime.exception.APIException;
import com.topcom.intime.model.Schedule;
import com.topcom.intime.model.SchedulePool;
import com.topcom.intime.model.SchedulePoolMembers;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.SchedulePoolMembersRepository;
import com.topcom.intime.repository.SchedulePoolRepository;
import com.topcom.intime.repository.ScheduleRepository;
import com.topcom.intime.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@WebAppConfiguration
class SchedulePoolServiceTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SchedulePoolRepository schedulePoolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchedulePoolMembersRepository schedulePoolMembersRepository;
    //단체 일정 추가 Test//
    @Test
    void save_schedulePoolTest(){
        Schedule schedule=scheduleRepository.findById(9).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find Schedule by id: " + 9);
        });
        User user=userRepository.findById(3).orElseThrow(()->{
            return new IllegalArgumentException("Failed to find User by id: " + 3);
        });
        List<Schedule> scheduleList=new ArrayList<>();
        scheduleList.add(schedule);

        SchedulePool schedulePool=new SchedulePool();
        schedulePool.setLeader(user);
        schedulePool.setSchedules(scheduleList);
        schedulePoolRepository.save(schedulePool);
        SchedulePoolMembers members= new SchedulePoolMembers();
        members.setStatus("LEADER");
        members.setSchedulePool(schedulePool);
        members.setUser(user);
        assertAll(
                ()->assertThat(schedulePool).isNotNull(),
                ()->assertThat(members).isNotNull()
        );
        schedulePoolRepository.delete(schedulePool);
        schedulePoolMembersRepository.delete(members);

    }

    //단체 일정 초대 Test(2번 유저)//
    @Test
    void inviteMembersTest(){
        SchedulePool schedulePool=schedulePoolRepository.findById(1).orElseThrow(() -> {
            return new IllegalArgumentException("Failed to find SchedulePool by id: " + 1);
        });
        User user=userRepository.findById(2).orElseThrow(() -> {
            return new IllegalArgumentException("Failed to find User by id: " + 2);
        });
        SchedulePoolMembers members=new SchedulePoolMembers();
        members.setStatus("INVITING");
        members.setSchedulePool(schedulePool);
        members.setUser(user);
        schedulePoolMembersRepository.save(members);
        assertThat(members).isNotNull();
        schedulePoolMembersRepository.delete(members);
    }

    //단체 일정 수락 TEST//
    @Test
    void acceptScheduleTest(){
        SchedulePoolMembers member=schedulePoolMembersRepository.findBySchedulePoolIdAndUserId(1, 2).orElseThrow(() -> {
            return new IllegalArgumentException("Failed to find members by id: " + 1);
        });
        member.setStatus("OK");
        schedulePoolMembersRepository.save(member);
        assertThat(member.getStatus()).isEqualTo("OK");
    }
}
