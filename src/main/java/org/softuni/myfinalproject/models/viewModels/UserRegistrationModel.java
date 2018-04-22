package org.softuni.myfinalproject.models.viewModels;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UserRegistrationModel {

    @Length(min = 4, message = "Too short username")
    @NotNull
    private String username;

    @Length(min = 3, message = "Password too short")
    @NotNull
    private String password;

    @Length(min = 3, message = "Confirm password too short")
    @NotNull
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
