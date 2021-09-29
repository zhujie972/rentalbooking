package com.laioffer.staybooking.service;
// created @ 09/25 lecture 28
import com.laioffer.staybooking.util.JwtUtil;
import org.springframework.stereotype.Service;

import com.laioffer.staybooking.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.laioffer.staybooking.exception.UserNotExistException;
import com.laioffer.staybooking.model.Authority;
import com.laioffer.staybooking.model.Token;
import com.laioffer.staybooking.model.User;
import com.laioffer.staybooking.model.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;


@Service
public class AuthenticationService {

    private AuthenticationManager authenticationManager;
    private AuthorityRepository authorityRepository; //用于验证 host guest权限
    private JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, AuthorityRepository authorityRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.authorityRepository = authorityRepository;
        this.jwtUtil = jwtUtil;
    }

    public Token authenticate(User user, UserRole role) throws UserNotExistException {
        //检查用户密码
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException exception) { //authenticate 方法 失败会 出异常
            throw new UserNotExistException("User Doesn't Exist");
        }
        //检查 用户权限
        // findById返回的是 optional， 存在就有对象，没有的话你需要自己设置 （.orElse方法）
        Authority authority = authorityRepository.findById(user.getUsername()).orElse(null);
        if (!authority.getAuthority().equals(role.name())) {
            throw new UserNotExistException("User Doesn't Exist");
        }
        //生成token 返回
        return new Token(jwtUtil.generateToken(user.getUsername()));
    }


}
