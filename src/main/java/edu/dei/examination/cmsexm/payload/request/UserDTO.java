package edu.dei.examination.cmsexm.payload.request;

import java.util.List;

public class UserDTO {

    private Integer id;              // for update

    private String username;
    private String password;

    private String name;
    private String email;
    private String phone;

    private List<Integer> roleIds;   // multi-role support

    // ✅ Scholar-specific
    private Integer enrollmentNo;

    // optional status (ACTIVE / INACTIVE)
    private String status;

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(Integer enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
