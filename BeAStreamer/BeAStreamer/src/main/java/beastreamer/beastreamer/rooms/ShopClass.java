package beastreamer.beastreamer.rooms;

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
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopClass implements Listener {
    private BeAStreamer plugin;

    public ShopClass(BeAStreamer plugin) {
        this.plugin = plugin;
    }

    private final RoomClass roomClass = new RoomClass(plugin);
    private final Database database = new Database();



//    public ShopClass(JavaPlugin plugin) {
//        this.plugin = plugin;
//    }


////    private static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>( );
//
//    public static void createNPC(Player p){
//        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer world = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
//        GameProfile profile = new GameProfile(UUID.randomUUID(), ChatColor.AQUA + "Продавец");
//        EntityPlayer npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
//        npc.setLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ(),
//                p.getLocation().getYaw(), p.getLocation().getPitch());
//        addNPCPacket(npc);
////        NPC.add(npc);
//    }
//
//    public static void addNPCPacket(EntityPlayer npc){
//        for (Player player : Bukkit.getOnlinePlayers()){
//            PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
//            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
//        }
//    }
//
//
//    public static List<EntityPlayer> getNPC() {
//        return NPC;
//    }
//
//    public static void addJoinPacket(Player player){
//        for (EntityPlayer npc : NPC){
//            PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
//            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
//        }
//    }


//    @EventHandler
//    public void join(PlayerJoinEvent e){
//        Player p =e.getPlayer();
//        if (getNPC() == null)
//            return;
//
//        if (getNPC().isEmpty())
//            return;
//
//        addJoinPacket(p);
//    }


    NPCRegistry registry  = CitizensAPI.getNPCRegistry();
    NPC npc = registry.createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" +ChatColor.BOLD+ "Продавец");




    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        if (p.getWorld().equals(Bukkit.getWorld("shopWorld"))){
            if (firstClickJoin){
                p.sendMessage("<" +ChatColor.YELLOW + "" +ChatColor.BOLD+ "Продавец"+ "> "+ChatColor.WHITE+ "Приветствую!");
                p.sendMessage("<" +ChatColor.YELLOW + "" +ChatColor.BOLD+ "Продавец"+ "> " +ChatColor.WHITE+ "В этом магазине ты можешь покупат предметы по скидке.");
                firstClickJoin =false;
            }
            npc.spawn(p.getLocation());
        }
        if (e.getFrom().equals(Bukkit.getWorld("shopWorld"))){
            npc.despawn();
            npc.destroy();

        }
    }
    boolean firstClickJoin = true;



    @EventHandler
    public void disconnect(PlayerQuitEvent e){
        npc.despawn();
        npc.destroy();
    }

    public void buyMechanic(ItemStack curItem, Material material, String argForPath, int slot, Player p, int slotInWarehouse){
        if (curItem.getType().equals(material)){
            String path = argForPath+"." +slot;
            int coast = plugin.getConfig().getInt("market.accessories.products."+path+".salePrice");
            double balance = database.getIntArgs(p.getUniqueId(), "MONEY");
            if (balance>=coast){
                database.updateArgs(p.getUniqueId(), "MONEY", balance-coast);
                roomClass.statisticBoard(p);
                p.sendMessage(ChatColor.GREEN + "Успех...");
                p.sendMessage(ChatColor.GREEN + "Проверяйте склад!");
                p.closeInventory();


            }else {
                p.sendMessage(ChatColor.RED + "Пора работу...");
            }

        }
    }


    @EventHandler
    public void clickInInv(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        Inventory inv = plugin.getShopInv();
        if (e.getClickedInventory().equals(inv)){
            if (e.getCurrentItem().getType() != Material.BARRIER){
                buyMechanic(e.getCurrentItem(), Material.IRON_INGOT, "videoCards",e.getSlot(), p,9);
                if (e.getCurrentItem().getType().equals(Material.IRON_INGOT)){
                    database.updateArgs(p.getUniqueId(), "ACT_VIDEOCARD", e.getSlot());
                }
                buyMechanic(e.getCurrentItem(), Material.END_CRYSTAL, "processors",e.getSlot(), p,10);
                if (e.getCurrentItem().getType().equals(Material.END_CRYSTAL)){
                    database.updateArgs(p.getUniqueId(), "ACT_CORE", e.getSlot());
                }

                buyMechanic(e.getCurrentItem(), Material.IRON_TRAPDOOR, "motherboards",e.getSlot(), p, 11);
                if (e.getCurrentItem().getType().equals(Material.IRON_TRAPDOOR)){
                    database.updateArgs(p.getUniqueId(), "ACT_MB", e.getSlot());
                }
            }
        }
    }




}
