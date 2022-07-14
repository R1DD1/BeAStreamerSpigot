package beastreamer.beastreamer.games;

import beastreamer.beastreamer.BeAStreamer;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class JustChattingClass implements Listener {
    private final BeAStreamer plugin;

    public JustChattingClass(BeAStreamer plugin) {
        this.plugin = plugin;
    }
    int timer = 100;
    int taskToCancel;


    public int randomInt() {
        int i = (int) (Math.random() * 5);
        return i;
    }

    ArrayList<Integer> listOfDoubles = new ArrayList<Integer>();

    int rand;

    boolean sended = false;


    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p =e.getPlayer();
        if (e.getPlayer().getWorld().equals(Bukkit.getWorld("justChattingWorld"))){
            p.sendMessage("");
            p.sendMessage(ChatColor.GREEN+"Успех! Стрим успешно начат!");
            p.sendMessage("");


            // mechanic of chatting
            rand = randomInt();
            listOfDoubles.add(rand);
            String chatting = plugin.getConfig().getString("justChatting.strs."+rand);
            p.sendMessage(chatting);
            sended = true;


            taskToCancel = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    ActionBarAPI.sendActionBar(p, ("Конец стрима через " +ChatColor.AQUA+timer));
                    timer = timer-1;
                    if (timer==0){
                        Bukkit.getScheduler().cancelTask(taskToCancel);
                        p.sendMessage("Начинаю телепортацию");
                    }

                }
            }, 20, 20);
        }
    }

    String answer;

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (sended){
            answer = plugin.getConfig().getString("justChatting.strs."+rand);

                if (listOfDoubles.size() >3){
                    p.sendMessage("ПОБЕДА");
                    e.setCancelled(true);
                    sended = false;
                }else {
                    if (e.getMessage().equalsIgnoreCase(answer)){
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.GREEN + "ПРАВИЛЬНО!");

                        rand = randomInt();
                        if (!(listOfDoubles.contains(rand))){
                            listOfDoubles.add(rand);
                            answer = plugin.getConfig().getString("justChatting.strs."+rand);
                            String question = plugin.getConfig().getString("justChatting.strs."+rand);
                            p.sendMessage("");
                            p.sendMessage(ChatColor.BOLD +question);
                        }else {
                            while (listOfDoubles.contains(rand)){
                                rand = randomInt();
                            }
                            listOfDoubles.add(rand);
                            answer = plugin.getConfig().getString("justChatting.strs."+rand);
                            String question = plugin.getConfig().getString("justChatting.strs."+rand);
                            p.sendMessage("");
                            p.sendMessage(ChatColor.BOLD +question);

                        }
                    }else {
                        e.getPlayer().sendMessage(ChatColor.RED + "Вы ошиблись");
                        e.setCancelled(true);
                    }
                }

        }
    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent e){
        Bukkit.getScheduler().cancelTask(taskToCancel);
    }
}
