package com.techsophy.securitydemo.poc;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListLenghts {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        List<Integer> list = numbers.stream().map(s -> s*2).collect(toList());
        System.out.println("Double values of ArrayList Integers: "+list);
    }
}
