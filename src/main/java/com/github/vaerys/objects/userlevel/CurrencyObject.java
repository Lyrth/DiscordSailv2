package com.github.vaerys.objects.userlevel;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyObject {

    private String id;
    private String name;
    private String symbol = "";
    private int decimalDigits = 0;

    // String is id
    private HashMap<String,AliasCurrency> aliases = new HashMap<>();

    public CurrencyObject(String id, String name, int decimalDigits) {
        this.id = id;
        this.name = name; // ==null ? id.substring(0, 1).toUpperCase() + id.substring(1) : name;
        this.decimalDigits = decimalDigits < 0 ? 0 : decimalDigits;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public AliasCurrency getAlias(String aliasId){
        return aliases.get(aliasId);
    }

    public void addAlias(String aliasId, double factor, @Nullable String aliasName, @Nullable String aliasSymbol){
        aliases.put(aliasId, new AliasCurrency(aliasId,aliasName,aliasSymbol,factor));
    }

    public boolean removeAlias(String id){
        return aliases.remove(id) != null;
    }

    public double getRealValue(long val){
        return ((double)val)/Math.pow(10,decimalDigits);
    }

    public long getLongValue(double val){
        return (long)(val*Math.pow(10,decimalDigits));
    }

    public double getAliasAsBase(double aliasval, String alias){
        return aliasval*aliases.get(alias).factor;
    }

    public double getBaseAsAlias(double baseval, String alias){
        return baseval/aliases.get(alias).factor;
    }

    public long getLongAliasValue(double aliasval, String alias){
        return getLongValue(getAliasAsBase(aliasval,alias));
    }

    public HashMap<String, AliasCurrency> getAliases() {
        return aliases;
    }

    public String printAliases(double baseVal){
        StringBuilder str = new StringBuilder();
        aliases.forEach((id,alias) -> {
            if (getBaseAsAlias(baseVal,id) > 0.0999d)
                str.append(" =   ").append(getBaseAsAlias(baseVal, id)).append(" ").append(alias.name).append("\n");
        });
        return str.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
