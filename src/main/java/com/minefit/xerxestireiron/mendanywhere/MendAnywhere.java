package com.minefit.xerxestireiron.mendanywhere;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MendAnywhere extends JavaPlugin implements Listener {
    private final Commands commands = new Commands(this);

    public void onEnable() {
        getCommand("mendthis").setExecutor(this.commands);
    }

    public void onDisable() {
    }
}
