package org.example;

import org.example.entity.User;
import org.example.repository.UserDAO;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HibernateServiceTest {

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUser() {
        User mockUser = new User();
        mockUser.setId(100L);
        mockUser.setName("MockName");

        Mockito.when(userDao.findById(100L)).thenReturn(mockUser);

        String name = userService.getUSerById(100L).getName();
        Assertions.assertEquals("MockName", name);
    }

    @Test
    void testGetUserNotFound() {
        Mockito.when(userDao.findById(999L)).thenReturn(null);
        Assertions.assertThrows(RuntimeException.class, () -> userService.getUSerById(999L));
    }

    @Test
    void testCreateUser() {
        User mockUser = new User();
        mockUser.setName("MockName");
        userService.createUser(mockUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao).save(captor.capture());

        Assertions.assertEquals("MockName", captor.getValue().getName());
    }
}
