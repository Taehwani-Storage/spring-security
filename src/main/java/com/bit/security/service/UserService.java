package com.bit.security.service;

import com.bit.security.model.UserDTO;
import org.apache.catalina.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private SqlSession sqlSession;

    private final String NAMESPACE = "mappers.UserMapper";

    public UserDTO loadByUserName(String username) {

        return sqlSession.selectOne(NAMESPACE + ".selectByUsername", username);
    }

    public void insert(UserDTO userDTO) {
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));

    }
}
