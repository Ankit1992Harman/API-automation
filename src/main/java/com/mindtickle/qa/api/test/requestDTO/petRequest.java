package com.mindtickle.qa.api.test.requestDTO;

import lombok.Data;

@Data

public class petRequest {

    public petRequest() {
        super();
    }
    public int id;
    public String username;

    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String phone;
    public int userStatus;



}
