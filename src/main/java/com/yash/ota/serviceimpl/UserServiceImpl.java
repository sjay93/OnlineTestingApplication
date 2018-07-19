package com.yash.ota.serviceimpl;

import com.yash.ota.dao.UserDAO;
import com.yash.ota.daoimpl.BatchDAOImpl;
import com.yash.ota.exception.*;
import com.yash.ota.model.User;
import com.yash.ota.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private static BatchServiceImpl batchService = new BatchServiceImpl(new BatchDAOImpl());

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean registerUser(User user) throws EmptyUserDetailsException, DuplicateUserException, EmptyBatchDetailsException {
        if (!checkForEmptyFields(user))
            return false;

        if (!checkForAlreadyRegisteredUser(user))
            return false;

        if (batchService.checkForBatchExistence(user.getBatchId()))
            throw new EmptyBatchDetailsException("Batch Details doesn't exists for register User");

        userDAO.insert(user);
        return true;
    }

    @Override
    public User authenticateUser(String loginName, String password) throws UserAuthenticationException {
        if (checkForEmptyUserList()) {
            return null;
        }

        for (User user1 : userDAO.list()) {
            if (user1.getLoginName().equals(loginName) && !user1.getPassword().equals(password))
                throw new UserAuthenticationException("User Authentication Failed");
            else if (user1.getLoginName().equals(loginName) && user1.getPassword().equals(password))
                return user1;
        }
        return null;
    }

    @Override
    public List<User> listUsers() {
        return userDAO.list();
    }

    @Override
    public boolean removeUser(User user) throws UserAuthenticationException, EmptyUserDetailsException {
        if (checkForEmptyUserList())
            return false;

        if (checkForUserExistence(user))
            throw new EmptyUserDetailsException("User to be deleted does not exist");

        return userDAO.delete(user.getId());
    }

    @Override
    public boolean updateUser(User user) throws UpdateUserException {
        if (checkForUserExistence(user))
            throw new UpdateUserException("User to be updated doesn't exists");

        return userDAO.update(user);
    }

    @Override
    public User findUserByProperty(String property, Object value) throws EmptyUserDetailsException {
        return userDAO.findByProperty(property, value);
    }

    private boolean checkForAlreadyRegisteredUser(User user) throws DuplicateUserException {
        for (User user1 : userDAO.list()) {
            if (user1.getLoginName().equals(user.getLoginName()))
                throw new DuplicateUserException("User already Registered");
        }
        return true;
    }

    private boolean checkForEmptyFields(User user) throws EmptyUserDetailsException {
        if (user.getFirstName() == null || user.getFirstName().isEmpty())
            throw new EmptyUserDetailsException("Invalid First Name");

        if (user.getLastName() == null || user.getLastName().isEmpty())
            throw new EmptyUserDetailsException("Invalid Last Name");

        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new EmptyUserDetailsException("Invalid Email Address");

        if (user.getContact() == 0)
            throw new EmptyUserDetailsException("Invalid Contact");

        if (user.getLoginName() == null || user.getLoginName().isEmpty())
            throw new EmptyUserDetailsException("Invalid Login Name");

        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new EmptyUserDetailsException("Invalid Password");

        return true;
    }

    private boolean checkForEmptyUserList() throws UserAuthenticationException {
        if (userDAO.list().isEmpty())
            throw new UserAuthenticationException("First Register User");

        return false;
    }

    private boolean checkForUserExistence(User user) {
        boolean found = false;
        for (User user1 : listUsers()) {
            if (user1.getId() == user.getId()) {
                found = true;
                break;
            }
        }
        return !found;
    }

}
