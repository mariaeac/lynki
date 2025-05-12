package com.lynki.lynki.exceptions;

public class UserException {

    public static class UsernameAlreadyExists extends RuntimeException {
        public UsernameAlreadyExists(String username) {
            super("Usuário " + username + " já cadastrado");
        }
    }

    public static class UsernameNotFound extends RuntimeException {
        public UsernameNotFound(String username) {
            super("Usuário " + username + " não encontrado");
        }
    }

    public static class UserNotFound extends RuntimeException {
        public UserNotFound() {
            super("Usuário não encontrado!");
        }
    }


}
