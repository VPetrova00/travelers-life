package org.softuni.myfinalproject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.models.viewModels.UserRegistrationModel;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.softuni.myfinalproject.services.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    private static final String PASSWORD_HASH = "hash";

    @Mock
    private UserRegistrationModel userRegistrationModel;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Before
    public void setUp() {
        when(this.passwordEncoder.encode(anyString())).thenReturn(PASSWORD_HASH);
    }

    @Test
    public void testRegisterWithUsernamePasswordAndConfirmPassword_passwordShouldBeEncoded() {
        when(this.userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        final String USERNAME = "test";
        final String PASSWORD = "test123";
        final String CONFIRM_PASSWORD = "test123";

        this.userRegistrationModel.setUsername(USERNAME);
        this.userRegistrationModel.setPassword(PASSWORD);
        this.userRegistrationModel.setConfirmPassword(CONFIRM_PASSWORD);

        this.userService.register(this.userRegistrationModel);

        assertEquals("Password was not or wrong encoded", PASSWORD_HASH);
    }
}
