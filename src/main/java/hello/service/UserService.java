package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {


    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Map<String,User> users = new ConcurrentHashMap<>();
    private UserMapper userMapper;
    public void save(String username,String password) {
        userMapper.save(username,bCryptPasswordEncoder.encode(password));
    }
    public User getUserByUserName(String username) {

        return userMapper.findUserByUsername(username);
    }

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
//        save("user","password");
    }

    public User getUserById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUserName(username);
        if(user == null) {
            throw new UsernameNotFoundException(username+"不存在!");
        }


        return new org.springframework.security.core.userdetails.User(username,user.getEncryptedPassword(), Collections.emptyList());
    }
}
