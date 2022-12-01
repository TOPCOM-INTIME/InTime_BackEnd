package com.topcom.intime.service;

import com.topcom.intime.Dto.FriendResDto;
import com.topcom.intime.Dto.FriendsReqDto;
import com.topcom.intime.Dto.FriendsReqListDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.Friends;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.FriendsRepository;
import com.topcom.intime.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class FriendsServiceTest {
    @Autowired
    private FriendsRepository friendsRepository;
    @Autowired
    private UserRepository userRepository;

    //닉네임 검색 test//
    @Test
    void searchTest(){
        FriendsReqDto friendsReqDto=new FriendsReqDto();
        friendsReqDto.setUsername("ja");
        List<User> users=userRepository.findAll();
        List<FriendsReqDto> results=new ArrayList<>();
        for(User user: users){
            if(user.getUsername()!=null&&user.getUsername().contains(friendsReqDto.getUsername())){
                FriendsReqDto friendsReqDto1= new FriendsReqDto();
                friendsReqDto1.setUsername(user.getUsername());
                results.add(friendsReqDto1);
            }
        }
        assertThat(results).hasSize(2); //jaehyeok과 jaehyeok2 조회 test
    }

    //친구 목록 조회 Test//
    @Test
    void getAllFriendsTest(){
        List<Friends> friends=friendsRepository.findAllByUserId(1);
        List<FriendResDto> names=new ArrayList<>();
        for(Friends friend: friends){
            List<User> users=userRepository.findAllById(friend.getFriendId());
            for(User user: users){
                if(friend.isAccepted()){
                    FriendResDto friendResDto= new FriendResDto();
                    friendResDto.setUsername(user.getUsername());
                    names.add(friendResDto);
                }
            }
        }
        assertThat(names).isNotEmpty();
    }

    //친구 삭제 test//
    @Test
    void deleteFriendsTest(){
        User friend=userRepository.findByUsername("jaehyeok").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않는 닉네임입니다."));
        Friends friends=friendsRepository.findByFriendIdAndUserId(friend.getId(), 1).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "친구 목록에 없는 계정을 삭제할 수 없습니다."));
        friendsRepository.delete(friends);
        Assert.assertFalse(friendsRepository.existsById(10)); //10번 친구관계 삭제됬는지 test, 10번 친구관계 삭제 안 됬을 시에 False 반환//
    }

    //친구 요청 Test//
    @Test
    void addFriendsTest(){
        User user=userRepository.findById(1).orElseThrow(()->new ResourceNotFoundException("User", "useridx", (long)1));
        User friend=userRepository.findByUsername("test").orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않는 닉네임입니다."));
        if(friendsRepository.existsByFriendIdAndUserId(friend.getId(), 1)){
            throw new APIException(HttpStatus.BAD_REQUEST, "이미 친구 요청이 된 상태입니다.");
        }
        Friends friends=new Friends();
        friends.setFriendId(friend.getId());
        friends.setAccepted(false);
        friends.setUser(user);
        friendsRepository.save(friends);

        assertThat(friends).isNotNull();
    }

    //친구 요청 수락 test//
    @Transactional
    @Test
    void acceptTest(){
        User user=userRepository.findById(1).orElseThrow(()->new ResourceNotFoundException("User", "useridx", (long)1));
        Friends friends=friendsRepository.findById(14).orElseThrow(()->new ResourceNotFoundException("Friends", "FriendsIdx", (long)14));
        if(friends.getFriendId()!=4){
            throw new APIException(HttpStatus.NOT_FOUND, "해당되는 친구 요청이 존재하지 않습니다");
        }
        friends.setAccepted(true);
        Friends friends2= new Friends();
        friends2.setFriendId(friends.getUser().getId());
        friends2.setAccepted(true);
        friends2.setUser(user);
        friendsRepository.save(friends);
        friendsRepository.save(friends2);

        assertAll(
                ()->assertThat(friends.isAccepted()).isEqualTo(true),
                ()->assertThat(friends2.isAccepted()).isEqualTo(true)
        );
    }

    //친구 요청 거절 test//
    @Test
    void denyTest(){
        Friends friends=friendsRepository.findById(13).orElseThrow(()->new ResourceNotFoundException("Friends", "FriendsIdx", (long)13));
        if(2!=friends.getFriendId()){
            throw new APIException(HttpStatus.NOT_FOUND, "해당되는 친구 요청이 존재하지 않습니다");
        }
        friendsRepository.delete(friends);
        Assert.assertFalse(friendsRepository.existsById(13));
    }

    //친구 요청 목록 가져오기 test//
    @Test
    void getAllRequestsTest(){
        List<Friends> friends=friendsRepository.findAllByFriendId(4);
        List<FriendsReqListDto> requests=new ArrayList<>();
        for(Friends friend: friends){
            List<User> users=userRepository.findAllById(friend.getUser().getId());
            for(User user: users){
                if(!friend.isAccepted()){
                    FriendsReqListDto friendsReqListDto= new FriendsReqListDto();
                    friendsReqListDto.setId(friend.getId());
                    friendsReqListDto.setUsername(user.getUsername());
                    requests.add(friendsReqListDto);
                }
            }
        }
        assertThat(requests).isNotEmpty();
    }
}
