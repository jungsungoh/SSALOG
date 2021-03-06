package com.ssalog.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssalog.config.webhook;
import com.ssalog.dto.GroupDTO;
import com.ssalog.dto.GroupRole;
import com.ssalog.dto.PageRequest;
import com.ssalog.service.GroupService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping
public class GroupController {
	
	@Autowired
	GroupService groupService;
	
	@PostMapping("user/grouping/make_group")
	@ApiOperation(value = "[그룹 만들기] 새로운 그룹을 만든다.")
	public ResponseEntity<String> make_group(HttpServletResponse response,@RequestBody GroupDTO groupdto) {
		String username = response.getHeader("username"); 
		String result = groupService.makeGroup(groupdto, username);
		if(result.equals("is_exist")) {
			return new ResponseEntity<String>(result,HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<String>(result, HttpStatus.OK);
		}
	}
	
	@PostMapping("user/grouping/apply_group")
	@ApiOperation(value = "[그룹 가입신청] 그룹에 가입을 신청합니다.")
	public ResponseEntity<String> apply_group(HttpServletResponse response, @RequestParam("groupname") String groupname, @RequestParam(required = false) String introduce) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<String>(groupService.applyGroup(groupname, username, introduce),HttpStatus.OK);
	}
	
	@GetMapping("user/grouping/apply_list")
	@ApiOperation(value = "[그룹 가입신청 목록보기] 내 그룹에 가입신청한 목록을 봅니다.")
	public ResponseEntity<List<Map<String,String>>> apply_list(HttpServletResponse response, @RequestParam("groupname") String groupname) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<List<Map<String,String>>>(groupService.appliylist(username, groupname),HttpStatus.OK);
	}
	

	@PostMapping("user/grouping/apply_accept")
	@ApiOperation(value = "[그룹 가입신청 수락] 내 그룹에 신청한 가입을 수락합니다.")
	public ResponseEntity<String> apply_accept(HttpServletResponse response,@RequestParam("groupname") String groupname, @RequestParam("regid") Long regid) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<String>(groupService.applyaccept(username, groupname, regid),HttpStatus.OK);
	}
	
	@PostMapping("user/grouping/make_goal")
	@ApiOperation(value = "[그룹목표 설정] 그룹장이 목표에 해당하는 문제를 설정한다.")
	public ResponseEntity<String> make_goal(HttpServletResponse response,@RequestParam("groupname") String groupname,@RequestParam("problemid") String problemid,@RequestParam("problemname") String problemname,@RequestParam("limitday") @DateTimeFormat(iso = ISO.DATE_TIME) Date limit, @RequestParam(required = false) String mention) {
		String username = response.getHeader("username"); 
		System.out.println("limit = " + limit);
		return new ResponseEntity<String>(groupService.makeGoal(username, groupname, problemid, problemname, limit, mention),HttpStatus.OK);
	}
	@GetMapping("newuser/grouping/check_goal")
	@ApiOperation(value = "[그룹원들이 얼마나 풀었는지 확인합니다.] 내 그룹에 가입신청한 목록을 봅니다.")
	public ResponseEntity<Map<String, String>> check_goal(@RequestParam("groupname") String groupname, @RequestParam("problemid") String problemid) {
		return new ResponseEntity<Map<String, String>>(groupService.checkGoal(groupname, problemid),HttpStatus.OK);
	}
	
	@DeleteMapping("user/grouping/apply_reject")
	@ApiOperation(value = "[그룹 가입신청 거절] 내 그룹에 신청한 가입을 거절합니다.")
	public ResponseEntity<String> apply_reject(HttpServletResponse response, @RequestParam("groupname") String groupname, @RequestParam("regid") Long regid) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<String>(groupService.applyreject(username,groupname, regid),HttpStatus.OK);
	}
	
	@GetMapping("user/grouping/myGrouplist")
	@ApiOperation(value = "[그룹 보기] 내가 가입한 그룹목록을 보여줍니다. 사용자 입장")
	public ResponseEntity<?> myGroup(HttpServletResponse response) {
		String username = response.getHeader("username"); 
		Map<String, GroupRole> m = groupService.myGroup(username);
		return new ResponseEntity<Map<String, GroupRole>>(m,HttpStatus.OK);
	}
	@GetMapping("user/grouping/pre_goal_list")
	@ApiOperation(value = "[그룹에서 만료시간이 안지난 문제보기] 그룹목표문제 중 만료시간이 안지난 문제리스트를 보여줍니다.. ")
	public ResponseEntity<List<Map<String,Object>>> pre_goal(HttpServletResponse response, @RequestParam("groupname") String groupname, PageRequest pageable) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<List<Map<String,Object>> >(groupService.preGoal(username, groupname, pageable.of()),HttpStatus.OK);
	}
	@GetMapping("user/grouping/post_goal_list")
	@ApiOperation(value = "[그룹에서 만료시간이 지난 문제보기] 그룹목표문제 중 만료시간이 지난 문제리스트를 보여줍니다.. ")
	public ResponseEntity<List<Map<String,Object>>> post_goal(HttpServletResponse response, @RequestParam("groupname") String groupname, PageRequest pageable) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<List<Map<String,Object>>>(groupService.postGoal(username, groupname, pageable.of()),HttpStatus.OK);
	}
	
	@GetMapping("user/grouping/group_member")
	@ApiOperation(value = "[그룹내 멤버보기]내 그룹의 멤버가 누군지 봅시다 ")
	public ResponseEntity<List<Map<String,Object>>> mymember(HttpServletResponse response, @RequestParam("groupname") String groupname) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<List<Map<String,Object>>>(groupService.Mymember(username, groupname),HttpStatus.OK);
	}
	
	@DeleteMapping("user/grouping/kick_member")
	@ApiOperation(value = "[그룹강퇴하기] (그룹장만 이용가능, 그룹장은 강퇴불가!) 그룹이름과, 강퇴하고 싶음 사람의 닉네임을 주면, 그룹에서 추방한다. 문제없이 작동하면 true, 중간에 조건이 안맞으면 false")
	public ResponseEntity<Boolean> kick_member(HttpServletResponse response, @RequestParam("groupname") String groupname, @RequestParam("wantkick") String wantkick) {
		String username = response.getHeader("username"); 
		return new ResponseEntity<Boolean>(groupService.group_kick(username, groupname, wantkick),HttpStatus.OK);
	}
	@GetMapping("newuser/grouping/search_group")
	@ApiOperation(value = "[그룹 이름 검색] 그룹이름으로 그룹을 검색합니다!")
	public ResponseEntity<List<Map<String, String>>> search_group(@RequestParam(required=false) String groupname, PageRequest pageable) {
		return new ResponseEntity<List<Map<String, String>>>(groupService.findGroup(groupname, pageable.ofs2()),HttpStatus.OK);
	}
	@GetMapping("user/grouping/check_role")
	@ApiOperation(value = "[그룹 권한 체크] 그룹이름과 nickname을 줘서 해당 사용자의 권한을 확인합니다. owner, member두개가 존재하고 둘다에 해당하지 않으면 false를 return합니다.")
	public ResponseEntity<String> check_role(@RequestParam("groupname") String groupname,@RequestParam("nickname") String nickname) {
		return new ResponseEntity<String>(groupService.Check_Role(groupname, nickname),HttpStatus.OK);
	}
	@DeleteMapping("user/grouping/delete_goal")
	@ApiOperation(value = "[그룹의 목표를 제거합니다.] 해당 goal의 id를 줘서 제거합니다. 관리자만이 삭제할 수 있습니다.")
	public ResponseEntity<String> delete_goal(HttpServletResponse response,@RequestParam("id") Long id) {
		String username = response.getHeader("username");
		return new ResponseEntity<String>(groupService.delete_goal(username, id),HttpStatus.OK);
	}
	
	@DeleteMapping("user/grouping/delete_group")
	@ApiOperation(value = "[그룹을 삭제합니다.] 그룹이름을 줘서 삭제합니다, 관리자만이 삭제할 수 있습니다.")
	public ResponseEntity<String> delete_goal(HttpServletResponse response,@RequestParam("groupname") String groupname) {
		String username = response.getHeader("username");
		return new ResponseEntity<String>(groupService.delete_group(username, groupname),HttpStatus.OK);
	}
	@ExceptionHandler(Exception.class)
	public void nullex(HttpServletResponse response,Exception e) {
		String username = response.getHeader("username");
		System.err.println("그룹 부분에서 " + e.getClass());
		webhook w = new webhook();
		if(username != null) {
			w.send(username + "이 사고치는중!\n 그룹 부분에서 " + e.getClass());
		}else {
			w.send("그룹 부분에서 " + e.getClass());
		}
	}
}
