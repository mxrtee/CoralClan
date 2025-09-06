package it.mxrte.coralclan;

import it.mxrte.coralclan.commands.ClanCommand;
import it.mxrte.coralclan.database.DBManager;
import it.mxrte.coralclan.utils.LangFile;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoralClan extends JavaPlugin {


    @Override
    public void onEnable() {
        DBManager dbManager = new DBManager(this);
        LangFile langFile = new LangFile(this);
        saveDefaultConfig();
        this.getCommand("clan").setExecutor(new ClanCommand(this, dbManager));
        dbManager.setupDatabase();
        dbManager.createClanTable();
        dbManager.createClanPlayerTable();
        dbManager.createClanTable();
        dbManager.createClanHomeTable();
        dbManager.createClanClaimTable();
        langFile.createConfig();

    }



    @Override
    public void onDisable() {
        //tg
    }
}
