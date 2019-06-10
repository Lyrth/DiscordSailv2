package com.github.vaerys.objects.userlevel;

import com.github.vaerys.handlers.GuildHandler;
import com.github.vaerys.masterobjects.GuildObject;
import sx.blah.discord.handle.obj.IIDLinkedObject;
import sx.blah.discord.handle.obj.IRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vaerys on 17/11/2016.
 */


/* Notes:
 * Character names will now have to be unique and cannot contain spaces.
 * The base commands that are with v1 are the first ones being created
 * the rest will come later. (not top priority sadly :c)
 */

public class CharacterObject {
    String name; //Character's Name
    long userID; //author's authorSID
    String nickname; //character's name;
    ArrayList<Long> roleIDs = new ArrayList<>(); // these are the cosmetic and modifier roleIDs the author has;
    String gender = "N/a"; //limit = 20 chars.
    String age = "Unknown"; // limit = 20 chars.
    String shortBio = ""; //limit to 140 chars.
    String avatarURL = "";
    String longBioURL = ""; //URL link linking to Character Bios
    String weapon = "";
    HashMap<String,Long> currencies;
    private String weight = null;
    private String height = null;

    //dungeon stats
    CharStats stats = new CharStats(0,0,0,0,0,0, 10);

    public CharacterObject(String name, long userID, String nickname, List<Long> roleIDs) {
        this.name = name;
        this.userID = userID;
        this.nickname = nickname;
        this.roleIDs = (ArrayList<Long>) roleIDs;
        currencies = new HashMap<>();
    }

    public String getLongBioURL() {
        return longBioURL;
    }

    public void setLongBioURL(String longBioURL) {
        this.longBioURL = longBioURL;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public List<Long> getRoleIDs() {
        return roleIDs;
    }

    public void setRoleIDs(ArrayList<Long> roleIDs) {
        this.roleIDs = roleIDs;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public void update(String name, List<IRole> roles) {
        this.nickname = name;
        this.roleIDs = new ArrayList<>(roles.stream().map(IIDLinkedObject::getLongID).collect(Collectors.toList()));
    }

    public Color getColor(GuildObject guild) {
        return GuildHandler.getUsersColour(roleIDs.stream().map(aLong -> guild.getRoleByID(aLong)).filter(iRole -> iRole != null).collect(Collectors.toList()));
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public void modifyCredits(String currency, long amount){  // + / -
        if (currencies == null)
            currencies = new HashMap<>();
        if (currencies.containsKey(currency)){
            currencies.compute(currency,(cur,val) -> (val == null) ? amount : val+amount);
        } else {
            currencies.put(currency,amount);
        }
    }

    public void setCredits(String currency, long amount){
        if (currencies == null)
            currencies = new HashMap<>();
        if (amount == 0){
            currencies.remove(currency);
        } else {
            currencies.put(currency, amount);
        }
    }

    public long getCredits(String currency){
        if (currencies == null){
            currencies = new HashMap<>();
            return 0L;
        }
        return currencies.getOrDefault(currency,0L);
    }

    public String getCharCurrencies(GuildObject guild){
        if (currencies == null)
            currencies = new HashMap<>();
        StringBuilder str = new StringBuilder();
        currencies.forEach((id,val) -> {
            try {
                str.append("**" + guild.currencies.getCurrency(id).getName() + "**: ")
                        .append(guild.currencies.getCurrency(id).getRealValue(val))
                        .append("\n")
                        .append(guild.currencies.getCurrency(id).printAliases(guild.currencies.getCurrency(id).getRealValue(val),guild))
                        .append("\n\n");
            } catch(NullPointerException ignored){}
        });
        return str.toString();
    }

    public int getStat(String stat){
        if (stats == null) stats = new CharStats(0,0,0,0,0,0, 10);
        return stats.getStat(stat);
    }

    public String setStat(String stat, int value){
        if (stats == null) stats = new CharStats(0,0,0,0,0,0, 10);
        return stats.setStat(stat,value);
    }

    public String printStats(){
        if (stats == null) stats = new CharStats(0,0,0,0,0,0, 10);
        return stats.printStats();
    }

    public String printStatsShort(){
        if (stats == null) stats = new CharStats(0,0,0,0,0,0, 10);
        return stats.printStatsShort();
    }
}
