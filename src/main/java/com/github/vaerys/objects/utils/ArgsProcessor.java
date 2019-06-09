package com.github.vaerys.objects.utils;

/**
 * Created by Lyrth on 09/06/2019.
 */
public class ArgsProcessor {

    String[] args;

    String firstWord = null;
    String rest = null;

    public ArgsProcessor(String from) {
        args = from.split("\\s+");
    }

    public int length(){
        return args.length;
    }

    public boolean isPresent(int index){
        return !get(index).isEmpty();
    }

    public String get(int index){  // negative index: starts from right
        if (index > args.length-1 || index < -args.length) return "";
        if (index < 0) {
            return args[args.length+index];
        } else {
            return args[index];
        }
    }

    public String getRange(int i, int j){  // inclusive, inclusive
        if (i < 0) i = args.length+i;
        if (j < 0) j = args.length+j;
        if (i > args.length-1 || j > args.length-1 || i < 0 || j < 0 || i > j){
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int k = i; k <= j; k++){
            out.append(args[k]).append(" ");
        }
        return out.toString().replaceAll("^\\s+|\\s+$","");
    }

    public String getAll() {
        return String.join(" ",args);
    }
}
