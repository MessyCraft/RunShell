package com.github.messycraft.runshell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.time.LocalDateTime;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            JavaPlugin main = JavaPlugin.getProvidingPlugin(RunShell.class);
            new BukkitRunnable() {
                @Override
                public void run() {
                    String cmd = "";
                    for (String arg : args) {
                        cmd = cmd + arg + " ";
                    }
                    cmd = cmd.substring(0, cmd.length()-1);
                    if (main.getConfig().getBoolean("limit-command")) {
                        if (!main.getConfig().getStringList("command-whitelist").contains(cmd)) {
                            sender.sendMessage("§cThis command is not in whitelist.");
                            return;
                        }
                    }
                    try {
                        File file = new File(main.getDataFolder(), "history.log");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file, true);
                        String log;
                        LocalDateTime now = LocalDateTime.now();
                        log = "[" + now.getYear() + "/"
                                + now.getMonthValue() + "/"
                                + now.getDayOfMonth() + "-"
                                + now.getHour() + ":"
                                + now.getMinute() + ":"
                                + now.getSecond() + "] "
                                + sender.getName() + " execute shell-command: \""
                                + cmd + "\".\n";
                        byte[] bytes = log.getBytes();
                        fos.write(bytes, 0, bytes.length);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (main.getConfig().getBoolean("auto-add-prefix")) {
                        cmd = "cmd /c " + cmd;
                    }
                    try {
                        Process process = Runtime.getRuntime().exec(cmd);
                        sender.sendMessage("§aExecuted \"§7" + cmd + "§a\". Waiting...");
                        process.waitFor();
                        BufferedReader result = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String ans;
                        sender.sendMessage("§3§lReturn:");
                        while ((ans = result.readLine()) != null) {
                            sender.sendMessage("§b  " + ans);
                        }
                        sender.sendMessage("§aDone.");
                        result.close();
                        process.destroy();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        sender.sendMessage("§cError.");
                    }
                }
            }.runTaskAsynchronously(main);
            return true;
        }
        return false;
    }
}
