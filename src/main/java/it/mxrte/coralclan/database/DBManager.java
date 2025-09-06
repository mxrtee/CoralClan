package it.mxrte.coralclan.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.mxrte.coralclan.CoralClan;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {

    private final JavaPlugin plugin;
    private final String host;
    private final String database;
    private final int port;
    private final String username;
    private final String password;
    HikariDataSource dataSource;

    public DBManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.host = plugin.getConfig().getString("Database.host");
        this.database = plugin.getConfig().getString("Database.database");
        this.username = plugin.getConfig().getString("Database.username");
        this.password = plugin.getConfig().getString("Database.password");
        this.port = plugin.getConfig().getInt("Database.port");

    }





    public void setupDatabase(){
            HikariConfig hikariConfig = new HikariConfig();
            FileConfiguration config = plugin.getConfig();
            hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
            hikariConfig.setJdbcUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.addDataSourceProperty("cachePrepStmts", config.getBoolean("Database.cachePrepStmts"));
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", config.getInt("Database.prepStmtCacheSize"));
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", config.getInt("Database.prepStmtCacheSqlLimit"));
            hikariConfig.setMaximumPoolSize(config.getInt("Database.maximumPoolSize"));
            hikariConfig.setMinimumIdle(config.getInt("Database.minimumIdle"));
            hikariConfig.setConnectionTimeout(config.getLong("Database.connectionTimeout"));
            hikariConfig.setLeakDetectionThreshold(config.getLong("Database.leakDetectionThreshold"));
            this.dataSource = new HikariDataSource(hikariConfig);
    }

    public void createClanTable(){
        String query = "CREATE TABLE IF NOT EXISTS clan_list (name VARCHAR(255) PRIMARY KEY, tag VARCHAR(5))";

        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();

        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void createClanPlayerTable(){
        String query = "CREATE TABLE IF NOT EXISTS clan_player(uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(255), clan VARCHAR(255), role VARCHAR(255))";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void createClanHomeTable(){
        String query = "CREATE TABLE IF NOT EXISTS clan_home(name VARCHAR(255) PRIMARY KEY, x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT, world VARCHAR(255))";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void createClanClaimTable(){
        String query = "CREATE TABLE IF NOT EXISTS clan_claim(claim_id VARCHAR(5) PRIMARY KEY, name VARCHAR(255), x DOUBLE, z DOUBLE, world VARCHAR(255))";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        }catch (SQLException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public HikariDataSource getDataSource(){
        return dataSource;
    }
}
