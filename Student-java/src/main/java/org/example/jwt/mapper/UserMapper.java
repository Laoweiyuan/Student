package org.example.jwt.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.example.jwt.entity.User;


@Mapper
public interface UserMapper {
    @DS("User")
    User findByUsername(String username);//4.UserMapper.xml映射文件中定义的查询语句
    @DS("User")
    int insert(User user);//5.UserMapper.xml映射文件中定义的插入语句
}