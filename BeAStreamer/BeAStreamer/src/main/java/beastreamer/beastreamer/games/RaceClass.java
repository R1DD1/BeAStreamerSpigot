package beastreamer.beastreamer.games;

import beastreamer.beastreamer.BeAStreamer;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class RaceClass implements Listener {
    private final BeAStreamer plugin;

    public RaceClass(BeAStreamer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public  void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p  = e.getPlayer();
        if (e.getFrom().equals(Bukkit.getWorld("raceWorld"))){
            if (p.isInsideVehicle()){
                Entity vehicle = p.getVehicle();
                p.eject();
            }
        }

        if (p.getWorld().equals(Bukkit.getWorld("raceWorld"))){
            p.getWorld().spawnEntity(p.getLocation(), EntityType.BOAT);
            for(Entity ents : p.getWorld().getEntities()){

                if (ents instanceof Boat){
                    if (ents.isEmpty()){
                        ents.setPassenger(p);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onLeaveVeh(VehicleExitEvent e){
        if (e.getVehicle() instanceof Boat){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (p.isInsideVehicle()){
            p.eject();
        }
    }
}
