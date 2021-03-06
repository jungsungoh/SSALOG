package com.ssalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssalog.dto.Groupdetail;

public interface GroupDetailRepository extends JpaRepository<Groupdetail, Long>{
	public List<Groupdetail> findByGroupdto_groupname(String groupname);
	public List<Groupdetail> findByAccount_username(String username);
	public Groupdetail findByAccount_nicknameAndGroupdto_groupname(String username, String groupname);
	public Groupdetail findByAccount_usernameAndGroupdto_groupname(String username, String groupname);
}
