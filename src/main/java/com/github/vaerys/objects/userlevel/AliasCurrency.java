package com.github.vaerys.objects.userlevel;


import com.sun.istack.internal.Nullable;

public class AliasCurrency {
    public String id;
    public String name;
    @Nullable
    public String symbol;

    public double factor;  // alias = factor * parent

    AliasCurrency(String id, String name, String symbol, double factor){
        this.id = id;
        this.name = name==null || name.isEmpty() ? id.substring(0, 1).toUpperCase() + id.substring(1) : name;
        this.symbol = symbol;
        this.factor = factor;
    }
}