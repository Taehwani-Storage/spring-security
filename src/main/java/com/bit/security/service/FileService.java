package com.bit.security.service;

import com.bit.security.model.FileDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private SqlSession sqlSession;

    private final String NAMESPACE = "mappers.FileMapper";

    public void insert(FileDTO fileDTO) {
        sqlSession.insert(NAMESPACE + ".insert", fileDTO);

    }

    public List<FileDTO> selectAll() {
        return sqlSession.selectList(NAMESPACE + ".selectAll");
    }
}
