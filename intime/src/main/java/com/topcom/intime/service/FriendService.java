package com.topcom.intime.service;

import com.topcom.intime.Dto.DeleteFriendsReqDto;
import com.topcom.intime.Dto.FriendResDto;
import com.topcom.intime.Dto.FriendsReqDto;
import com.topcom.intime.Dto.FriendsReqListDto;
import com.topcom.intime.exception.APIException;
import com.topcom.intime.exception.ResourceNotFoundException;
import com.topcom.intime.model.Friends;
import com.topcom.intime.model.User;
import com.topcom.intime.repository.FriendsRepository;
import com.topcom.intime.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {
    private FriendsRepository friendsRepository;
    private UserRepository userRepository;

    public FriendService(FriendsRepository friendsRepository, UserRepository userRepository){
        this.friendsRepository=friendsRepository;
        this.userRepository=userRepository;
    }
    //특정 텍스트열로 닉네임 검색 api//
    public List<FriendsReqDto> searchFriends(int useridx, FriendsReqDto friendsReqDto){
        List<User> users=userRepository.findAll();
        List<FriendsReqDto> results=new ArrayList<>();
        for(User user: users){
            if(user.getUsername()!=null&&user.getUsername().contains(friendsReqDto.getUsername())){
                FriendsReqDto friendsReqDto1= new FriendsReqDto();
                friendsReqDto1.setUsername(user.getUsername());
                results.add(friendsReqDto1);
            }
        }
        return results;
    }
    //<예정 사항> 친구 목록 가져오기, 친구 추가, 친구 요청 목록 가져오기//
    //친구 목록 가져오기(닉네임으로?)
    public List<FriendResDto> getAllFriends(int useridx){
        List<Friends> friends=friendsRepository.findAllByUserId(useridx);
        List<FriendResDto> friendResDtoList=new ArrayList<>();
        for(Friends friend: friends){
            List<User> users=userRepository.findAllById(friend.getFriendId());
            for(User user: users){
                if(friend.isAccepted()){
                    FriendResDto friendResDto= new FriendResDto();
                    friendResDto.setId(user.getId());
                    friendResDto.setUsername(user.getUsername());
                    friendResDtoList.add(friendResDto);
                }
            }
        }
        return friendResDtoList;
    }
    //친구 삭제 API//
    @Transactional
    public void deleteFriends(int useridx, DeleteFriendsReqDto deleteFriendsReqDto){
        User friend=userRepository.findByUsername(deleteFriendsReqDto.getUsername()).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않는 닉네임입니다."));
        Friends friends=friendsRepository.findByFriendIdAndUserId(friend.getId(), useridx).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "친구 목록에 없는 계정을 삭제할 수 없습니다."));
        friendsRepository.delete(friends);
    }

    //친구 요청 날리는 API(아직 수락전)//
    public void addFriends(int useridx, String username){
        User user=userRepository.findById(useridx).orElseThrow(()->new ResourceNotFoundException("User", "useridx", (long)useridx));
        User friend=userRepository.findByUsername(username).orElseThrow(()->new APIException(HttpStatus.NOT_FOUND, "존재하지 않는 닉네임입니다."));
        if(friendsRepository.existsByFriendIdAndUserId(friend.getId(), useridx)){
            throw new APIException(HttpStatus.BAD_REQUEST, "이미 친구 요청이 된 상태입니다.");
        }
        Friends friends=new Friends();
        friends.setFriendId(friend.getId());
        friends.setAccepted(false);
        friends.setUser(user);
        friendsRepository.save(friends);
    }
    //친구 요청 수락 API//
    @Transactional
    public void accept(int useridx, int friendsIdx){
          User user=userRepository.findById(useridx).orElseThrow(()->new ResourceNotFoundException("User", "useridx", (long)useridx));
          Friends friends=friendsRepository.findById(friendsIdx).orElseThrow(()->new ResourceNotFoundException("Friends", "FriendsIdx", (long)friendsIdx));
          if(useridx!=friends.getFriendId()){
              throw new APIException(HttpStatus.NOT_FOUND, "해당되는 친구 요청이 존재하지 않습니다");
          }
          friends.setAccepted(true);
          Friends friends2= new Friends();
          friends2.setFriendId(friends.getUser().getId());
          friends2.setAccepted(true);
          friends2.setUser(user);
          friendsRepository.save(friends);
          friendsRepository.save(friends2);
   }
    //친구 요청 거절 API//
    public void deny(int useridx, int friendsIdx){
        Friends friends=friendsRepository.findById(friendsIdx).orElseThrow(()->new ResourceNotFoundException("Friends", "FriendsIdx", (long)friendsIdx));
        if(useridx!=friends.getFriendId()){
            throw new APIException(HttpStatus.NOT_FOUND, "해당되는 친구 요청이 존재하지 않습니다");
        }
        friendsRepository.delete(friends);
    }
    //친구 요청 목록 조회 API
    public List<FriendsReqListDto> getAllRequests(int useridx){
        List<Friends> friends=friendsRepository.findAllByFriendId(useridx);
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
        return requests;
    }

}
