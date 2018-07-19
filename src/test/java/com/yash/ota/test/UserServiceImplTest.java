package com.yash.ota.test;

import com.yash.ota.dao.UserDAO;
import com.yash.ota.exception.*;
import com.yash.ota.model.User;
import com.yash.ota.service.UserService;
import com.yash.ota.serviceimpl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImplTest {

    private User user;
    private UserService userService;
    private UserDAO userDAO;
    private List<User> userList;

    @Before
    public void userSetup() {
        userDAO = Mockito.mock(UserDAO.class);
        userService = new UserServiceImpl(userDAO);
        user = new User();
        user.setId(1);
        user.setBatchId(1);
        user.setFirstName("Jay");
        user.setLastName("Shah");
        user.setEmail("jay.shah@yash.com");
        user.setContact(12345);
        user.setLoginName("s.jay93");
        user.setPassword("Jvsshah@12");
        userList = new ArrayList<>();
        userList.add(user);
    }

    @Test(expected = EmptyUserDetailsException.class)
    public void testRegisterUser_GivenAnyParticularFieldAsNullOrEmpty_ShouldThrowEmptyParameterException() throws EmptyUserDetailsException, DuplicateUserException, EmptyBatchDetailsException, FileNotFoundException {
        user.setLastName("");
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Assert.assertFalse(userService.registerUser(user));
    }

    @Test(expected = DuplicateUserException.class)
    public void testRegisterUser_GivenDuplicateLoginName_ShouldThrowDuplicateUserException() throws EmptyUserDetailsException, DuplicateUserException, EmptyBatchDetailsException, FileNotFoundException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        Assert.assertFalse(userService.registerUser(user));
    }

    @Test
    public void testRegisterUser_GivenAllDetails_ShouldRegisterUser() throws DuplicateUserException, EmptyUserDetailsException, EmptyBatchDetailsException, FileNotFoundException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Assert.assertTrue(userService.registerUser(user));
    }

    @Test(expected = UserAuthenticationException.class)
    public void testAuthenticateUser_GivenIncorrectDetails_ShouldThrowAuthenticationException() throws UserAuthenticationException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        Assert.assertEquals(userService.authenticateUser("s.jay93", "Jvsshah@1"), user);
    }

    @Test
    public void testAuthenticateUser_GivenCorrectDetails_ShouldReturnUserObject() throws UserAuthenticationException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        Assert.assertEquals(userService.authenticateUser("s.jay93", "Jvsshah@12"), user);
    }

    @Test(expected = EmptyUserDetailsException.class)
    public void testRemoveUser_GivenNonExistentUserDetails_ShouldThrowRemoveException() throws UserAuthenticationException, EmptyUserDetailsException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        User user2 = new User();
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setId(user.getId());
        user2.setBatchId(user.getBatchId());
        user2.setLoginName(user.getLoginName());
        user2.setEmail(user.getEmail());
        user2.setContact(user.getContact());
        user2.setPassword("Jvsshah@1");
        Assert.assertFalse(userService.removeUser(user2));
    }

    @Test(expected = UpdateUserException.class)
    public void testUpdateUser_GivenIncorrectUserId_ShouldThrowUpdateUserException() throws UpdateUserException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        User user2 = new User();
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setId(5);
        user2.setBatchId(user.getBatchId());
        user2.setLoginName(user.getLoginName());
        user2.setEmail(user.getEmail());
        user2.setContact(user.getContact());
        user2.setPassword(user.getPassword());
        Assert.assertFalse(userService.updateUser(user2));
    }

    @Test
    public void testUpdateUser_GivenCorrectUserId_ShouldUpdateUserDetails() throws UpdateUserException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        User user2 = new User();
        user2.setFirstName("Jay Viren");
        user2.setLastName(user.getLastName());
        user2.setId(1);
        user2.setBatchId(user.getBatchId());
        user2.setLoginName(user.getLoginName());
        user2.setEmail(user.getEmail());
        user2.setContact(user.getContact());
        user2.setPassword(user.getPassword());
        Assert.assertTrue(userService.updateUser(user2));
    }

    @Test(expected = EmptyUserDetailsException.class)
    public void testFindUserByProperty_GivenUserThatDontExists_ShouldThrowEmptyUserDetailsException() throws EmptyUserDetailsException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        Assert.assertEquals(userService.findUserByProperty("Id", 20), user);
    }

    @Test
    public void testFindUserByProperty_GivenUserThatExists_ShouldReturnThatParticularUser() throws EmptyUserDetailsException {
        Mockito.when(userDAO.insert(user)).thenReturn(true);
        Mockito.when(userDAO.list()).thenReturn(userList);
        Assert.assertEquals(userService.findUserByProperty("Email", "jay.shah@yash.com"), user);
    }
}
