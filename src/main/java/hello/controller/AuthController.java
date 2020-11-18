package hello.controller;

import hello.entity.User;
import hello.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    @Inject
    public AuthController( AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
       String userName =  SecurityContextHolder.getContext().getAuthentication().getName();
       User loggedInUser = userService.getUserByUserName(userName);
       if(loggedInUser == null){
           return new Result("ok","没有",false);
       }
       return new Result("ok",null,true,loggedInUser);

    }
    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,String> usernameAndPasswordJson) {
        System.out.println("----login------");
        System.out.println(usernameAndPasswordJson);
        String username = usernameAndPasswordJson.get("username").toString();
        String password = usernameAndPasswordJson.get("password").toString();
        UserDetails userDetails = null;
        try {
             userDetails =  userService.loadUserByUsername(username);

        } catch (UsernameNotFoundException e) {
            return new Result("fail","用户不存在",false);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok","登录成功",true,userService.getUserByUserName(username));
        } catch (BadCredentialsException e) {
            return new Result("fail","密码不正确",false);
        }


    }
    private static class Result {
        String status;
        String msg;
        boolean isLogin;
        Object data;

        public Object getData() {
            return data;
        }

        public Result(String status, String msg, boolean isLogin) {
            this(status,msg,isLogin,null);
        }

        public Result(String status, String msg, boolean isLogin, Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public boolean isLogin() {
            return isLogin;
        }
    }
}
