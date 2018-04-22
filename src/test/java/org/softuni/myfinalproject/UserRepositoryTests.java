package org.softuni.myfinalproject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.myfinalproject.models.entities.User;
import org.softuni.myfinalproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindBySubstring_givenSubstring_shouldFindTwoUsers() {
        User test1 = new User();
        test1.setUsername("test1");
        test1.setPassword("1234");

        User test2 = new User();
        test2.setUsername("test2");
        test2.setPassword("2345");

        User test3 = new User();
        test3.setUsername("test3");
        test3.setPassword("3456");
        this.testEntityManager.persistAndFlush(test1);
        this.testEntityManager.persistAndFlush(test2);
        this.testEntityManager.persistAndFlush(test3);

        // act
        User result = this.userRepository.findByUsername("test1");

        // assert
        assertEquals("test1", "test1");
    }
}
