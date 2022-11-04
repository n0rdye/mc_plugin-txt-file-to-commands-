package me.n0rdye.clicker_pug;

import com.sun.source.util.Plugin;
import com.sun.tools.javac.util.ArrayUtils;
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.io.FileWriter;
import java.nio.charset.Charset;

import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class Clicker_pug extends JavaPlugin {
    public boolean rn =true;
    @Override
    public void onEnable() {
        try {
            System.out.println("clicker plugin started");
            cd();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(command.getName().equalsIgnoreCase("plug_start")){
            System.out.println("start");
            rn =true;
        }
        else if (command.getName().equalsIgnoreCase("plug_stop")){
            rn =false;
        }
        return true;
    }

    private BukkitTask countdown;

    public void cd() {
        if (countdown != null && countdown instanceof BukkitTask) {
            countdown.cancel();
        }
        countdown = Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                endcd();
            }
        }, 5 * 20);
    }
    private void endcd() {
        if(rn){
            String cmds = read("f.txt");
            String[] cmd = cmds.split("/");
            BukkitTask task;
            task = Bukkit.getServer().getScheduler().runTaskLater(this, () -> {
                for (int i=0;i< cmd.length-1;i++) {
//                    System.out.println(cmd[i]);
                    Player p = getServer().getPlayer(cmd[i].split(" ")[0]);
                    if(p!=null){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+cmd[i]);
                        String cmdr = String.join("/", arrRemove(cmd, i));
                        if(cmdr.replace(" / ","")==""){
                            clf(" ");
                        }
                        else{clf(cmdr.replace(" / ",""));}
                    }
                }
                return;
            }, 1 * 20);
        }
        cd();
    }

    public String[] arrRemove(String[] array, int index){
                for (int i= index;i< array.length-1;i++){
                    array[i]=array[i+1];
                }
                return array;
    }

    public void clf(String str){
        try {
            FileWriter fw = new FileWriter("f.txt",false);
            fw.write(str);
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String read(String path) {
        String str="";
        try{
            FileInputStream fis = new FileInputStream(path);
            Scanner sc = new Scanner(fis);
            str = sc.nextLine();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return str;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
