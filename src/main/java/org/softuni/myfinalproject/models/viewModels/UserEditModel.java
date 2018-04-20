package org.softuni.myfinalproject.models.viewModels;

import java.util.ArrayList;
import java.util.List;

public class UserEditModel extends  UserLoginModel {

    private List<Long> roles;

    public UserEditModel() {
        this.roles = new ArrayList<>();
    }

    public List<Long> getRoles() {
        return this.roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }
}
