package com.zhaihuilin.controller;

import com.zhaihuilin.config.EmailConfig;
import com.zhaihuilin.entity.*;
import com.zhaihuilin.service.MemberService;
import com.zhaihuilin.service.RoleService;
import com.zhaihuilin.service.SmsCodeService;
import com.zhaihuilin.util.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by zhaihuilin on 2017/11/14  10:37.
 */
@RestController
@Log4j
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SendEmailUtils sendEmailUtils;

    @Autowired
    private EmailConfig  emailConfig;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SmsCodeService smsCodeService;


    /**
     * 发送邮箱验证码
     * @param email   接收者的邮箱 【注册人】
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendCode")
    public  String sendCode(
            @RequestParam(value = "email",defaultValue = "",required = true) String email,
            HttpServletRequest request ){
        try {
            String code =RandomTools.randomCode();//产生随机的验证码
            request.getSession().setAttribute("registerCode",code);
            //发送邮件开始   发送验证码
            log.info("开始发送邮件验证码------");
            sendEmailUtils.sendRegisterCode(email,code);
        }catch (Exception e){
            e.printStackTrace();
            return  "0";
        }
        return "1";
    }


    /**
     * 注册
     * @param email    邮箱
     * @param username   用户名
     * @param nickname  昵称
     * @param smscode  电话号码
     * @param password  密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/register")
    public ReturnMessages register(
            @RequestParam(value = "email",defaultValue = "",required = true) String email,
            @RequestParam(value = "username",defaultValue = "",required = true) String username,
            @RequestParam(value = "nickname",defaultValue = "",required = false) String nickname,
            @RequestParam(value = "smscode",defaultValue = "",required = false) String smscode,
            @RequestParam(value = "password",defaultValue = "",required = true) String password,
            HttpServletRequest request
    ){
            ReturnMessages  returnMessages=null;
            if (!StringUtils.isNotEmpty(email)){
               return   new  ReturnMessages(RequestState.ERROR,"邮箱不能为空！",null);
            }
            if (!StringUtils.isNotEmpty(username)){
                return   new  ReturnMessages(RequestState.ERROR,"用户名不能为空！",null);
            }
            if (memberService.existMemberByemail(email)){
                return   new  ReturnMessages(RequestState.ERROR,"邮箱已存在！",null);
            }
            if (memberService.existMemberByusername(username)){
                return   new  ReturnMessages(RequestState.ERROR,"用户名已存在！",null);
            }
            if (!StringUtils.isNotEmpty(password)){
                return   new  ReturnMessages(RequestState.ERROR,"密码不能为空！",null);
            }
            // 此处测试的时候 无法获取到 session 值
//            String registerCode = (String) request.getSession().getAttribute("registerCode");
//            if (!registerCode.equals(code)){
//                return   new  ReturnMessages(RequestState.ERROR,"验证码输入不正确！",null);
//            }
            String pwd = RandomTools.randomCode(); //随机密码
            try {
                Member member=new Member();
                member.setEmail(email);
                member.setNickname(nickname);
                member.setState(StateConstant.USER_STATE_CHECK_ING.toString());
                member.setUsername(username);
                member.setPassword(password);
                if (StringUtils.isNotEmpty(smscode)) {
                    member.setSmscode(smscode);
                }
                member.setPassword(SecurityUtil.md5(email,pwd));
                member=memberService.saveMember(member);
                if (member !=null){
                    Role role=roleService.getRoleByDefaule();//获取默认的角色
                    List<Role> roles =new ArrayList<Role>();
                    roles.add(role);
                    roleService.setMemberRole(member.getUsername(),roles);
                    //注册成功后 通知注册人
                    sendEmailUtils.sendRegisterSuc(email,pwd, emailConfig.URL+"?username="+username+"&code="+pwd);//注册成功后

                    //注册成功后 通知管理员
                    sendEmailUtils.sendOnRegister(emailConfig.MEMEMAIL,username,email,"#");//注册成功后
                    return   new  ReturnMessages(RequestState.SUCCESS,"注册成功！",member);
                }else{
                    return   new  ReturnMessages(RequestState.ERROR,"注册失败！",null);
                }
            } catch (Exception e) {
                return   new  ReturnMessages(RequestState.ERROR,"注册失败！",null);
            }
    }

    /**
     *   编辑 用户信息
     * @param memberId  编号
     * @param nickname   昵称
     * @param smscode   电话
     * @param email    邮箱
     * @param rids     角色编号
     * @return
     */
    @RequestMapping(value = "/updateMember")
    public  ReturnMessages updateMember(
            @RequestParam(value = "memberId",defaultValue = "",required = false) String memberId,
            @RequestParam(value = "nickname",defaultValue = "",required = false) String nickname,
            @RequestParam(value = "smscode",defaultValue = "",required = false) String smscode,
            @RequestParam(value = "email",defaultValue = "",required = false) String email,
            @RequestParam(value = "rid",defaultValue = "",required = false) String[] rids,
            HttpServletRequest request
    ){
            ReturnMessages returnMessages=null;
            if (!StringUtils.isNotEmpty(memberId)){
                return new ReturnMessages(RequestState.ERROR,"编号不能为空！",null);
            }
            Member member=memberService.findMemberByid(memberId);
            if(member ==null){
                return new ReturnMessages(RequestState.ERROR,"用户不存在！",null);
            }
            if (StringUtils.isNotEmpty(nickname)){
                member.setNickname(nickname);
            }
            if (StringUtils.isNotEmpty(smscode)){
                member.setSmscode(smscode);
            }
            if (StringUtils.isNotEmpty(email)){
                member.setEmail(email);
            }
            if (rids !=null && rids.length>0){
                 List<Role> roles =new ArrayList<Role>();
                 for (String rid:rids){
                   Role role=  roleService.findRoleById(Integer.parseInt(rid));
                   if (role !=null){
                       roles.add(role);
                   }
                 }
                 roleService.setMemberRole(member.getUsername(),roles);
            }
            member= memberService.updateMember(member);
            if (member !=null){
                if (StringUtils.isNotEmpty(smscode)){
                    String contant = member.getUsername()+"您好！您在个人博客上的预留手机号已更改为:"+smscode;
                    HashMap<String, String> pp = (HashMap<String, String>) SmsSendUtils.singleMt(smscode, contant);
                    Map<String, Object> map = new HashMap<String,Object>();
                    try {
                        if (pp !=null){
                            SmsCode smsCode=new SmsCode();
                            smsCode.setPhone(smscode);
                            smsCode.setRequestTime(new Date().getTime());
                            smsCode.setSa(SmsConstant.SA);
                            smsCode.setMtMsgId(pp.get("mtmsgid"));
                            smsCode.setState(pp.get("mtstat"));
                            smsCode.setErrCode(pp.get("mterrcode"));
                            smsCode=smsCodeService.saveSmsCode(smsCode);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return   new  ReturnMessages(RequestState.SUCCESS,"编辑成功！",member);
            }else{
                return   new  ReturnMessages(RequestState.ERROR,"编辑失败！",null);
            }
    }

    /**
     * 修改密码
     * @param code          初始密码
     * @param newPassword   新密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePwd")
    public  ReturnMessages updatePwd(
            @RequestParam(value = "olePassword",defaultValue = "",required = true) String code,
            @RequestParam(value = "username",defaultValue = "",required = true) String username,
            @RequestParam(value = "newPassword",defaultValue = "",required = true) String newPassword,
            HttpServletRequest request
    ){
            ReturnMessages returnMessages=null;
            if (!StringUtils.isNotEmpty(code)){
                return   new  ReturnMessages(RequestState.ERROR,"初始密码不能为空！",null);
            }
            if (!StringUtils.isNotEmpty(newPassword)){
                return   new  ReturnMessages(RequestState.ERROR,"新密码不能为空！",null);
            }
            Member member = memberService.findMemberByusername(username);
            if (member ==null){
                return   new  ReturnMessages(RequestState.ERROR,"该用户不存在！",null);
            }
            try {
                if ( StringUtils.isNotEmpty(code) && StringUtils.isNotEmpty(newPassword)){
                     member.setPassword(SecurityUtil.md5(member.getEmail(),newPassword));
                     member.setEmailCode(code);
                     member=memberService.updateMember(member);
                    if (member !=null){
                        return   new  ReturnMessages(RequestState.SUCCESS,"修改密码成功！",member);
                    }else{
                        return   new  ReturnMessages(RequestState.ERROR,"修改密码失败！",null);
                    }
                }else{
                    return   new  ReturnMessages(RequestState.ERROR,"修改密码失败！",null);
                }
            }catch (Exception e){
                return   new  ReturnMessages(RequestState.ERROR,"修改密码失败！",null);
            }
    }

    /**
     * 找回密码
     * @param email     用户邮箱
     * @param request
     * @return
     */
    @RequestMapping(value ="/findPwd")
    public  ReturnMessages findPwd(
            @RequestParam(value = "email",defaultValue = "",required = true) String email,
            HttpServletRequest request
    ){
           ReturnMessages returnMessages=null;
           if (!StringUtils.isNotEmpty(email)){
               return   new  ReturnMessages(RequestState.ERROR,"邮箱不能为空！",null);
           }
           Member member= memberService.findMemeberByemail(email);
           if (member ==null){
                return   new  ReturnMessages(RequestState.ERROR,"该用户不存在！",null);
           }
           try {
               String url="http:192.168.0.17:8080/member/login";
               String randPwd = RandomTools.randomCode();//随机产生一个密码
               member.setPassword(SecurityUtil.md5(email,randPwd));
               member=memberService.updateMember(member);
               //发送邮件通知 用户
               sendEmailUtils.sendFindPwdSuc(email,randPwd,url);
               return   new  ReturnMessages(RequestState.SUCCESS,"找回密码成功！",member);
           }catch (Exception e){
               return   new  ReturnMessages(RequestState.ERROR,"找回密码失败！",null);
           }
    }

    /**
     * 登录
     * @param username    用户名
     * @param password   密码
     * @param request
     * @return
     */
    @RequestMapping(value = "/login")
    public  ReturnMessages login(
            @RequestParam(value = "username",defaultValue = "",required = true) String username,
            @RequestParam(value = "password",defaultValue = "",required = true) String password,
            HttpServletRequest request
    ){
        ReturnMessages returnMessages=null;
        try {
            if (!StringUtils.isNotEmpty(username)){
                return   new  ReturnMessages(RequestState.ERROR,"用户名不能为空！",null);
            }
            if (!StringUtils.isNotEmpty(password)){
                return   new  ReturnMessages(RequestState.ERROR,"密码不能为空！",null);
            }
            Member member=memberService.findMemberByusername(username);
            if (member== null){
                return   new  ReturnMessages(RequestState.ERROR,"用户不存在！",null);
            }
            String pwd=SecurityUtil.md5(member.getEmail(),password);
            if (member.getPassword().equals(pwd)){
                request.setAttribute("username",username);
                return   new  ReturnMessages(RequestState.SUCCESS,"登录成功！",null);
            }else{
                return   new  ReturnMessages(RequestState.ERROR,"密码错误,登录失败！",null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return   new  ReturnMessages(RequestState.ERROR,"登录失败！",null);
        }
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @RequestMapping(value = "/findMe")
    public  ReturnMessages findMe(HttpServletRequest request){
        ReturnMessages returnMessages=null;
        String username= HttpRequestUtils.getUsername(request);
        if (!StringUtils.isNotEmpty(username)){
            return new ReturnMessages(RequestState.ERROR,"该用户没登录！",null);
        }
        Member member=memberService.findMemberByusername(username);
        if (member !=null){
            return new ReturnMessages(RequestState.SUCCESS,"有数据！",member);
        }else{
            return new ReturnMessages(RequestState.ERROR,"该用户不存！",null);
        }
    }

}
