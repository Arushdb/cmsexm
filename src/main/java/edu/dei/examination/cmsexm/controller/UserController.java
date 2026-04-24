package edu.dei.examination.cmsexm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.payload.request.UserDTO;
import edu.dei.examination.cmsexm.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.createUser(dto));
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return service.getUser(id);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Integer id,
                       @RequestBody UserDTO dto) {
        return service.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteUser(id);
    }
    
    @GetMapping("/roles")
    public List<Role> getroles() {
    	return service.getroles();
    }
    
    @GetMapping("/search")
    public List<User> search(@RequestParam String q) {
        return service.searchUsers(q);
    }
    
    @GetMapping("/rolename/{rolename}")
    public List<User> getByroles(@PathVariable String rolename) {
        return service.getUserswithroles(rolename);
    }
    
    
    
}