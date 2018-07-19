package com.yash.ota.main;

import com.yash.ota.exception.*;
import com.yash.ota.util.ApplicationUtil;

import java.io.IOException;

public class OTAStartupApplication {

    public static void main(String[] args) throws IOException, EmptyBatchDetailsException, DuplicateBatchException, UpdateBatchException, UserAuthenticationException, EmptyUserDetailsException, UpdateUserException, DuplicateUserException {
        ApplicationUtil.launchApplication();
    }
}
