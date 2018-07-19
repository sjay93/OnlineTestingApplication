package com.yash.ota.daoimpl;

import com.yash.ota.dao.UserDAO;
import com.yash.ota.exception.EmptyUserDetailsException;
import com.yash.ota.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDAOImpl implements UserDAO {

    private static File userFile = new File("User.txt");
    private List<User> users;

    @Override
    public boolean insert(User user) {
        BufferedWriter bufferedWriter = null;
        try {
            if (!userFile.exists())
                userFile.createNewFile();

            bufferedWriter = new BufferedWriter(new FileWriter(userFile, true));
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            printWriter.println(user.getId() + "," + user.getBatchId() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + user.getContact() + "," + user.getLoginName() + "," + user.getPassword());
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean delete(int id) {
        users = list();
        for (User user : users) {
            if (user.getId() == id) {
                users.remove(user);
                break;
            }
        }
        writeToFile(users);
        return true;
    }

    @Override
    public boolean update(User user) {
        users = list();
        for (User user1 : users) {
            if (user1.getId() == user.getId()) {
                updateUserFields(user1, user);
                break;
            }
        }
        writeToFile(users);
        return true;
    }

    @Override
    public List<User> list() {
        userFile = new File("User.txt");
        users = new ArrayList<>();
        if (userFile.exists()) {
            try {
                Scanner scanner = new Scanner(userFile);
                while (scanner.hasNext()) {
                    User user = new User();
                    String input[] = scanner.nextLine().split(",");
                    user.setId(Integer.parseInt(input[0]));
                    user.setBatchId(Integer.parseInt(input[1]));
                    user.setFirstName(input[2]);
                    user.setLastName(input[3]);
                    user.setEmail(input[4]);
                    user.setContact(Long.parseLong(input[5]));
                    user.setLoginName(input[6]);
                    user.setPassword(input[7]);
                    users.add(user);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public User findById(int id) throws EmptyUserDetailsException {
        return findByProperty("id", id);
    }

    @Override
    public User findByProperty(String property, Object value) throws EmptyUserDetailsException {
        User userToBeSearched = null;
        for (User user : list()) {
            switch (property.toLowerCase()) {
                case "id":
                    if (user.getId() == Integer.parseInt((String) value))
                        userToBeSearched = user;
                    break;
                case "batchid":
                    if (user.getBatchId() == Integer.parseInt((String) value))
                        userToBeSearched = user;
                    break;
                case "firstname":
                    if (user.getFirstName().equals(value))
                        userToBeSearched = user;
                    break;
                case "lastname":
                    if (user.getLastName().equals(value))
                        userToBeSearched = user;
                    break;
                case "email":
                    if (user.getEmail().equals(value))
                        userToBeSearched = user;
                    break;
                case "loginname":
                    if (user.getLoginName().equals(value))
                        userToBeSearched = user;
                    break;
                case "password":
                    if (user.getPassword().equals(value))
                        userToBeSearched = user;
                    break;
                case "contact":
                    if (user.getContact() == (Long.parseLong((String) value)))
                        userToBeSearched = user;
                    break;
            }
            if (userToBeSearched != null)
                break;
        }

        if (userToBeSearched == null)
            throw new EmptyUserDetailsException("User you are searching doesn't exists");

        return userToBeSearched;
    }

    private void updateUserFields(User user1, User user) {
        if (!user.getFirstName().equals(user1.getFirstName())) {
            user1.setFirstName(user.getFirstName());
        }
        if (!user.getLastName().equals(user1.getLastName())) {
            user1.setLastName(user.getLastName());
        }
        if (!user.getEmail().equals(user1.getEmail())) {
            user1.setEmail(user.getEmail());
        }
        if (user.getContact() != user1.getContact()) {
            user1.setContact(user.getContact());
        }
        if (!user.getLoginName().equals(user1.getLoginName())) {
            user1.setLoginName(user.getLoginName());
        }
        if (!user.getPassword().equals(user1.getPassword())) {
            user1.setPassword(user.getPassword());
        }
    }

    private void writeToFile(List<User> users) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userFile));
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            for (User user : users)
                printWriter.println(user.getId() + "," + user.getBatchId() + "," + user.getFirstName() + "," + user.getLastName() + "," + user.getEmail() + "," + user.getContact() + "," + user.getLoginName() + "," + user.getPassword());
            printWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
