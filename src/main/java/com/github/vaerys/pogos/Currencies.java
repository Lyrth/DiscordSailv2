package com.github.vaerys.pogos;

import com.github.vaerys.handlers.GuildHandler;
import com.github.vaerys.handlers.PixelHandler;
import com.github.vaerys.main.Constants;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.masterobjects.GuildObject;
import com.github.vaerys.masterobjects.UserObject;
import com.github.vaerys.objects.userlevel.AliasCurrency;
import com.github.vaerys.objects.userlevel.CharacterObject;
import com.github.vaerys.objects.userlevel.CurrencyObject;
import com.github.vaerys.objects.userlevel.DungeonCharObject;
import com.github.vaerys.objects.utils.DualVar;
import com.github.vaerys.templates.GlobalFile;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Vaerys on 14/08/2016.
 */

public class Currencies extends GlobalFile {
    public static final String FILE_PATH = "Currencies.json";
    private double fileVersion = 1.0;
    private LinkedHashMap<String,CurrencyObject> currencies = new LinkedHashMap<>();

    public LinkedHashMap<String,CurrencyObject> getCurrencies() {
        return currencies;
    }

    public void addCurrency(CurrencyObject currency){
        currencies.put(currency.getId(),currency);
    }

    public boolean removeCurrency(CurrencyObject currency){  // true on success
        return currencies.remove(currency.getId()) != null;
    }

    public boolean removeCurrency(String currencyId){  // true on success
        return currencies.remove(currencyId) != null;
    }

    public CurrencyObject getCurrency(String currencyId){
        return currencies.get(currencyId);
    }

    public DualVar<String,String> findCurrencyType(String name){  // String : (base)id; String : aliasid or null
        // find as base id
        if (currencies.containsKey(name.toLowerCase()))
            return new DualVar<>(name.toLowerCase(),null);
        // find as symbol, base name, aliasid, alias symbol, alias name - in that order
        for (CurrencyObject cur : currencies.values()){
            if (cur.getSymbol().equalsIgnoreCase(name) || cur.getName().toLowerCase().contains(name.toLowerCase()))
                return new DualVar<>(cur.getId(),null);
            if (cur.getAliases().containsKey(name.toLowerCase()))
                return new DualVar<>(cur.getId(),name.toLowerCase());
            for (AliasCurrency alias : cur.getAliases().values()){
                if ((alias.symbol != null && alias.symbol.equalsIgnoreCase(name)) || alias.name.toLowerCase().contains(name.toLowerCase()))
                    return new DualVar<>(cur.getId(),alias.id);
            }
        }
        return null;
    }

}
