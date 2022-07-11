package beastreamer.beastreamer.rooms;


import beastreamer.beastreamer.BeAStreamer;
import beastreamer.beastreamer.database.Database;
import com.connorlinfoot.actionbarapi.ActionBarAPI;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
//import net.md_5.bungee.api.chat.TextComponent;


import java.util.List;

public class RoomClass implements Listener {

    BeAStreamer plugin;


    public RoomClass(BeAStreamer plugin) {
        this.plugin = plugin;
    }

    private final Database database =new Database();


    World roomWorld = Bukkit.getWorld("connectWorld");
    World arenaWorld = Bukkit.getWorld("arenaWorld");


    public ItemStack createItem(Material material, String displayName, ChatColor color) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(color + displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void addItemIntoInv(Inventory inv, int slot, ItemStack item) {
        inv.setItem(slot, item);
    }


//    public Inventory invCreator(Inventory inv, int slots, String GUIname, int slot, ItemStack item){
//
//        inv = Bukkit.createInventory(null, slots, GUIname);
//        return inv;
//    }

    public int randomInt(int arg0) {
        int i = (int) (Math.random() * 36);
        return i;
    }

    public void statisticBoard(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard sb = manager.getNewScoreboard();
        Objective obj = sb.registerNewObjective("Статистика", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score money = obj.getScore(ChatColor.BOLD + "Баланс:");
        money.setScore((int) database.getIntArgs(p.getUniqueId(), "MONEY"));

        Score followers = obj.getScore(ChatColor.BOLD + "Фоловеров:");
        followers.setScore((int) database.getIntArgs(p.getUniqueId(), "FOLLOWERS"));

        Score maxEnergy = obj.getScore(ChatColor.BOLD + "Энергия:");
        maxEnergy.setScore((int) database.getIntArgs(p.getUniqueId(), "CUR_ENERGY"));
        p.setScoreboard(sb);
    }



    public ItemStack addAccessories(Material material, String path,int slot){

        ItemStack item = createItem(material, plugin.getConfig()
                .getString("market.accessories.products."+path+".name"), ChatColor.WHITE);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(plugin.getConfig()
                .getStringList("market.accessories.products."+path+".lore"));
        item.setItemMeta(meta);


        addItemIntoInv(accessoriesInv, slot ,item);

        return item;

    }


    public void toggleEquipped(ItemStack curItem, int slot, Material material){
        if (curItem.getType().equals(material)){
            warehouse.setItem(slot,equipped);
        }
    }

    public void getDisplayNameBySlot(ItemStack curItem, Material material, String argForPath, int slot, Player p, String arg){
        if (curItem.getType().equals(material)) {
            database.updateArgs(p.getUniqueId(), arg, slot);
        }
    }

    public void buyMechanic(ItemStack curItem, Material material, String argForPath, int slot, Player p, int slotInWarehouse){
        if (curItem.getType().equals(material)){
            String path = argForPath+"." +slot;
            int coast = plugin.getConfig().getInt("market.accessories.products."+path+".price");
            double balance = database.getIntArgs(p.getUniqueId(), "MONEY");
            if (balance>=coast){
                database.updateArgs(p.getUniqueId(), "MONEY", balance-coast);
                statisticBoard(p);
                p.sendMessage(ChatColor.GREEN + "Успех...");
                p.sendMessage(ChatColor.GREEN + "Проверяйте склад!");
                p.closeInventory();
                warehouse.setItem( slotInWarehouse, curItem);


            }else {
                p.sendMessage(ChatColor.RED + "Пора работу...");
            }

        }
    }

//    public void addAccessoriesFromDB(){
//        //ДОБАВЬ ПРЕДМЕТ МАТЕРИАЛА АРГУМЕНТА, С НАЗВАНИЕМ КОТОРЫЙ ТЫ ПОЛУЧИШЬ ЧЕРЕЗ КОНФИГ С СЛОТОМ ИЗ БД
//    }

    public void addNBT(NBTItem nbtItem, String path){
        nbtItem.setInteger("price", plugin.getConfig()
                .getInt("market.accessories.products."+path+".price"));
    }

    private int curWeb = 0;

    Inventory warehouse = Bukkit.createInventory(null, 18, "Склад");


    Inventory postInv = Bukkit.createInventory(null, 36, "Почта");
    Inventory accessoriesInv = Bukkit.createInventory(null, 36, "Комплектующие");
    ItemStack eMail = createItem(Material.COOKIE, "Письмо", ChatColor.WHITE);

    ItemStack notEquipped = new ItemStack(Material.STAINED_GLASS_PANE,1 ,(short) 14);

    ItemStack equipped = new ItemStack(Material.STAINED_GLASS_PANE,1 ,(short) 5);





    // ДОСТУП К ИГРАМ
    boolean arenaGame = false;
    boolean secondGame = false;
    boolean secretGame = false;








    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        p.teleport(Bukkit.getWorld("connectWorld").getSpawnLocation());

        database.updateArgs(p.getUniqueId(), "MONEY", 100000);

        ItemMeta notEquippedMeta = notEquipped.getItemMeta();
        notEquippedMeta.setDisplayName(ChatColor.RED+"НЕ ИСПОЛЬЗУЕТСЯ");
        notEquipped.setItemMeta(notEquippedMeta);

        ItemMeta equippedMeta = equipped.getItemMeta();
        equippedMeta.setDisplayName(ChatColor.GREEN+"ИСПОЛЬЗУЕТСЯ");
        equipped.setItemMeta(equippedMeta);

        for (int i = 0; i<9; i++){
            warehouse.setItem(i, notEquipped);



        }





//
//        p.sendMessage(String.valueOf(database.getIntArgs(p.getUniqueId(), "BOOST_FOLL")));
//        p.teleport(roomWorld.getSpawnLocation());
//        TextComponent textComponent = new TextComponent("Hello");
//        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept"));
//        p.spigot().sendMessage(textComponent);



//        Vehicle allVeh = e.getPlayer().getLocation().getChunk().getEntities();
        statisticBoard(p);

    }


    @EventHandler
    public void sOnJoinEvent(PlayerJoinEvent e){
        Player p =e.getPlayer();



        addAccessories(Material.IRON_INGOT, "videoCards.1",1);


//            //RTX 1660
        addAccessories(Material.IRON_INGOT, "videoCards.10",10);

//            //RTX 2080
        addAccessories(Material.IRON_INGOT, "videoCards.19", 19);

//            //RTX 3090
        addAccessories(Material.IRON_INGOT, "videoCards.28", 28);



//             PROCESSORS
        //i3
        addAccessories(Material.END_CRYSTAL, "processors.3", 3);

        //i5
        addAccessories(Material.END_CRYSTAL, "processors.12", 12);

        //i7
        addAccessories(Material.END_CRYSTAL, "processors.21", 21);

        //i9
        addAccessories(Material.END_CRYSTAL, "processors.30",30);

        //MOTHERBOARDS
        //mb3
        addAccessories(Material.IRON_TRAPDOOR, "motherboards.5",5);

        //mb5
        addAccessories(Material.IRON_TRAPDOOR, "motherboards.14",14);

        //mb7
        addAccessories(Material.IRON_TRAPDOOR, "motherboards.23", 23);

        //mb9
        addAccessories(Material.IRON_TRAPDOOR, "motherboards.32",32);


        double slotOfVideocard = database.getIntArgs(p.getUniqueId(), "ACT_VIDEOCARD");
        double slotOfCore = database.getIntArgs(p.getUniqueId(), "ACT_CORE");
        double slotOfMotherboard = database.getIntArgs(p.getUniqueId(), "ACT_MB");



        ItemStack videocard = accessoriesInv.getItem((int) slotOfVideocard);
        ItemStack core = accessoriesInv.getItem((int) slotOfCore);
        ItemStack mb = accessoriesInv.getItem((int) slotOfMotherboard);

        warehouse.setItem(9, videocard);
        warehouse.setItem(10, core);
        warehouse.setItem(11, mb);


        if (database.getIntArgs(p.getUniqueId(), "ACT_VIDEOCARD") != 0){
            warehouse.setItem(0, equipped);
        }
        if (database.getIntArgs(p.getUniqueId(), "ACT_CORE") != 0){
            warehouse.setItem(1, equipped);
        }
        if (database.getIntArgs(p.getUniqueId(), "ACT_MB") != 0){
            warehouse.setItem(2, equipped);
        }

    }

    @EventHandler
    public void switchWorld(PlayerChangedWorldEvent e){
        Player p =e.getPlayer();
        double slotOfVideocard = database.getIntArgs(p.getUniqueId(), "ACT_VIDEOCARD");
        double slotOfCore = database.getIntArgs(p.getUniqueId(), "ACT_CORE");
        double slotOfMotherboard = database.getIntArgs(p.getUniqueId(), "ACT_MB");



        ItemStack videocard = accessoriesInv.getItem((int) slotOfVideocard);
        ItemStack core = accessoriesInv.getItem((int) slotOfCore);
        ItemStack mb = accessoriesInv.getItem((int) slotOfMotherboard);

        warehouse.setItem(9, videocard);
        warehouse.setItem(10, core);
        warehouse.setItem(11, mb);
    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent e) {
        if (e.getPlayer().isInsideVehicle()) {
            Vehicle veh = (Vehicle) e.getPlayer().getVehicle();
            veh.eject();
        }
    }

    @EventHandler
    public void clickItemCancelled(InventoryClickEvent e) {
        if (e.getCurrentItem().getType() != Material.AIR) {
            if (e.getClickedInventory().getSize() == 36) {
                e.setCancelled(true);

            }
        }

    }

    // STREAM ////////////////////////////////////////////

    @EventHandler
    public void startOfStream(VehicleEnterEvent e) {
        Player p = (Player) e.getEntered();
        Inventory PCInv = Bukkit.createInventory(null, 36, "Компьютер");
        if (e.getVehicle() instanceof Minecart) {
//            p.getLocation().setPitch((float) -1.54);
//            p.getLocation().setYaw((float) -93.34);
            addItemIntoInv(PCInv, 0, createItem(Material.EMERALD, "Начать стрим", ChatColor.GREEN));
            addItemIntoInv(PCInv, 1, createItem(Material.BEACON, "Интернет магазин", ChatColor.GREEN));
            addItemIntoInv(PCInv, 2, createItem(Material.PAPER, "Почта", ChatColor.GREEN));
            p.openInventory(PCInv);


        }
    }

    ////// START STREAM ////////////////////////////////////////////////////////////////////////

    @EventHandler
    public void startOfStream(InventoryClickEvent e) {
        ItemStack arena = createItem(Material.GOLDEN_APPLE, "ПВЕ", ChatColor.BOLD);
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().equals(createItem(Material.EMERALD, "Начать стрим", ChatColor.GREEN))) {
            if (database.getIntArgs(p.getUniqueId(), "CUR_ENERGY") >= 20) {
                Inventory liveStream = Bukkit.createInventory(null, 36, "Прямой эфир");
                addItemIntoInv(liveStream, randomInt(36), createItem(Material.WEB, "Подключение", ChatColor.WHITE));
                p.openInventory(liveStream);
            } else {
                p.sendMessage("Я слишком устал, вздремнуть бы сейчас");
                p.closeInventory();
            }

        }
        int needWeb = 2;

        if (e.getCurrentItem().equals(createItem(Material.WEB, "Подключение", ChatColor.WHITE))) {
            e.getCurrentItem().setType(Material.AIR);
            e.getClickedInventory().remove(e.getCurrentItem());
            addItemIntoInv(e.getClickedInventory(), randomInt(36), createItem(Material.WEB, "Подключение", ChatColor.WHITE));
            if (curWeb == needWeb) {
                p.sendMessage("Вы выполнили!");


                double curFollowers = database.getIntArgs(p.getUniqueId(), "FOLLOWERS");

                if (curFollowers < 1000) {
                    int followersRand = randomInt(10);
                    double follWithBoost = followersRand * database.getIntArgs(p.getUniqueId(), "BOOST_FOLL");
                    double foll = curFollowers + follWithBoost;
                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас " + ChatColor.GREEN + (int) follWithBoost + " новых фолловеров" + ChatColor.AQUA + " [" + (float) database.getIntArgs(p.getUniqueId(), "BOOST_FOLL") + "x]", 100);
                    database.updateArgs(p.getUniqueId(), "FOLLOWERS", foll);
                    statisticBoard(p);
                }
//                else if(curFollowers<500){
//                    int newFollowers = randomInt(50);
//                    int foll = curFollowers+newFollowers;
//                    p.sendMessage(String.valueOf(newFollowers));
//                    p.sendMessage(String.valueOf(curFollowers));
//                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас "+ChatColor.GREEN+newFollowers+" новых фолловеров", 100);
//                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".followers", foll);
//                    statisticBoard(p);
//                }else if(curFollowers<1000){
//                    int newFollowers = randomInt(100);
//                    int foll = curFollowers+newFollowers;
//                    p.sendMessage(String.valueOf(newFollowers));
//                    p.sendMessage(String.valueOf(curFollowers));
//                    ActionBarAPI.sendActionBar(p, "Поздравляю, у вас "+ChatColor.GREEN+newFollowers+" новых фолловеров", 100);
//                    plugin.getConfig().set("streamers.users."+p.getUniqueId()+".followers", foll);
//                    statisticBoard(p);
//
//                }


                curWeb = 0;
                p.closeInventory();

                Inventory chooseThemeOfStream = Bukkit.createInventory(null, 18, "Выберите тему стрима");
                addItemIntoInv(chooseThemeOfStream, 0, arena);

                p.openInventory(chooseThemeOfStream);


                double newEnergy = database.getIntArgs(p.getUniqueId(), "CUR_ENERGY") - 20;
                database.updateArgs(p.getUniqueId(), "CUR_ENERGY", newEnergy);
                statisticBoard(p);
            } else {
                curWeb = curWeb + 1;
                if (curWeb % 10 == 0) {
                    int a = needWeb - curWeb;
                    if (a == 0) {

                    } else {
                        p.sendMessage("Осталось: " + a);
                    }
                }
            }
        }

        if (arenaGame) {
            if (e.getCurrentItem().equals(arena)) {
                e.setCancelled(true);
                Location loc = new Location(Bukkit.getWorld("arenaWorld"), 0, 70, 0);
                p.teleport(loc);
            }
        }


        if (e.getCurrentItem().getType().equals(Material.GOLD_PICKAXE)) {
            e.setCancelled(true);
            Location loc = new Location(Bukkit.getWorld("jobWorld"), 0, 70, 0);
            p.teleport(loc);
        }
        if (e.getCurrentItem().getType().equals(Material.APPLE)) {
            e.setCancelled(true);
            Location loc = new Location(Bukkit.getWorld("shopWorld"), 0, 70, 0);
            p.teleport(loc);
        }
        if (e.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL)){
            e.setCancelled(true);
            Location loc = new Location(Bukkit.getWorld("educationWorld"), 0, 70, 0);
            p.teleport(loc);
        }

    }



    /////// NET MARKET ///////////////////////////////////////////////////////////
    @EventHandler
    public void netMarket(InventoryClickEvent e) {
        // КОМПЛЕКТУЮЩИЕ
        ItemStack accessories = createItem(Material.DAYLIGHT_DETECTOR, plugin.getConfig().getString("market.slots.videocard.name"), ChatColor.WHITE);
        ItemMeta metaAccessories = accessories.getItemMeta();
        metaAccessories.setDisplayName(plugin.getConfig().getString("market.accessories.displayName"));
        List<?> lore = plugin.getConfig().getList("market.accessories.lore");
        metaAccessories.setLore((List<String>) lore);

        accessories.setItemMeta(metaAccessories);


        //



        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.BEACON)) {
            Inventory webMarketInv = Bukkit.createInventory(null, 36, "Интернет магазин");
            webMarketInv.setItem(0, accessories);

            p.openInventory(webMarketInv);
        }
        if (e.getCurrentItem().equals(accessories)){

            // VIDEOCARDS

            // GT 1030




            p.openInventory(accessoriesInv);
        }


        if (e.getClickedInventory().equals(accessoriesInv)){
            ItemStack curItem = e.getCurrentItem();
            if (curItem.getType() != Material.AIR){
                int slot = e.getSlot();
                buyMechanic(curItem, Material.IRON_INGOT, "videoCards",slot, p,9);
                getDisplayNameBySlot(curItem, Material.IRON_INGOT, "videoCards", slot, p, "ACT_VIDEOCARD" );
                warehouse.setItem(0, equipped);

                buyMechanic(curItem, Material.END_CRYSTAL, "processors",slot, p,10);
                getDisplayNameBySlot(curItem, Material.END_CRYSTAL, "processors", slot, p, "ACT_CORE" );
                warehouse.setItem(1, equipped);

                buyMechanic(curItem, Material.IRON_TRAPDOOR, "motherboards",slot, p, 11);
                getDisplayNameBySlot(curItem, Material.IRON_TRAPDOOR, "motherboards", slot, p, "ACT_MB" );
                warehouse.setItem(2, equipped);


                double videocard = database.getIntArgs(p.getUniqueId(), "ACT_VIDEOCARD");
                double core =  database.getIntArgs(p.getUniqueId(), "ACT_CORE");
                double mb = database.getIntArgs(p.getUniqueId(), "ACT_MB");

                //9, 36,63
                if (videocard + core +mb ==9){
                    p.sendMessage("");
                    p.sendMessage(ChatColor.YELLOW + "Поздравляю!");
                    p.sendMessage(ChatColor.YELLOW + "Ты разблокировал новую игру!");
                    p.sendMessage("");
                    arenaGame = true;
                }


            }
        }
