package beastreamer.beastreamer;

import beastreamer.beastreamer.database.Database;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class JobClass implements Listener {

    //PLUGIN
    BeAStreamer plugin;

    public JobClass(BeAStreamer instanse) {
        plugin = instanse;
    }

    private final RoomClass RoomClassObj = new RoomClass(plugin);
    private final Database database = new Database();

    //START WORLD//

    World jobWorld = Bukkit.getWorld("jobWorld");


    @EventHandler
    public void onSwitchWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        p.sendMessage("Вы попали в мир РАБОТЫ ");
    }

    //VARIABLES

    ItemStack Work[] = Work();
    ItemStack BakerCraft[] = BakerCraft();
    ItemStack BakerItem[] = BakerItem();
    boolean RESPAWN_WEB = true;

    boolean  RandColorWool = true;
    ItemStack RandColorTerracotta ;
    byte randBlock;

    float LocationTerracottaLBlock = 54;

    Block Gravelblock;


    //METHODS
    public ItemStack[] Work() {
        ItemStack Work[] = new ItemStack[9];
        Work[0] = RoomClassObj.createItem(Material.CLAY_BALL, "Домработник", ChatColor.GRAY);
        Work[1] = RoomClassObj.createItem(Material.COOKED_FISH, "Рыбак", ChatColor.DARK_GRAY);
        Work[2] = RoomClassObj.createItem(Material.BREAD, "Повар", ChatColor.AQUA);
        Work[3] = RoomClassObj.createItem(Material.BOOK, "Бухалтер", ChatColor.BLUE);
        Work[4] = RoomClassObj.createItem(Material.BLAZE_ROD, "Дизайнер", ChatColor.LIGHT_PURPLE);
        Work[5] = RoomClassObj.createItem(Material.IRON_BLOCK, "Стажер-Программист", ChatColor.DARK_PURPLE);
        Work[6] = RoomClassObj.createItem(Material.GOLD_BLOCK, "Junior-Программист", ChatColor.GREEN);
        Work[7] = RoomClassObj.createItem(Material.DIAMOND_BLOCK, "Middle-Программист", ChatColor.YELLOW);
        Work[8] = RoomClassObj.createItem(Material.EMERALD_BLOCK, "Senior-Программист", ChatColor.RED);
        return Work;
    }

    public Location[] PlaceWork(Player p) {
        Location PLocation[] = new Location[9];
        PLocation[0] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[1] = new Location(p.getWorld(), -8.577, 54.2, -13.544);
        PLocation[2] = new Location(p.getWorld(), -11.5, 54.2, 1.5);
        PLocation[3] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[4] = new Location(p.getWorld(), -19, 55.2, -15.0);
        PLocation[5] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[6] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[7] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        PLocation[8] = new Location(p.getWorld(), -0.5, 53.2, -13.5);
        return PLocation;
    }

    public ItemStack[] BakerCraft()
    {
        ItemStack BakerCraft[] = new ItemStack[9];
        BakerCraft[0] = RoomClassObj.createItem(Material.COOKED_CHICKEN, "Курица с печёным яблоком", ChatColor.DARK_GREEN);
        BakerCraft[1] = RoomClassObj.createItem(Material.COOKED_BEEF, "Стейк из говядины", ChatColor.DARK_GREEN);
        BakerCraft[2] = RoomClassObj.createItem(Material.GOLDEN_APPLE, "Золотое яблоко", ChatColor.DARK_GREEN);
        BakerCraft[3] = RoomClassObj.createItem(Material.COOKED_CHICKEN, "Курица с печёным Золотым яблоком", ChatColor.YELLOW);
        BakerCraft[4] = RoomClassObj.createItem(Material.MUSHROOM_SOUP, "Куриный суп", ChatColor.YELLOW);
        BakerCraft[5] = RoomClassObj.createItem(Material.BREAD, "Хлеб", ChatColor.YELLOW);
        BakerCraft[6] = RoomClassObj.createItem(Material.GRILLED_PORK, "Нагенцы", ChatColor.RED);
        BakerCraft[7] = RoomClassObj.createItem(Material.PUMPKIN_PIE, "Тыквеный пирог", ChatColor.RED);
        BakerCraft[8] = RoomClassObj.createItem(Material.RABBIT_STEW, "Тыквеный суп", ChatColor.RED);
        return BakerCraft;
    }

    public  ItemStack[] DesinerMaterial()
    {
        ItemStack DesinerItem[] = new ItemStack[16];
        for(short i = 0 ; i < 16 ;i++) {
            DesinerItem[i] = createItemWithDurability(Material.STAINED_CLAY, "Terracotta", ChatColor.BLACK, i);
        }

        return DesinerItem;
    }

    public int HandOverToolID(int i) {

        switch (i) {
            case 0:
                return 268;


            case 1:
                return 346;

            default:
                return 0;
        }
    }

    public ItemStack createItemWithDurability(Material m , String displayName,ChatColor c,short d)
    {
        ItemStack Item = RoomClassObj.createItem(m, displayName, c);
        Item.setDurability(d);
        return Item;
    }

    //LOGIC//

    Inventory GoToWork;

    @EventHandler
    public void GoToWork(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.WORKBENCH)) {

                Inventory placesInv = Bukkit.createInventory(null, 9, "Работа");
                GoToWork = placesInv;
                e.setCancelled(true);

                for (int i = 0; i < 9; i++) {
                    RoomClassObj.addItemIntoInv(placesInv, i, Work[i]);
                }
                e.getPlayer().openInventory(placesInv);


            }

        }
    }

    @EventHandler
    public void ChooseWork(InventoryClickEvent e) {

            if (e.getClickedInventory().getTitle() == GoToWork.getTitle()) {
                Player p = (Player) e.getWhoClicked();
                Location location[] = PlaceWork(p);
                for (int i = 0; i < 9; i++) {
                    if (e.getCurrentItem().getType().equals(Work[i].getType())) {
                        e.setCancelled(true);
                        p.teleport(location[i]);
                        if (!p.getInventory().contains(HandOverToolID(i)) && HandOverToolID(i) != 0) {
                            p.getInventory().addItem(new ItemStack(HandOverToolID(i)));
                        }
                        break;
                    }
                }
            }
            return;

    }

    ///////////////////NOT WORK UPDATE INTERFACE//////////////////KEY ERROR ~?
    @EventHandler
    public void Housekeeper(BlockBreakEvent block) {
        if (block.getBlock().getType() == Material.WEB && RESPAWN_WEB) {
            int rand = Math.abs((int) (Math.random() * 4));
            /* KEY:~? */
            database.updateArgs(block.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(block.getPlayer().getUniqueId(), "MONEY") + rand);
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.getBlock().setType(Material.WEB);
                }
            }.runTaskLater(plugin, 140);
        }

    }

    @EventHandler
    public void Fisherman(PlayerFishEvent e) {
        if (e.getCaught() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getPlayer().getInventory().contains(Material.RAW_FISH)) {
                        int rand = Math.abs((int) (Math.random() * (30 - 10 + 1) + 10));
                        for (int i = 0; i < e.getPlayer().getInventory().getSize(); i++) {
                            if (e.getPlayer().getOpenInventory().getItem(i).getType() == Material.RAW_FISH) {
                                database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") + rand);
                                e.getPlayer().getInventory().removeItem(e.getPlayer().getOpenInventory().getItem(i));
                                e.getPlayer().updateInventory();
                                e.getPlayer().sendMessage("YEEEEEE!");
                                break;
                            }
                        }
                    }

                }
            }.runTaskLater(plugin, 30);
        }
    }


    public ItemStack[] BakerItem() {
        ItemStack BakerItem[] = new ItemStack[9];
        BakerItem[0] = RoomClassObj.createItem(Material.COAL, "Топливо", ChatColor.WHITE);
        BakerItem[1] = RoomClassObj.createItem(Material.RAW_BEEF, "Мясо", ChatColor.WHITE);
        BakerItem[2] = RoomClassObj.createItem(Material.RAW_CHICKEN, "Курица", ChatColor.WHITE);
        BakerItem[3] = RoomClassObj.createItem(Material.SUGAR, "Мука", ChatColor.WHITE);
        BakerItem[4] = RoomClassObj.createItem(Material.APPLE, "Яблоко", ChatColor.WHITE);
        BakerItem[5] = RoomClassObj.createItem(Material.GOLD_NUGGET, "Золотой самородок", ChatColor.WHITE);
        BakerItem[6] = RoomClassObj.createItem(Material.BOWL, "Тарелка", ChatColor.WHITE);
        BakerItem[7] = RoomClassObj.createItem(Material.WATER_BUCKET, "Вода", ChatColor.WHITE);
        BakerItem[8] = RoomClassObj.createItem(Material.PUMPKIN, "Тыква", ChatColor.WHITE);
        return BakerItem;
    }

    int Itm = 0;
    Inventory Invent = Bukkit.createInventory(null, 9, "Столешница");
    @EventHandler
    public void BakerTakeRawFood(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {

                Inventory placesInv = Bukkit.createInventory(null, 9, "Материалы");
                e.setCancelled(true);

                for (int i = 0; i < 9; i++) {
                    RoomClassObj.addItemIntoInv(placesInv, i, BakerItem[i]);
                }
                e.getPlayer().openInventory(placesInv);


            }
            else if (e.getClickedBlock().getType().equals(Material.IRON_PLATE)) {


                e.setCancelled(true);
                e.getPlayer().openInventory(Invent);
            }

        }
    }

    @EventHandler
    public void BakerCraft(InventoryCloseEvent i)
    {
        if(i.getInventory().getName() == Invent.getName())
        {
            for(int g = 0 ; g < 9 ; g++)
            {
                if(Invent.contains(BakerItem[g]))
                {
                    Itm += Math.pow(2,g);
                }
            }

         //   System.out.println(Itm);

            switch (Itm)
            {
                case 21:
                    i.getPlayer().getInventory().addItem(BakerCraft[0]);
                    break;

                case 67:
                    i.getPlayer().getInventory().addItem(BakerCraft[1]);
                    break;

                case 56:
                    i.getPlayer().getInventory().addItem(BakerCraft[2]);
                    break;

                case 61:
                    i.getPlayer().getInventory().addItem(BakerCraft[3]);
                    break;

                case 197:
                    i.getPlayer().getInventory().addItem(BakerCraft[4]);
                    break;

                case 137:
                    i.getPlayer().getInventory().addItem(BakerCraft[5]);
                    break;

                case 205:
                    i.getPlayer().getInventory().addItem(BakerCraft[6]);
                    break;

                case 457:
                    i.getPlayer().getInventory().addItem(BakerCraft[7]);
                    break;

                case 483:
                    i.getPlayer().getInventory().addItem(BakerCraft[8]);
                    break;

            }
            Invent.clear();
            Itm = 0;
        }
    }

    @EventHandler
    public void Designer(PlayerInteractEvent e)
    {
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if(e.getClickedBlock().getType().equals(Material.GRAVEL) && RandColorWool &&  e.getClickedBlock().getLocation().getY() != LocationTerracottaLBlock)
            {
                RandColorWool = false;
                randBlock = (byte) Math.abs( (Math.random() * (16)));
                RandColorTerracotta = DesinerMaterial()[randBlock];

                e.getClickedBlock().setType(RandColorTerracotta.getType());
                e.getClickedBlock().setData(randBlock);
                Gravelblock = e.getClickedBlock();
            }
            else if(e.getClickedBlock().getData() == randBlock &&  e.getClickedBlock().getLocation().getY() == LocationTerracottaLBlock && !RandColorWool)
            {
                database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") + Math.abs((int) (Math.random() * (75 - 50 + 1) + 50)));
                Gravelblock.setType(Material.GRAVEL);
                RandColorWool = true;
            } else if (e.getClickedBlock().getData() != randBlock &&  e.getClickedBlock().getLocation().getY() == LocationTerracottaLBlock && !RandColorWool) {

                database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") - Math.abs((int) (Math.random() * (25 - 10 + 1) + 10)));
                Gravelblock.setType(Material.GRAVEL);
                RandColorWool = true;
            }

        }
    }

    @EventHandler
    public void ExitToLobby(PlayerInteractEvent e)
    {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.DARK_OAK_DOOR)) {
                e.setCancelled(true);
                e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), -0.4, 53.2, 1.5));

                int rand0 = Math.abs((int) (Math.random() * (10 - 5 + 1) + 5));
                int rand1 = Math.abs((int) (Math.random() * (20 - 7 + 1) + 7));
                int rand2 = Math.abs((int) (Math.random() * (30 - 15 + 1) + 15));
                for (int i = 0; i < e.getPlayer().getInventory().getSize(); i++) {
                    //e.getPlayer().sendMessage("1");

                    for(int j = 0 ; j < 3; j++) {
                        if (e.getPlayer().getOpenInventory().getItem(i).getType() == BakerCraft[j].getType()) {
                            database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") + rand0*e.getPlayer().getInventory().getItem(i).getAmount());
                            e.getPlayer().getInventory().removeItem(e.getPlayer().getOpenInventory().getItem(i));
                            e.getPlayer().updateInventory();
                        }
                    }

                    for(int j = 3 ; j < 6; j++) {
                        if (e.getPlayer().getInventory().getItem(i) == BakerCraft[j]) {
                            database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") + rand1*e.getPlayer().getInventory().getItem(i).getAmount());
                            e.getPlayer().getInventory().removeItem(e.getPlayer().getOpenInventory().getItem(i));
                            e.getPlayer().updateInventory();
                        }
                    }

                    for(int j = 6 ; j < 9; j++) {
                        if (e.getPlayer().getInventory().getItem(i) == BakerCraft[j]) {
                            database.updateArgs(e.getPlayer().getUniqueId(), "MONEY", database.getIntArgs(e.getPlayer().getUniqueId(), "MONEY") + rand2*e.getPlayer().getInventory().getItem(i).getAmount());
                            e.getPlayer().getInventory().removeItem(e.getPlayer().getOpenInventory().getItem(i));
                            e.getPlayer().updateInventory();
                        }
                    }
                }
            }
        }
    }

}