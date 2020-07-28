package world.lixiang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import world.lixiang.entity.User;
import world.lixiang.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/login")
    public String login(User user , HttpSession httpSession){
        User user1 =  userService.login(user);
        if(user1 != null){
            httpSession.setAttribute("user",user1);
            return "redirect:/showAll";
        }else {
            return "redirect:/index";
        }
    }
}
