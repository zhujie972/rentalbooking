package com.laioffer.staybooking.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.laioffer.staybooking.exception.UserAlreadyExistException;
import com.laioffer.staybooking.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.laioffer.staybooking.repository.AuthorityRepository;
import com.laioffer.staybooking.repository.UserRepository;

import com.laioffer.staybooking.model.Authority;
import com.laioffer.staybooking.model.User;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class RegisterService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserAlreadyExistException {
//     第一种写法：
//        Optional<User> s  = userRepository.findById(user.getUsername());
//        if (s != null) {
//            throw new UserAlreadyExistException("User already exists");
//        } .. s.orElse 来设置 没有Optional 返回的情况
//     第二种写法：
        if (userRepository.existsById(user.getUsername())) {
            throw new UserAlreadyExistException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }


}
