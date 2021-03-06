package com.ssalog.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssalog.dto.Account;
import com.ssalog.dto.Comment;
import com.ssalog.dto.Post;
import com.ssalog.repository.AccountRepository;
import com.ssalog.repository.PostRepository;


@Service
public class CommentServiceImpl implements CommentService{

	@Autowired
	PostRepository postRepository;
	@Autowired
	AccountRepository accountRepository;
	@Override
	@Transactional
	public int write_comment(String post_pk,String comment,String username) {
		Optional<Post> p = postRepository.findById(post_pk);
		if(p.isPresent()) {
			Post p1 = p.get();
			List<Comment> clist = p1.getComment();
			if(clist == null) {
				clist = new ArrayList<>();
			}
			String uid = username+UUID.randomUUID().toString();
			Comment c = new Comment();
			c.setMessage(comment);
			c.setUniqueid(uid);
			c.setUsername(username);
			Account ac=  accountRepository.findByUsername(username);
			c.setNickname(ac.getNickname());
			Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time1 = format1.format(date);
			c.setTime(time1);
			clist.add(c);
			p1.setComment(clist);
			postRepository.save(p1);
			return 1;
		}
		else {
			return 2;
		}
	}

	@Override
	public List<Comment> read_comment(String Scoring){
		Post p = postRepository.findByScoring(Scoring);
		List<Comment> list = null;
		if(p == null) {
			return null;
		}else {
			list = p.getComment();
		}
		if(list == null) {
			return new ArrayList<Comment>();
		}else {
			for(int i=0; i<list.size(); i++) {
				Account ac =accountRepository.findByUsername(list.get(i).getUsername());
				if(ac != null) {
					list.get(i).setNickname(ac.getNickname());
					list.get(i).setImgpath(ac.getImgpath()==null?"default.png":ac.getImgpath());
				}
			}
			return list;
		}
	}
	public void delete_comment(String id) {
		postRepository.delete_comment(id);
	}
	public void update_comment(String message,String id,String username) {
		Date date = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time1 = format1.format(date);
		Account ac = accountRepository.findByUsername(username);
		Comment comment = new Comment();
		comment.setMessage(message);
		comment.setImgpath(ac.getImgpath());
		comment.setNickname(ac.getNickname());
		comment.setTime(time1);
		comment.setUsername(username);
		postRepository.update_comment(comment,id);
	}
	public void write_subcomment(String id, Comment comment) {
		postRepository.write_subcomment(id, comment);
	}
	public void update_subcomment(String id, Comment comment) {
		postRepository.update_subcomment(id, comment);
	}
}
