package it.mxrte.coralclan.utils;

import org.apache.commons.codec.binary.Hex;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Messages {
    
    private final JavaPlugin plugin;
    private final LangFile langFile;

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
        this.langFile = new LangFile(plugin);
        
    }

    public String SPECIFY_CLAN_NAME(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("specify-clan-name"));
    }

    public String SPECIFY_CLAN_TAG(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("specify-clan-tag"));
    }

    public String CLAN_ALREADY_EXIST(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-already-exist"));
    }

    public String U_ALREADY_HAVE_A_CLAN(){
    return HexUtils.translateColorCodes(langFile.getConfig().getString("you-already-have-clan"));
    }

    public String CLAN_TAG_ALREADY_EXIST(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-tag-already-exist"));
    }

    public String MAX_CLAN_TAG_CHAR(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("max-clan-tag-char"));
    }

    public String CLAN_CREATED(String name, String tag){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-created")
                .replace("%clan%", name)
                .replace("%tag%", tag));
    }

    public String NOT_A_LEADER(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("not-a-leader"));
    }

    public String CLAN_DISBAND(String name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-disband").replace("%clan%", name));
    }

    public String SPECIFY_PLAYER_NAME(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("specify-player-name"));
    }

    public String PLAYER_NOT_FOUND(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("player-not-found"));
    }

    public String DONT_HAVE_CLAN(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("dont-have-clan"));
    }

    public String PLAYER_ALREADY_IN_CLAN(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("player-already-in-clan")
                .replace("%player_name%", player.getName()));
    }

    public String ALREADY_HAVE_CLAN_REQUEST(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("already-have-clan-request")
                .replace("%player_name%", player.getName()));
    }

    public String CLAN_REQUEST(String clan){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-request")
                .replace("%clan%", clan));
    }

    public String SEND_REQUEST(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("request-sent")
                .replace("%player_name%", player.getName()));
    }

    public String NOT_IN_CLAN(String player_name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("not-in-clan")
                .replace("%player_name%", player_name));
    }

    public String CLAN_KICK(String name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-kick")
                .replace("%clan%", name));
    }

    public String PLAYER_KICKED(String player_name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("player-kicked")
                .replace("%player_name%",player_name));
    }

    public String MAX_ROLE_REACHED(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("max-role-reached")
                .replace("%player_name%", player.getName()));
    }

    public String MINE_ROLE_REACHED(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("mine-role-reached")
                .replace("%player_name%", player.getName()));
    }

    public String PLAYER_PROMOTED(Player player, ClanRole role){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("player-promoted")
                .replace("%player_name%", player.getName())
                .replace("%player_role%", role.name()));
    }

    public String PLAYER_DEMOTED(Player player, ClanRole role){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("player-demoted")
                .replace("%player_name%", player.getName())
                .replace("%player_role%", role.name()));
    }

    public String YOU_ARE_PROMOTED(ClanRole role){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("you-are-promoted")
                .replace("%player_role%", role.name()));
    }

    public String YOU_ARE_DEMOTED(ClanRole role){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("you-are-demoted")
                .replace("%player_role%", role.name()));
    }

    public String SPECIFY_MESSAGE(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("specify-message"));
    }

    public String CLAN_MESSAGE(Player player, ClanRole role, String message, String clan){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-message")
                .replace("%player_name%", player.getName())
                .replace("%player_role%", role.name())
                .replace("%message%", message)
                .replace("%clan%", clan));
    }

    public String CLAN_HOME_SET(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-home-set"));
    }

    public String NO_CLAN_HOME_SET(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("no-clan-home-set"));
    }

    public String NO_REQUEST(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("no-request"));
    }

    public String CLAN_JOIN(String name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-join")
                .replace("%clan%", name));
    }

    public String CLAN_ACCEPTED(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-accepted")
                .replace("%player_name%", player.getName()));
    }

    public String CLAN_DECLINED(Player player){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-declined")
                .replace("%player_name%", player.getName()));
    }

    public String CLAN_REFUSE(String name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-refuse")
                .replace("%clan%", name));
    }

    public String CLAN_NOT_FOUND(String name){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("clan-not-found")
                .replace("%clan%", name));
    }

    public String REQUEST_EXPIRED(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("request-expired"));
    }

    public List<String> CLAN_INFO(){
        return langFile.getConfig().getStringList("clan-info");
    }

    public String ALREADY_CLAIMED(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("already-claimed"));
    }

    public String CHUNK_CLAIMED(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("chunk-claimed"));
    }

    public String CHUNK_UNCLAIMED(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("chunk-unclaimed"));
    }

    public String NOT_YOUR_CLAIM(){
        return HexUtils.translateColorCodes(langFile.getConfig().getString("not-your-claim"));
    }


}