// /ПОТОМ ДОБАВЛЮ В МЕТОД



        //////// VIDEOCARD ////////////////////////////////////////////////////////
//        if (e.getCurrentItem().getType().equals(Material.DAYLIGHT_DETECTOR)) {
//            if (warehouse.contains(videoCard)) {
//                p.sendMessage(ChatColor.RED + "У вас уже куплен этот товар");
//            } else {
//                if (database.getIntArgs(p.getUniqueId(), "MONEY") >= plugin.getConfig().getInt("market.slots.videocard.price")) {
//                    ItemStack sold = createItem(Material.BARRIER, "КУПЛЕНО", ChatColor.RED);
//                    int slot = e.getSlot();
//                    e.getClickedInventory().setItem(slot, sold);
//                    double newMoney = database.getIntArgs(p.getUniqueId(), "MONEY") - plugin.getConfig().getInt("market.slots.videocard.price");
//                    database.updateArgs(p.getUniqueId(), "MONEY", newMoney);
//                    warehouse.addItem(videoCard);
//                    statisticBoard(p);
//                } else {
//                    p.sendMessage("Пора на работу");
//                }
//            }
//
//        }
    }

    Boolean notSended = true;
    Boolean partnerProg = false;

    @EventHandler
    public void post(InventoryClickEvent e) {


        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem().getType().equals(Material.PAPER)) {
            double curFollowers = database.getIntArgs(p.getUniqueId(), "FOLLOWERS");
            if (curFollowers > 100) {
                if (notSended) {
                    postInv.addItem(eMail);
                    notSended = false;
                }
            }
            p.openInventory(postInv);


        }
        if (e.getCurrentItem().getType().equals(Material.COOKIE)) {
            partnerProg = true;
            p.sendMessage("");
            p.sendMessage("");
            p.sendMessage("Мы предлогаем вам воспользоваться нашей партнерской программой");
            p.sendMessage("");
            p.sendMessage("Чтобы принять предложение напишите " + ChatColor.GREEN + "ПРИНЯТЬ");
            p.sendMessage("Чтобы отклонить предложение напишите " + ChatColor.RED + "ОТКАЗАТЬСЯ");
            p.sendMessage("");
        }
    }

    @EventHandler
    public void acceptChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        double boostFoll = database.getIntArgs(p.getUniqueId(), "BOOST_FOLL");
        if (msg.equalsIgnoreCase("принять")) {
//            postInv.remove(Material.COOKIE);
            p.sendMessage("");
            p.sendMessage("");
            p.sendMessage("Вы приняли предложение");
            p.sendMessage("");
            p.sendMessage("Для вас активирован бустер фолловеров " + ChatColor.AQUA + "[x1.1]");

            double newBoostFoll = boostFoll + 0.1;
            database.updateArgs(p.getUniqueId(), "BOOST_FOLL", newBoostFoll);

            p.sendMessage("Нынешний бустер: " +(float) database.getIntArgs(p.getUniqueId(), "BOOST_FOLL"));
            p.sendMessage("");
            p.sendMessage("");

            partnerProg = false;
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void warehouse(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (e.getClickedBlock().getType() == (Material.CHEST)){
                p.sendMessage("dsdsdsd");
                e.setCancelled(true);
                p.openInventory(warehouse);



            }
        }
    }



    @EventHandler
    public void warehouseClickInv(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory().equals(warehouse)){
            e.setCancelled(true);
//            toggleEquipped(e.getCurrentItem(), 0, Material.IRON_INGOT);
//            getDisplayNameBySlot(e.getCurrentItem(), Material.IRON_INGOT, "videoCards", e.getSlot(), p);
//
//            toggleEquipped(e.getCurrentItem(), 1, Material.END_CRYSTAL);
//
//            toggleEquipped(e.getCurrentItem(), 2, Material.IRON_TRAPDOOR);


        }
    }





    @EventHandler
    public void playerEject(InventoryCloseEvent e){
        Player p = (Player) e.getPlayer();
//        if (p.getInventory().equals(liveStream)){
//            if (p.isInsideVehicle()){
//                Vehicle veh = (Vehicle) e.getPlayer().getVehicle();
//                if (veh instanceof Minecart){
//                    veh.eject();
//                }
//            }
//        }
    }



    // SLEEP ////////////////////////////////////////////

    @EventHandler
    public void playerSleep(PlayerBedEnterEvent e){
        Player p = e.getPlayer();
        database.updateArgs(p.getUniqueId(), "CUR_ENERGY", database.getIntArgs(p.getUniqueId(), "MAX_ENERGY"));
        statisticBoard(p);


    }
    @EventHandler
    public void playerWakeUp(PlayerBedLeaveEvent e){



    }

    // GO OUT ////////////////////////////////////////////
    @EventHandler
    public void leaveOut(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Inventory placesInv = Bukkit.createInventory(null, 36, "Выбор места");
        Material Door = Material.WOODEN_DOOR;



        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (p.getWorld().equals(roomWorld)){
                if(e.getClickedBlock().getType().equals(Door)){
                    e.setCancelled(true);
                    ItemStack work = createItem(Material.GOLD_PICKAXE, "Работа", ChatColor.BOLD);
                    ItemStack shop = createItem(Material.APPLE, "Магазин", ChatColor.BOLD);
                    ItemStack education = createItem(Material.BOOK_AND_QUILL, "Образование", ChatColor.BOLD);
                    addItemIntoInv(placesInv, 0, work);
                    addItemIntoInv(placesInv, 1, shop);
                    addItemIntoInv(placesInv, 2, education);
                    p.openInventory(placesInv);
                }
            }

        }

    }


//    @EventHandler
//    public void setNight(PlayerInteractEvent e){
//        e.getPlayer().sendMessage(String.valueOf(e.getClickedBlock().getType()));
//
//        if (e.getPlayer().getWorld().getTime()<=13000){
//            if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
//                e.getPlayer().getWorld().setTime(13000);
//            }
//        }
//    }
}
