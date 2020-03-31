package web4.deelopdracht1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web4.deelopdracht1.domain.Person;
import web4.deelopdracht1.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatAppRestController {

    @Autowired
    public ChatAppRestController(PersonService personService) {
        this.personService = personService;
    }

    private PersonService personService;

    @RequestMapping("/friendlist")
    @GetMapping
    public void getFriends(HttpServletResponse response, HttpServletRequest request, Model model) {
        System.out.println("refresh friends");
        Person user = (Person) request.getSession().getAttribute("user");
        ArrayList<Person> friends = (ArrayList<Person>) user.getFriends();

            try {
                String friendJSON = this.toJSON(friends);
                response.setContentType("application/json");
                response.getWriter().write(friendJSON);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


    }

    public String toJSON (List<Person> persons) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(persons);
    }

    @PostMapping
    @RequestMapping("/updateStatus")
    public void changeStatus(HttpServletRequest request , Model model) {
        String status = (String)request.getParameter("status");
        System.out.println("Kowalski status: "+status);
        if (!status.trim().isEmpty()) {
            HttpSession session = request.getSession();
            Person user = (Person) session.getAttribute("user");
            user.setStatus(status);
            session.setAttribute("user",user);
            personService.refresh(user);
        }
        else model.addAttribute("errors","Please choose a valid status.");

    }

    @PostMapping
    @RequestMapping("/addFriend")
    public void addFriend(Model model, HttpServletRequest request) {
        String email = (String) request.getParameter("email");
        System.out.println("Kowalski friend: "+email);
        email = email + "@ucll.be";
        HttpSession session = request.getSession();
        Person user = (Person) session.getAttribute("user");
        user = personService.addFriend(user, email);
        session.setAttribute("user",user);
        personService.refresh(personService.getPerson(email));
    }

}
