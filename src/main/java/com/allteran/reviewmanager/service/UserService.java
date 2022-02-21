package com.allteran.reviewmanager.service;

import com.allteran.reviewmanager.domain.Role;
import com.allteran.reviewmanager.domain.User;
import com.allteran.reviewmanager.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Cant find user with name  " + username);
        }
        return user;
    }

    public boolean createUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if(userFromDb != null) {
            //means there is some user with such username
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    public User updateUser(User userFromDb, User user) {
        BeanUtils.copyProperties(user, userFromDb, "id");
        return userRepo.save(userFromDb);
    }

//    @PostConstruct
//    public void initUsers() {
//        User admin = new User();
//        admin.setId(10);
//        admin.setUsername("admin");
//        admin.setPassword("admin");
//        admin.setRoles(Collections.singleton(Role.ADMIN));
//        createUser(admin);
//
//        User user = new User();
//        user.setId(11);
//        user.setUsername("user");
//        user.setPassword("user");
//        user.setRoles(Collections.singleton(Role.USER));
//        createUser(user);
//    }

}
