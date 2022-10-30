package com.github.messycraft.runshell;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class RunShell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().warning("------------------------------");
        getLogger().warning("此插件设计于解决无法登录服务器后台等情况,");
        getLogger().warning("方便特定权限者执行windows/linux命令,");
        getLogger().warning("但也可以做到一些较极端的操作,");
        getLogger().warning("如关机,删除文件,更改后台密码,甚至更改注册表");
        getLogger().warning("等一切主机命令能做到的事情.");
        getLogger().warning("对权限管理不当造成滥用此插件的一切损失由使用者[自行承担]!");
        getLogger().warning("你可以在配置文件里规定本插件只能使用的shell命令,");
        getLogger().warning("防止主机被破坏.");
        getLogger().warning("同时通过本插件使用的任何命令都会被记录到log,");
        getLogger().warning("使用命令需特别给予权限 runshell.admin");
        getLogger().warning("例: 重启主机远程桌面服务:");
        getLogger().warning("   /runshell net stop TermService");
        getLogger().warning("   /runshell net start TermService");
        getLogger().warning("------------------------------");
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
    }
}
