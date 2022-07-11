package beastreamer.beastreamer.jobs;

import beastreamer.beastreamer.BeAStreamer;
import beastreamer.beastreamer.database.Database;
import beastreamer.beastreamer.rooms.RoomClass;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Education implements Listener {
    private BeAStreamer plugin;

    public Education(BeAStreamer plugin) {
        this.plugin = plugin;
    }

    private  final Database database = new Database();
    private final RoomClass roomClass = new RoomClass(plugin);

    NPCRegistry registry  = CitizensAPI.getNPCRegistry();
    NPC npc = registry.createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" +ChatColor.BOLD+ "Учитель");

    Inventory educationInv = Bukkit.createInventory(null, 27, "Образование");




    public int randomInt() {
        int i = (int) (Math.random() * 10);
        return i;
    }



    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        if (p.getWorld().equals(Bukkit.getWorld("educationWorld"))){
            npc.spawn(p.getLocation());

        }
    }

    @EventHandler
    public void  interactNPC(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if(e.getRightClicked().getCustomName().equals(ChatColor.YELLOW + "" +ChatColor.BOLD+ "Учитель")) {
            double educationLvl = database.getIntArgs(p.getUniqueId(), "EDUCATION");
            educationInv.setItem((int) (educationLvl+9), new ItemStack(Material.STAINED_GLASS_PANE,1 ,(short) 5));

            for (int i=0;i<=5; i++){
                ItemStack item = new ItemStack(Material.KNOWLEDGE_BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("education."+i+".name")));
                List<String> lore = plugin.getConfig().getStringList("education."+i+".lore");
                List<String> coloredLore = new ArrayList<String>();
                for (String s : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', s));
                }
                meta.setLore(coloredLore);
                item.setItemMeta(meta);
                educationInv.setItem(i, item);
            }

            ItemStack stat = roomClass.createItem(Material.PAPER, ("Нынешнее образование: "
                    +ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("education."+(int)educationLvl+".name"))), ChatColor.WHITE);
            educationInv.setItem(26, stat);
            p.openInventory(educationInv);

        }
    }

    boolean questionSend = false;
    int slot;
    int rand;

    ArrayList<Integer> listOfDoubles = new ArrayList<Integer>();

    @EventHandler
    public void clickEvent(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.KNOWLEDGE_BOOK)){
            e.setCancelled(true);
            slot = e.getSlot();
            int curEduc = (int) database.getIntArgs(p.getUniqueId(), "EDUCATION");
            if (slot == curEduc || slot<curEduc){
                p.sendMessage("еблан ?");
            }else {
                rand = randomInt();
                listOfDoubles.add(rand);
                String question = plugin.getConfig().getString("education."+slot+".questions."+rand+".question");

                p.closeInventory();
                questionSend = true;
                p.sendMessage("");
                p.sendMessage("");
                p.sendMessage(ChatColor.YELLOW+"Напишите правильный ответ в " + ChatColor.AQUA +ChatColor.BOLD+ "чат");
                p.sendMessage(ChatColor.BOLD +question);
                p.sendMessage("");
            }
        }
    }
    String answer;
    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        if (questionSend){
            answer = plugin.getConfig().getString("education."+slot+".questions."+rand+".answer");
            if (e.getMessage().equalsIgnoreCase(answer)){
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.GREEN + "ПРАВИЛЬНО!");

                rand = randomInt();
                if (!(listOfDoubles.contains(rand))){
                    listOfDoubles.add(rand);
                    answer = plugin.getConfig().getString("education."+slot+".questions."+rand+".answer");
                    String question = plugin.getConfig().getString("education."+slot+".questions."+rand+".question");
                    p.sendMessage("");
                    p.sendMessage(ChatColor.BOLD +question);
                }else {
                    while (listOfDoubles.contains(rand)){
                        rand = randomInt();
                    }
                    listOfDoubles.add(rand);
                    answer = plugin.getConfig().getString("education."+slot+".questions."+rand+".answer");
                    String question = plugin.getConfig().getString("education."+slot+".questions."+rand+".question");
                    p.sendMessage("");
                    p.sendMessage(ChatColor.BOLD +question);
                }
                if (listOfDoubles.size() ==10){
                    p.sendMessage("");
                    p.sendMessage(ChatColor.GREEN+"Вы повысили свое образование поздравляю!");
                    p.sendMessage("");
                    database.updateArgs(p.getUniqueId(), "EDUCATION", slot);
                    listOfDoubles.clear();
                    questionSend = false;
                    educationInv.clear(slot+8);
                }
            }

            else {
                e.getPlayer().sendMessage(ChatColor.RED + "Вы ошиблись, больше попыток не будет");
                questionSend = false;
                e.setCancelled(true);
                listOfDoubles.clear();
            }


        }
    }



}
