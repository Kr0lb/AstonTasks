package org.example;


public class HashMapMain {
    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 1; i <= 15; i++) {
            map.put("key" + i, "value" + i);
        }

        map.remove("key16");
        map.remove("key13");


        for (int i = 1; i <= 15; i++) {
            System.out.println("key" + i + "-" + map.get("key" + i));
        }
    }
}