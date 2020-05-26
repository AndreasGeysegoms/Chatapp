package web4.deelopdracht1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web4.deelopdracht1.db.ChatBerichtenRepository;
import web4.deelopdracht1.domain.Chatbericht;
import web4.deelopdracht1.domain.Person;
import web4.deelopdracht1.domain.Role;
import web4.deelopdracht1.service.ChatBerichtenService;
import web4.deelopdracht1.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class ChatAppRestController {

    @Autowired
    private PersonService personService;
    @Autowired
    private ChatBerichtenService chatService;

    @RequestMapping("/friendlist")
    @GetMapping
    public void getFriends(HttpServletResponse response, HttpServletRequest request, Model model) {
        System.out.println("refresh friends");
        Person user = (Person) request.getSession().getAttribute("user");

        Person userDb = (Person) personService.getPerson(user.getUserId());
        List<Person> friends = userDb.getFriends();
        List<Person> updated = new ArrayList<>();
        System.out.println("#vrienden: " + friends.size());
        if (friends.size() == 0) {
            try {
                String friendJSON = "[{\"firstName\":\"Uw vriendenlijst is leeg.\",\"status\":\"\"}, {\"firstName\":\"Voeg meer vrienden toe!\",\"status\":\"\"}]";
                response.setContentType("application/json");
                response.getWriter().write(friendJSON);
                return;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Person friend : friends) {
            Person temp = new Person(personService.getPerson(friend.getUserId()));
            updated.add(temp);
            System.out.println(temp.getUserId() + ", status: " + temp.getStatus());
        }
        try {
            String friendJSON = this.toJSON(updated);
            response.setContentType("application/json");
            response.getWriter().write(friendJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String toJSON(List<Person> persons) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(persons);
    }

    @PostMapping
    @RequestMapping("/updateStatus")
    public void changeStatus(HttpServletRequest request, Model model) {
        String status = (String) request.getParameter("status");
        System.out.println("update status: " + status);
        if (!status.trim().isEmpty()) {
            HttpSession session = request.getSession();
            Person user = (Person) session.getAttribute("user");
            user.setStatus(status);
            session.setAttribute("user", user);
            personService.updatePersons(user);
            System.out.println("Nieuwe status: " + personService.getPerson(user.getUserId()).getStatus());
        } else model.addAttribute("errors", "Please choose a valid status.");

    }

    @PostMapping
    @RequestMapping("/addFriend")
    public void addFriend(Model model, HttpServletRequest request) {
        String email = (String) request.getParameter("email");
        System.out.println("add friend: " + email);
        email = email.toLowerCase() + "@ucll.be";
        HttpSession session = request.getSession();
        Person user = (Person) session.getAttribute("user");
        user = personService.addFriend(user, email);
        session.setAttribute("user", user);
        personService.updatePersons(user);
    }

    @GetMapping
    @RequestMapping("/getMessages")
    public void getMessages(HttpServletRequest request, HttpServletResponse response) {
        String ontvangerId = request.getParameter("ontvanger").toLowerCase();
        HttpSession session = request.getSession();
        Person user = (Person) session.getAttribute("user");
        try {
            String ChatberichtenJSON = this.toJSONBerichten(chatService.getChatBerichten(user, personService.getPerson(ontvangerId)));
            response.setContentType("application/json");
            response.getWriter().write(ChatberichtenJSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toJSONBerichten(List<Chatbericht> chatBerichten) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(chatBerichten);
    }

    @PostMapping
    @RequestMapping("/sendMessage")
    public void sendChatBericht(HttpServletRequest request, HttpServletResponse response) {
        String bericht = request.getParameter("bericht");
        HttpSession session = request.getSession();
        Person zender = (Person) session.getAttribute("user");
        Person ontvanger = personService.getPerson(request.getParameter("ontvanger").toLowerCase());
        chatService.addBericht(zender, bericht, ontvanger);
    }

    @GetMapping
    @RequestMapping("/getStatus")
    public void getStatus(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter("userId");
        Person p = personService.getPerson(userId.toLowerCase());
        String status = p.getStatus();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String JSON = mapper.writeValueAsString(status);
            response.setContentType("application/json");
            response.getWriter().write(JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping
    @RequestMapping("/getUsers")
    public void getUsers(HttpServletResponse response) {
        List<Person> persons = personService.getPersons();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String JSON = toJSON(persons);
            response.setContentType("application/json");

            // allow Angular
            response.setHeader("Access-Control-Allow-Origin","*");

            response.getWriter().write(JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    @RequestMapping(value = "/updateUser")
    @CrossOrigin(origins = "http://localhost:4200")
    public void update(HttpServletRequest request, HttpServletResponse response, HttpEntity<String> httpEntity) {
        String id;
        String lnaam;
        String vnaam;
        Person p = new Person();
        try {
            JsonNode body = new ObjectMapper().readTree(httpEntity.getBody());
            id = body.get("userId").asText();
            vnaam = body.get("firstName").asText();
            lnaam = body.get("lastName").asText();
            System.out.println(id);
            System.out.println(vnaam);
            System.out.println(lnaam);
            p.setUserId(id);
            p.setLastName(lnaam);
            p.setFirstName(vnaam);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        Person repo = this.personService.getPerson(p.getUserId());
        String status = repo.getStatus();
        p.setStatus(status);
        String pass = repo.getPassword();
        p.setPassword(pass);
        String salt = repo.getSalt();
        p.setSalt(salt);
        Role role = repo.getRole();
        p.setRole(role);
        List<Person> friends = repo.getFriends();
        p.setFriends(friends);
        this.personService.updatePersons(p);
    }

}
