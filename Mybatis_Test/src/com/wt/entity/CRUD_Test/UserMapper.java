package com.wt.entity.CRUD_Test;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wt.entity.test1.Users;

// 使用注解的方式实现对数据库表的相关操作
public interface UserMapper {
	
	@Insert("insert into users(name, age) values(#{name}, #{age})")
	public int addUser(Users users);
	
	@Delete("delete from users where id = #{id}")
	public int deleteById(int id);
	
	@Update("update users set name = #{name}, age = #{age} where id = #{id}")
	public int updateUser(Users users);
	
	@Select("select * from users where id = #{id}")
	public Users getById(int id);
	
	@Select("select * from users")
	public List<Users> getAllusers();
	
}
