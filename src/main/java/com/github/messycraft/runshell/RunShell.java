package com.github.messycraft.runshell;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class RunShell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("RunShell 已加载。");
        saveDefaultConfig();
        reloadConfig();
        try {
            new File(getDataFolder(), "history.log").createNewFile();
        } catch (IOException ignored) {}
        getCommand("runshell").setExecutor(new MainCommand());
        getCommand("runshell-reload").setExecutor(new ReloadCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("RunShell 已卸载。");
    }
}
