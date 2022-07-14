package beastreamer.beastreamer;


import beastreamer.beastreamer.database.Database;
import beastreamer.beastreamer.games.ArenaGame;
import beastreamer.beastreamer.games.JustChattingClass;
import beastreamer.beastreamer.games.RaceClass;
import beastreamer.beastreamer.jobs.Education;
import beastreamer.beastreamer.jobs.JobClass;
import beastreamer.beastreamer.rooms.RoomClass;
import beastreamer.beastreamer.rooms.ShopClass;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;



public final class BeAStreamer extends JavaPlugin implements Listener {
    private static BeAStreamer instance;



    private Connection connection;
    public String host, database, username, password, table;
    public int port;



    public ItemStack createItem(Material material, String displayName, ChatColor color) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(color + displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack addToMarket(Material material, String path){
        ItemStack item = createItem(material, getConfig()
                .getString("market.accessories.products."+path+".name"), ChatColor.WHITE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(getConfig()
                .getStringList("market.accessories.products."+path+".lore"));
        item.setItemMeta(meta);
        return  item;
    }

    public int slotForMarket (int number){
        int[] slots = new int[]{1, 10, 19, 28, 3, 12, 21, 30, 5, 14, 23, 32};
        return slots[number];
    }
    public ItemStack methodForMarket(int number){
        ItemStack [] itemsForMarket = new ItemStack[12];
        itemsForMarket[0] = addToMarket(Material.IRON_INGOT, "videoCards.1");
        itemsForMarket[1] = addToMarket(Material.IRON_INGOT, "videoCards.10");
        itemsForMarket[2] = addToMarket(Material.IRON_INGOT, "videoCards.19");
        itemsForMarket[3] = addToMarket(Material.IRON_INGOT, "videoCards.28");

        itemsForMarket[4] = addToMarket(Material.END_CRYSTAL, "processors.3");
        itemsForMarket[5] = addToMarket(Material.END_CRYSTAL, "processors.12");
        itemsForMarket[6] = addToMarket(Material.END_CRYSTAL, "processors.21");
        itemsForMarket[7] = addToMarket(Material.END_CRYSTAL, "processors.30");

        itemsForMarket[8] = addToMarket(Material.IRON_TRAPDOOR, "motherboards.5");
        itemsForMarket[9] = addToMarket(Material.IRON_TRAPDOOR, "motherboards.14");
        itemsForMarket[10] = addToMarket(Material.IRON_TRAPDOOR, "motherboards.23");
        itemsForMarket[11] = addToMarket(Material.IRON_TRAPDOOR, "motherboards.32");
        return itemsForMarket[number];
    }

    @Override
    public void onEnable() {

        mySQLSetup();

        instance = this;


        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getServer().broadcastMessage("");
                Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Магазин обновился!");
                Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Следуйщее обновление через 8 часов!");
                Bukkit.getServer().broadcastMessage("");

                for (int i = 0;i<12;i++){
                    shopInv.setItem(slotForMarket(i), new ItemStack(Material.BARRIER));
                }

                int i = (int) (Math.random() * 12);

                shopInv.setItem(slotForMarket(i),methodForMarket(i));


            }
        },20, 20*60*60*8);



        // WORLDS

        WorldCreator connectWorld = new WorldCreator("connectWorld");
        connectWorld.type(WorldType.FLAT);
        connectWorld.generatorSettings("2;0;1;");
        connectWorld.createWorld();

        WorldCreator arenaWorld = new WorldCreator("arenaWorld");
        arenaWorld.type(WorldType.FLAT);
        arenaWorld.generatorSettings("2;0;1;");
        arenaWorld.createWorld();

        WorldCreator jobWorld = new WorldCreator("jobWorld");
        jobWorld.type(WorldType.FLAT);
        jobWorld.generatorSettings("2;0;1;");
        jobWorld.createWorld();

        WorldCreator shopWorld = new WorldCreator("shopWorld");
        shopWorld.type(WorldType.FLAT);
        shopWorld.generatorSettings("2;0;1;");
        shopWorld.createWorld();

        WorldCreator educationWorld = new WorldCreator("educationWorld");
        educationWorld.type(WorldType.FLAT);
        educationWorld.generatorSettings("2;0;1;");
        educationWorld.createWorld();

        WorldCreator justChattingWorld = new WorldCreator("justChattingWorld");
        justChattingWorld.type(WorldType.FLAT);
        justChattingWorld.generatorSettings("2;0;1;");
        justChattingWorld.createWorld();

        WorldCreator raceWorld = new WorldCreator("raceWorld");
        raceWorld.type(WorldType.FLAT);
        raceWorld.generatorSettings("2;0;1;");
        raceWorld.createWorld();



        Logger log = Bukkit.getLogger();
        log.info("ENABLE");

        getServer().getPluginManager().registerEvents(new RoomClass(this),this);
        getServer().getPluginManager().registerEvents(new JobClass(this),this);
        getServer().getPluginManager().registerEvents(new ArenaGame(this),this);
        getServer().getPluginManager().registerEvents(new Database(), this);
        getServer().getPluginManager().registerEvents(new ShopClass(this), this);
        getServer().getPluginManager().registerEvents(new Education(this), this);
        getServer().getPluginManager().registerEvents(new JustChattingClass(this), this);
        getServer().getPluginManager().registerEvents(new RaceClass(this), this);

        getServer().getPluginManager().registerEvents(this, this);

    }


//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (label.equalsIgnoreCase("createnpc")){
//            if (!(sender instanceof Player)){
//                return true;
//            }
//            Player player = (Player) sender;
////            ShopClass.createNPC(player);
//            player.sendMessage("created");
//        }
//        return false;
//    }

    public static BeAStreamer getInstance(){
        return instance;

    }


//    MySQL

    public void mySQLSetup(){
        host = "localhost";
        port = 3306;
        database = "test1";
        password="";
        username = "root";
        table = "player_statistic";

        try{
            synchronized (this){
                if (getConnection()!=null && !getConnection().isClosed()){
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://"
                        + this.host + ":" + this.port + "/" + this.database, this.username, this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"DATABASE WORKING");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }




    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    //LISTENER
    //LISTENER
    //LISTENER
    //LISTENER





    Inventory shopInv = Bukkit.createInventory(null, 36, "Оффлайн магазин");


    public Inventory getShopInv() {
        return shopInv;
    }

    @EventHandler
    public void onClickNPC(PlayerInteractEntityEvent e){
        Player p = e.getPlayer();
        if (!(e.getRightClicked().getType().equals(EntityType.MINECART))){
            if (e.getRightClicked().getCustomName().equals(ChatColor.YELLOW + "" +ChatColor.BOLD+ "Продавец")) {
                p.openInventory(shopInv);
            }
        }

    }


}
