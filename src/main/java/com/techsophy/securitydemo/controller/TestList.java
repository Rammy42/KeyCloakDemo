package com.techsophy.securitydemo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TestList {
    public static void main(String[] args) throws Exception {
         ;
        try {
            List<String> lt = Collections.<String>emptyList();
            Optional<List<String>> ol = Optional.of(lt);
            System.out.println(ol.get().isEmpty());
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }
}
