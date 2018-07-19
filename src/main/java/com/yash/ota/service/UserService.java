package com.yash.ota.service;

import com.yash.ota.exception.*;
import com.yash.ota.model.User;

import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {
    boolean registerUser(User user) throws EmptyUserDetailsException, DuplicateUserException, EmptyBatchDetailsException, FileNotFoundException;

    User authenticateUser(String loginName, String password) throws UserAuthenticationException;

    List<User> listUsers();

    boolean removeUser(User user) throws UserAuthenticationException, EmptyUserDetailsException;

    boolean updateUser(User user) throws UpdateUserException;

    User findUserByProperty(String property, Object value) throws EmptyUserDetailsException;
}
