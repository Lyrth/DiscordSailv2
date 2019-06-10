package com.github.vaerys.guildtoggles.toggles;

import com.github.vaerys.commands.characters.EditDungeonChar;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.pogos.GuildConfig;
import com.github.vaerys.templates.GuildSetting;

public class CurrencyRemainder extends GuildSetting {

    @Override
    public SAILType name() {
        return SAILType.CURRENCY_REMAINDER;
    }

    @Override
    public boolean toggle(GuildConfig config) {
        return config.currencyRemainder = !config.currencyRemainder;
    }

    @Override
    public boolean enabled(GuildConfig config) {
        return config.currencyRemainder;
    }

    @Override
    public boolean getDefault() {
        return new GuildConfig().currencyRemainder;
    }

    @Override
    public String desc(CommandObject command) {
        return "Enables remainder display for currencies.";
    }

    @Override
    public String shortDesc(CommandObject command) {
        return "enables remainder display mode for currencies.-";
    }

    @Override
    public void setup() {

    }
}