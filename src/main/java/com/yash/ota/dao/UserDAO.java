package com.yash.ota.dao;

import com.yash.ota.exception.EmptyUserDetailsException;
import com.yash.ota.model.User;

import java.io.IOException;
import java.util.List;

public interface UserDAO {

    boolean insert(User user);

    boolean delete(int id);

    boolean update(User user);

    List<User> list();

    User findById(int id) throws EmptyUserDetailsException;

    User findByProperty(String property, Object value) throws EmptyUserDetailsException;

}
