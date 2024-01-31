package com.ssafy.lam.user.model.service;
import com.ssafy.lam.entity.User;
import com.ssafy.lam.user.model.repository.UserRepository;
import com.ssafy.lam.entity.TokenInfo;
import com.ssafy.lam.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    protected final UserRepository userRepository;

    protected final AuthenticationManagerBuilder authenticationManagerBuilder;

    protected final JwtTokenProvider jwtTokenProvider;

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUser(long seq) {
        return userRepository.findById(seq).orElse(null);
    }

    @Override
    /**
     * 참고 블로그     * https://suddiyo.tistory.com/entry/Spring-Spring-Security-JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-4
     * 1편부터 보면 좋다.
     */
    public User createUser(User user) {
        if(userRepository.existsById(user.getSeq()))
            throw new RuntimeException("이미 존재하는 고객입니다.");

        user = user.toEntity(user.getSeq(), user.getUserId(), user.getUsername(), user.getPassword(), user.getRoles());
//        user = user.toEntity(user.getSeq(), user.getUserId(), user.getPassword(), user.getRoles());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long seq, User updatedUser) {
//        User oldUser = updatedUser.toEntity(seq, updatedUser.getUserId(), updatedUser.getPassword(), updatedUser.getRoles());
        User oldUser = updatedUser.toEntity(seq, updatedUser.getUserId(), updatedUser.getUsername(), updatedUser.getPassword(), updatedUser.getRoles());
        return userRepository.save(oldUser);
    }

    @Override
    public void deleteUser(long seq) {
        userRepository.deleteById(seq);
    }

    @Override
    @Transactional
    public TokenInfo getLoginToken(User user) throws Exception {
        TokenInfo tokenInfo = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
            System.out.println("authenticationToken = " + authenticationToken);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            tokenInfo = jwtTokenProvider.generateToken(authentication);
        } catch (AuthenticationException e) {
            System.out.println("AuthenticationException");

            System.out.println("e.getMessage() = " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return tokenInfo;
    }

    @Override
    public User findByUserId(String userId) throws Exception {
        return userRepository.findByUserId(userId).orElse(null);
    }

//    @Override
//    public void logout() {
//
//    }
}