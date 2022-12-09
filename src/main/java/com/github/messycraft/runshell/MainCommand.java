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
                    if (main.getConfig().getBoolean("limitCommandExecution")) {
                        if (!main.getConfig().getStringList("commandWhitelist").contains(cmd)) {
                            sender.sendMessage("§c此命令不在命令白名单内。");
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
                                + sender.getName() + " 执行 Shell 命令: \""
                                + cmd + "\".\n";
                        byte[] bytes = log.getBytes();
                        fos.write(bytes, 0, bytes.length);
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (main.getConfig().getBoolean("useCMDExecute")) {
                        cmd = "cmd /c " + cmd;
                    }
                    try {
                        Process process = Runtime.getRuntime().exec(cmd);
                        sender.sendMessage("§e已请求执行命令 \"§6" + cmd + "§e\"。 等待执行结果...");
                        process.waitFor();
                        BufferedReader result = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String ans;
                        sender.sendMessage("§e返回内容: ");
                        while ((ans = result.readLine()) != null) {
                            sender.sendMessage("§6  " + ans);
                        }
                        sender.sendMessage("§e执行完成。");
                        result.close();
                        process.destroy(); 
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        sender.sendMessage("§c执行出错。");
                    }
                }
            }.runTaskAsynchronously(main);
            return true;
        }
        return false;
    }
}
