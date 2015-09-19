package com.senacor.hackingdays.distributedcache.db;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.senacor.hackingdays.distributedcache.domain.Stuff;

public interface StuffMapper {
	
	@Select("select * from stuff where id=#{id}")
	Stuff getStuffById(int id);
	
	@Select("select * from stuff")
	List<Stuff> getAllStuff();
	
	@Insert("insert into stuff (name) values (#{name})")
	@SelectKey(statement="call identity()", keyProperty="id", before=false, resultType = int.class)
	void createStuff(Stuff stuff);

}
