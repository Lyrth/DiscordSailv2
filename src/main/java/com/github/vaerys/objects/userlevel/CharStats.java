package com.github.vaerys.objects.userlevel;

public class CharStats {

    private int hp, hpTotal;
    private int strength, dexterity, constitution, intelligence, wisdom, charisma;

    CharStats(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma, int totalHp){
        this.strength = strength < 0 ? 0 : strength;
        this.dexterity = dexterity < 0 ? 0 : dexterity;
        this.constitution = constitution < 0 ? 0 : constitution;
        this.intelligence = intelligence < 0 ? 0 : intelligence;
        this.wisdom = wisdom < 0 ? 0 : wisdom;
        this.charisma = charisma < 0 ? 0 : charisma;
        hp = this.hpTotal = totalHp;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setStrength(int strength) {
        this.strength = strength < 0 ? 0 : strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity < 0 ? 0 : dexterity;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution < 0 ? 0 : constitution;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence < 0 ? 0 : intelligence;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom < 0 ? 0 : wisdom;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma < 0 ? 0 : charisma;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setHpTotal(int hpTotal) {
        this.hpTotal = hpTotal;
    }

    public int getHp() {
        return hp;
    }

    public int getHpTotal() {
        return hpTotal;
    }

    public String getHpString(){
        return String.format("%d/%d",hp,hpTotal);
    }

    public int getStat(String stat) throws IllegalArgumentException {
        if ("strength".contains(stat.toLowerCase().trim())) return getStrength();
        if ("dexterity".contains(stat.toLowerCase().trim())) return getDexterity();
        if ("constitution".contains(stat.toLowerCase().trim())) return getConstitution();
        if ("intelligence".contains(stat.toLowerCase().trim())) return getIntelligence();
        if ("wisdom".contains(stat.toLowerCase().trim())) return getWisdom();
        if ("charisma".contains(stat.toLowerCase().trim())) return getCharisma();
        if ("hp".equals(stat.toLowerCase().trim())) return getHp();
        if ("hptotal".contains(stat.toLowerCase().trim())) return getHpTotal();
        throw new IllegalArgumentException("Stat invalid.");
    }

    public String setStat(String stat, int val) throws IllegalArgumentException {
        if ("strength".contains(stat.toLowerCase().trim())) {
            setStrength(val);
            return "Strength";
        }
        if ("dexterity".contains(stat.toLowerCase().trim())) {
            setDexterity(val);
            return "Dexterity";
        }
        if ("constitution".contains(stat.toLowerCase().trim())) {
            setConstitution(val);
            return "Constitution";
        }
        if ("intelligence".contains(stat.toLowerCase().trim())) {
            setIntelligence(val);
            return "Intelligence";
        }
        if ("wisdom".contains(stat.toLowerCase().trim())) {
            setWisdom(val);
            return "Wisdom";
        }
        if ("charisma".contains(stat.toLowerCase().trim())) {
            setCharisma(val);
            return "Charisma";
        }
        if ("hp".equals(stat.toLowerCase().trim())) {
            setHp(val);
            return "HP";
        }
        if ("hptotal".contains(stat.toLowerCase().trim())) {
            setHpTotal(val);
            return "HP Total";
        }
        throw new IllegalArgumentException("Stat invalid.");
    }

    public String printStatsShort(){
        return String.format(
                        "**STR:** %d\n" +
                        "**DEX:** %d\n" +
                        "**CON:** %d\n" +
                        "**INT:** %d\n" +
                        "**WIS:** %d\n" +
                        "**CHA:** %d\n",
                strength,dexterity,constitution,intelligence,wisdom,charisma);
    }

    public String printStats(){
        return String.format(
                        "**Strength:** %d\n" +
                        "**Dexterity:** %d\n" +
                        "**Constitution:** %d\n" +
                        "**Intelligence:** %d\n" +
                        "**Wisdom:** %d\n" +
                        "**Charisma:** %d\n",
                strength,dexterity,constitution,intelligence,wisdom,charisma);
    }
}
