package beastreamer.beastreamer.database;

import beastreamer.beastreamer.BeAStreamer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Database implements Listener {
    BeAStreamer plugin = BeAStreamer.getPlugin(BeAStreamer.class);

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        createPlayer(p.getUniqueId(), p);
    }

    @EventHandler
    public void hit(PlayerInteractEvent e) {
//        double fol = getIntArgs(e.getPlayer().getUniqueId(), "FOLLOWERS");
//        updateArgs(e.getPlayer().getUniqueId(), "FOLLOWERS", fol + 20.3);
//        e.getPlayer().sendMessage(String.valueOf(getIntArgs(e.getPlayer().getUniqueId(), "FOLLOWERS")));
//
//        updateArgs(e.getPlayer().getUniqueId(), "BOOST_FOLL", 1.7);
//        e.getPlayer().sendMessage(String.valueOf(getIntArgs(e.getPlayer().getUniqueId(), "BOOST_FOLL")));
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + "LOAD DB by R1DD1");
                return true;
            }
            plugin.getServer().broadcastMessage(ChatColor.RED + "DB: Player NOT Found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(final UUID uuid, Player player) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            System.out.print(1);
            if (playerExists(uuid) != true) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.table
                                + " (UUID,NAME,FOLLOWERS,MONEY,TOKENS,BOOST_FOLL,BOOST_MONEY, MAX_ENERGY,CUR_ENERGY, ACT_VIDEOCARD, ACT_CORE, ACT_MB, EDUCATION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setDouble(3, 0);
                insert.setDouble(4,100000);
                insert.setDouble(5, 0);
                insert.setDouble(6,1);
                insert.setDouble(7,1);
                insert.setDouble(8, 50);
                insert.setDouble(9, 50);
                insert.setString(10, "Не выбрана");
                insert.setString(11, "Не выбрана");
                insert.setString(12, "Не выбрана");
                insert.setDouble(13, 0);

                insert.executeUpdate();

                plugin.getServer().broadcastMessage(ChatColor.GREEN + "DB: Loaded Successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // STRING arg == WHAT WE NEED UPDATE

    public void updateArgs(UUID uuid, String arg, double followers) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("UPDATE " + plugin.table + " SET " + arg + "=? WHERE UUID=?");
            statement.setDouble(1, followers);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStringArgs(UUID uuid, String arg, String followers) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("UPDATE " + plugin.table + " SET " + arg + "=? WHERE UUID=?");
            statement.setString(1, followers);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // STRING arg == WHAT WE NEED GET

    public double getIntArgs(UUID uuid, String arg) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();

            double argValue = (results.getDouble(arg));

            return argValue;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getStringArgs(UUID uuid, String arg) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.table + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();

            String argValue = (results.getString(arg));

            return argValue;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
