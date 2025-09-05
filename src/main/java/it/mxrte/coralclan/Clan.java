package it.mxrte.coralclan;

import it.mxrte.coralclan.database.DBManager;
import it.mxrte.coralclan.utils.ClanRole;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clan {

    private final JavaPlugin plugin;
    private final DBManager dbmanager;

    public Clan(JavaPlugin plugin, DBManager dbManager) {

        this.plugin = plugin;
        this.dbmanager = dbManager;

    }

    public void createClan(String name, String tag){

        String query = "INSERT INTO clan_list(name, tag) VALUES (?,?)";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2,tag);
            statement.executeUpdate();

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void disbandClan(String name){
        String query = "DELETE FROM clan_list WHERE name = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            getAllClanPlayer(name).clear();
            removeAllClanMember(name);
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,name);
            statement.executeUpdate();

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void setLeader(String name, Player player){
        String query = "INSERT INTO clan_player(uuid, name, clan, role) VALUES (?,?,?,?)";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, name);
            statement.setString(4, ClanRole.LEADER.name());
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void setRecruit(String name, Player player){
        String query = "INSERT INTO clan_player(uuid, name, clan, role) VALUES (?,?,?,?)";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, name);
            statement.setString(4, ClanRole.RECRUIT.name());
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public ClanRole getPlayerRole(Player player){
        String query = "SELECT role FROM clan_player WHERE uuid = ?";
        if(player == null) return null;
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());

            try(ResultSet set = statement.executeQuery()) {

                if(set.next()){
                    return ClanRole.valueOf(set.getString("role"));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }


        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
        return null;
    }

    public ClanRole getOfflinePlayerRole(OfflinePlayer player){
        String query = "SELECT role FROM clan_player WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());

            try(ResultSet set = statement.executeQuery()) {

                if(set.next()){
                    return ClanRole.valueOf(set.getString("role"));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }


        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
        return null;
    }

    public void updatePlayerRole(Player player, ClanRole role){
        String query = "UPDATE clan_player SET role = ? WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, role.name());
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void updateOfflinePlayerRole(OfflinePlayer player, ClanRole role){
        String query = "UPDATE clan_player SET role = ? WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, role.name());
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void promotePlayer(Player player){
        ClanRole role = getPlayerRole(player);
        switch (role){
            case RECRUIT:
                updatePlayerRole(player, ClanRole.MEMBER);
                break;
            case MEMBER:
                updatePlayerRole(player, ClanRole.OFFICER);
                break;
            case OFFICER:
                updatePlayerRole(player, ClanRole.COLEADER);
                break;
            default:
                break;
        }
    }

    public void demotePlayer(Player player){
        ClanRole role = getPlayerRole(player);
        switch (role){
            case COLEADER:
                updatePlayerRole(player, ClanRole.OFFICER);
                break;
            case OFFICER:
                updatePlayerRole(player, ClanRole.MEMBER);
                break;
            case MEMBER:
                updatePlayerRole(player, ClanRole.RECRUIT);
                break;
            default:
                break;
        }
    }

    public List<UUID> getAllClanPlayer(String name){
        String query = "SELECT uuid FROM clan_player WHERE clan = ?";
        List<UUID> allPlayer = new ArrayList<>();
        try(Connection connection = dbmanager.getDataSource().getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            try(ResultSet set = statement.executeQuery()) {

                while (set.next()){
                    String uuidStr = set.getString("uuid");
                    allPlayer.add(UUID.fromString(uuidStr));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
        return allPlayer;
    }

    public void removeAllClanMember(String name){
        String query = "DELETE FROM clan_player WHERE clan = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public List<UUID> getAllClanPlayerWithRole(ClanRole role, String name){
        List<UUID> uuids = new ArrayList<>();
        for(UUID uuid : getAllClanPlayer(name)){
            if(getOfflinePlayerRole(Bukkit.getOfflinePlayer(uuid)).equals(role)){
                uuids.add(uuid);
            }
        }
        return uuids;
    }

    public void removeMember(Player player){
        String query = "DELETE FROM clan_player WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void removeOfflineMember(OfflinePlayer player){
        String query = "DELETE FROM clan_player WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public List<String> getClanList(){
        List<String> clans = new ArrayList<>();
        String query = "SELECT name FROM clan_list";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            try(ResultSet set = statement.executeQuery()) {

                while (set.next()){
                    clans.add(set.getString("name"));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return clans;
    }

    public List<String> getTagClanList(){
        List<String> clans = new ArrayList<>();
        String query = "SELECT tag FROM clan_list";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);

            try(ResultSet set = statement.executeQuery()) {

                while (set.next()){
                    clans.add(set.getString("tag"));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return clans;
    }

    public String getPlayerClan(Player player){
        String query = "SELECT clan FROM clan_player WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            try(ResultSet set = statement.executeQuery()) {

                if(set.next()){
                    return set.getString("clan");
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return null;
    }

    public String getOfflinePlayerClan(OfflinePlayer player){
        String query = "SELECT clan FROM clan_player WHERE uuid = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            try(ResultSet set = statement.executeQuery()) {

                if(set.next()){
                    return set.getString("clan");
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return null;
    }

    public boolean playerExistFromName(String name){

        String query = "SELECT 1 FROM clan_player WHERE name = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                return set.next();
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return false;
    }

    public UUID getPlayerUUID(String name){
        String query = "SELECT uuid FROM clan_player WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            try(ResultSet set = statement.executeQuery()) {

                if(set.next()){
                    return UUID.fromString(set.getString("uuid"));
                }

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }


        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return null;
    }

    public boolean isLeaderOrCoLeader(Player player){
        if(getPlayerRole(player) == null) return false;
        return getPlayerRole(player).equals(ClanRole.LEADER) || getPlayerRole(player).equals(ClanRole.COLEADER);
    }

    public OfflinePlayer getOfflineClanLeader(String name){
        for(UUID uuid : getAllClanPlayer(name)){
            if(getOfflinePlayerRole(Bukkit.getOfflinePlayer(uuid)).equals(ClanRole.LEADER)){
                return Bukkit.getOfflinePlayer(uuid);
            }
        }

        return null;
    }

    public void setClanHome(String name, Player player){
        String query = "INSERT INTO clan_home(name, x, y, z, yaw, pitch, world) VALUES (?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE x = ?, y = ?, z = ?, yaw = ?, pitch = ?, world = ?";
        Location location = player.getLocation();
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2, location.getX());
            statement.setDouble(3, location.getY());
            statement.setDouble(4, location.getZ());
            statement.setFloat(5, location.getYaw());
            statement.setFloat(6, location.getPitch());
            statement.setString(7,location.getWorld().getName());
            statement.setDouble(8, location.getX());
            statement.setDouble(9, location.getY());
            statement.setDouble(10, location.getZ());
            statement.setFloat(11, location.getYaw());
            statement.setFloat(12, location.getPitch());
            statement.setString(13,location.getWorld().getName());
            statement.executeUpdate();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public boolean haveClanHome(String name){
        String query = "SELECT 1 FROM clan_home WHERE name = ?";
        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {

                return set.next();

            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return false;
    }

    public double getClanHomeX(String name){
        String query = "SELECT x FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return set.getDouble("x");
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return 0;
    }

    public double getClanHomeY(String name){
        String query = "SELECT y FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return set.getDouble("y");
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return 0;
    }

    public double getClanHomeZ(String name){
        String query = "SELECT z FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return set.getDouble("z");
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return 0;
    }

    public float getClanHomeYaw(String name){
        String query = "SELECT yaw FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return set.getFloat("yaw");
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return 0;
    }

    public float getClanHomePitch(String name){
        String query = "SELECT pitch FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return set.getFloat("pitch");
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return 0;
    }

    public World getClanHomeWorld(String name){
        String query = "SELECT world FROM clan_home WHERE name = ?";

        try(Connection connection = dbmanager.getDataSource().getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);

            try(ResultSet set = statement.executeQuery()) {
                if(set.next()){
                    return Bukkit.getWorld(set.getString("world"));
                }
            }catch (SQLException e){
                Bukkit.getLogger().warning(e.getMessage());
            }

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }

        return null;
    }

    public List<String> getAllClanMemberName(String name){
        List<String> clanMember = new ArrayList<>();
        for (UUID uuid : getAllClanPlayer(name)){
            clanMember.add(Bukkit.getOfflinePlayer(uuid).getName());
        }

        return clanMember;
    }

}
