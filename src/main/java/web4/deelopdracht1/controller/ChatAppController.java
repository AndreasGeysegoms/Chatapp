package web4.deelopdracht1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import web4.deelopdracht1.domain.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import web4.deelopdracht1.service.PersonService;
import web4.deelopdracht1.domain.Status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatAppController {

    @Autowired
    private PersonService personService;
    @Autowired
    private ChatAppRestController restController;

    @RequestMapping("/")
    public String getIndex(Model model) {
        return "index.html";
    }

    @RequestMapping("/chat")
    public String getChat(HttpServletResponse response, HttpServletRequest request, Model model) throws IOException {
        HttpSession session = request.getSession();

        //restController.getFriends(user, response);
        return "chat.html";
    }



    @PostMapping
    @RequestMapping("/login")
    //TODO: errors showen niet meer
    public String login(@ModelAttribute(name = "email")  String email, @ModelAttribute(name = "password") String password, HttpServletRequest request, Model model) {
        List<String> errors = new ArrayList<String>();

        if (email == null || email.isEmpty()) {
            errors.add("No email given");
        }

        if (password == null || password.isEmpty()) {
            errors.add("No password given");
        }

        if (errors.size() == 0) {
            Person person = personService.getAuthenticatedUser(email, password);
            if (person != null) {
                HttpSession session = request.getSession();
                person.setStatus(Status.ONLINE.getString());
                session.setAttribute("user",person);
                personService.updatePersons(person);
            } else {
                errors.add("No valid email/password");
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }

        return "redirect:/";
    }

    @PostMapping
    @RequestMapping("/logout")
    public String logout(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Person offline = (Person) session.getAttribute("user");
        offline.setStatus("Offline");
        session.setAttribute("user",null);
        System.out.println(personService.getPersons().size());
        personService.updatePersons(offline);
        System.out.println(personService.getPersons().size());
        return "redirect:/";
    }
}
