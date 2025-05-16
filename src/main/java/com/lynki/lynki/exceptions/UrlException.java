package com.lynki.lynki.exceptions;

public class UrlException {

    public static class UrlNotFoundException extends RuntimeException {
        public UrlNotFoundException(){
            super("Nenhuma URL encontrada");
        }
    }

}
