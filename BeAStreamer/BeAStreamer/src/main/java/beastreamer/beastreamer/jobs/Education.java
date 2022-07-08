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

            for (int i=0;i<=5; i++){
                ItemStack item = new ItemStack(Material.BOOK);
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

}
