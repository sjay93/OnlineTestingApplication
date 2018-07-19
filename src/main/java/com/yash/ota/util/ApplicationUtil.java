package com.yash.ota.util;

import com.yash.ota.daoimpl.BatchDAOImpl;
import com.yash.ota.daoimpl.UserDAOImpl;
import com.yash.ota.exception.*;
import com.yash.ota.model.Batch;
import com.yash.ota.model.User;
import com.yash.ota.service.BatchService;
import com.yash.ota.service.UserService;
import com.yash.ota.serviceimpl.BatchServiceImpl;
import com.yash.ota.serviceimpl.UserServiceImpl;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Scanner;

public class ApplicationUtil {

    private static Scanner scanner = new Scanner(System.in);
    private static BatchService batchService;
    private static UserService userService;

    public static void launchApplication() throws IOException, EmptyBatchDetailsException, DuplicateBatchException, UpdateBatchException, UserAuthenticationException, EmptyUserDetailsException, UpdateUserException, DuplicateUserException {
        File file = new File("OTAMenu.txt");
        populateMenuFile(file);
        do {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while (!(str = br.readLine()).contains("Batch Menu")) {
                System.out.println(str);
            }
            System.out.println("Enter your choice");
            int choiceOfMenu = scanner.nextInt();
            br = new BufferedReader(new FileReader(file));
            switch (choiceOfMenu) {
                case 1:
                    printBatchSubMenu(br);
                    break;
                case 2:
                    printUserSubMenu(br);
                    break;
                case 3:
                    exit();
            }
            pressAnyKeyToContinue();
        } while (true);
    }

    private static void populateMenuFile(File file) throws IOException {
        try {
            if (!file.createNewFile()) {
                throw new FileAlreadyExistsException("File already exists! Kindly delete it");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println("----Main Menu----\n" +
                "1.Batch Management\n" +
                "2.User Management\n" +
                "3.Exit\n" +
                "\n" +
                "----Batch Menu-----\n" +
                "1. Register Batch\n" +
                "2. Update Batch\n" +
                "3. Delete Batch\n" +
                "4. List Batch\n" +
                "0. Go Back To Main Menu\n" +
                "\n" +
                "-----User Menu-----\n" +
                "\n" +
                "1. Register User\n" +
                "2. Authenticate User\n" +
                "3. List User\n" +
                "4. Remove User\n" +
                "5. Update User\n" +
                "6. Find User By Property\n" +
                "0. Go Back To Main Menu\n");
        printWriter.close();
    }

    private static void pressAnyKeyToContinue() {
        System.out.println("Press Any key to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBatchSubMenu(BufferedReader br) throws IOException, DuplicateBatchException, EmptyBatchDetailsException, UpdateBatchException {
        boolean print = false;
        String str;
        while ((str = br.readLine()) != null) {
            if (str.contains("Batch Menu") || print) {
                if (str.contains("User Menu"))
                    break;
                System.out.println(str);
                print = true;
            }
        }
        batchService = new BatchServiceImpl(new BatchDAOImpl());
        System.out.println("Enter the choice");
        int choice = scanner.nextInt();
        Batch batch = new Batch();

        switch (choice) {
            case 1:
                System.out.println("Enter the Batch Title");
                batch.setId(getBatchIDAvailable());
                batch.setTitle(scanner.next());
                if (batchService.addBatch(batch)) {
                    System.out.println("Successfully added Batch");
                }
                break;
            case 2:
                System.out.println("Enter the Batch Id for which Title is to be updated");
                batch.setId(scanner.nextInt());
                System.out.println("Enter the New Batch Title");
                batch.setTitle(scanner.next());
                if (batchService.updateBatch(batch)) {
                    System.out.println("Batch Details successfully update");
                }
                break;
            case 3:
                System.out.println("Enter the Batch Id for the Batch to be deleted");
                int batchId = scanner.nextInt();
                if (batchService.delete(batchId)) {
                    System.out.println("Batch successfully deleted");
                }
                break;
            case 4:
                for (Batch batch1 : batchService.listBatch()) {
                    System.out.println("Batch ID: " + batch1.getId() + ", " + "Batch Title: " + batch1.getTitle());
                }
                break;
            case 0:
                break;
        }
    }

    private static void printUserSubMenu(BufferedReader br) throws IOException, UserAuthenticationException, EmptyUserDetailsException, UpdateUserException, EmptyBatchDetailsException, DuplicateUserException {
        boolean print = false;
        String str;
        while ((str = br.readLine()) != null) {
            if (str.contains("User Menu") || print) {
                System.out.println(str);
                print = true;
            }
        }
        userService = new UserServiceImpl(new UserDAOImpl());
        System.out.println("Enter the choice");
        int choice = scanner.nextInt();
        User user = new User();
        switch (choice) {
            case 1:
                user.setId(getUserIDAvailable());
                getUserDetailsInput(user);
                if (userService.registerUser(user)) {
                    System.out.println("User Registration Successful!");
                }
                break;
            case 2:
                System.out.println("Enter Login Name and Password for Authentication");
                String userName = scanner.next();
                String password = scanner.next();
                if (userService.authenticateUser(userName, password) != null) {
                    System.out.println("User successfully authenticated");
                }
                break;
            case 3:
                for (User user1 : userService.listUsers()) {
                    System.out.println(user1);
                }
                break;
            case 4:
                System.out.println("Enter the User Id for the Batch to be deleted");
                int userId = scanner.nextInt();
                if (userService.removeUser(userService.findUserByProperty("Id", userId))) {
                    System.out.println("User successfully removed");
                }
                break;
            case 5:
                System.out.println("Enter the User Id for which User Details are to be updated");
                user.setId(scanner.nextInt());
                getUserDetailsInput(user);
                if (userService.updateUser(user))
                    System.out.println("User details updated successfully!");
                break;
            case 6:
                System.out.println("Enter the property for searching the user");
                String property = scanner.next();
                System.out.println("Enter the value for matching the user");
                String value = scanner.next();
                User userFound = userService.findUserByProperty(property, value);
                if (userFound != null)
                    System.out.println(userFound);
                break;
            case 0:
                break;
        }
    }

    private static int getBatchIDAvailable() throws FileNotFoundException {
        List<Batch> batchList = batchService.listBatch();

        if (batchList.isEmpty())
            return 1;
        else
            return batchList.get(batchList.size() - 1).getId() + 1;
    }

    private static int getUserIDAvailable() {
        List<User> userList = userService.listUsers();

        if (userList.isEmpty())
            return 1;
        else
            return userList.get(userList.size() - 1).getId() + 1;
    }

    private static void getUserDetailsInput(User user) {
        System.out.println("Enter the Batch Id");
        user.setBatchId(scanner.nextInt());
        System.out.println("Enter the First Name");
        user.setFirstName(scanner.next());
        System.out.println("Enter the Last Name");
        user.setLastName(scanner.next());
        System.out.println("Enter the Email");
        user.setEmail(scanner.next());
        System.out.println("Enter the contact");
        user.setContact(scanner.nextLong());
        System.out.println("Enter the Login Name");
        user.setLoginName(scanner.next());
        System.out.println("Enter the Password");
        user.setPassword(scanner.next());
    }

    private static void exit() {
        System.exit(0);
    }
}
