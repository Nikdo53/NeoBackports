package net.nikdo53.neobackports.utils;

import java.util.ArrayList;
import java.util.List;

public class ListReverser {
   public static <T> List<T> reverse(List<T> original){
       ArrayList<T> list = new ArrayList<>();

       for (int i = 0; i < original.size(); i++) {
           list.add(original.get(original.size() - i - 1));
       }

       return list;
   }
}
