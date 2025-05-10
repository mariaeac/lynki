package com.lynki.lynki.exceptions;

public class UserException {

    public static class UsernameAlreadyExists extends RuntimeException {
        public UsernameAlreadyExists(String username) {
            super("Username " + username + " already exists");
        }
    }

    public static class UsernameNotFound extends RuntimeException {
        public UsernameNotFound(String username) {
            super("Username " + username + " not found");
        }
    }


}
