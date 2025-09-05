package it.mxrte.coralclan.commands;

import it.mxrte.coralclan.Clan;
import it.mxrte.coralclan.database.DBManager;
import it.mxrte.coralclan.utils.ClanRole;
import it.mxrte.coralclan.utils.HexUtils;
import it.mxrte.coralclan.utils.Messages;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ClanCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Clan clanManager;
    private final BukkitAudiences audiences;


    HashMap<UUID, String> request = new HashMap<>();

    public ClanCommand(JavaPlugin plugin, DBManager dbManager) {
        this.plugin = plugin;
        clanManager = new Clan(plugin, dbManager);
        this.audiences = BukkitAudiences.create(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            return true;
        }
        Player player = (Player) commandSender;
        Messages messages = new Messages(plugin);


        if(strings.length == 0){
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9ⓘ | /ᴄʟᴀɴ"));
            player.sendMessage(" ");
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴄʀᴇᴀᴛᴇ <ɴᴏᴍᴇ> <ᴛᴀɢ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴅɪꜱʙᴀɴᴅ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ɪɴᴠɪᴛᴇ <ᴘʟᴀʏᴇʀ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴋɪᴄᴋ <ᴘʟᴀʏᴇʀ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴘʀᴏᴍᴏᴛᴇ <ᴘʟᴀʏᴇʀ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴅᴇᴍᴏᴛᴇ <ᴘʟᴀʏᴇʀ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴄʜᴀᴛ <ᴍᴇꜱꜱᴀɢɢɪᴏ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴄʟᴀɪᴍ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴜɴᴄʟᴀɪᴍ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ʜᴏᴍᴇ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ꜱᴇᴛʜᴏᴍᴇ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ɪɴꜰᴏ <ᴄʟᴀɴ>"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴀᴄᴄᴇᴘᴛ"));
            player.sendMessage(HexUtils.translateColorCodes("&#9438c9» /ᴄʟᴀɴ ᴅᴇᴄʟɪɴᴇ"));
            player.sendMessage(" ");
            return true;
        }

        if(strings[0].equalsIgnoreCase("create")){

            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_CLAN_NAME());
                return true;
            }

            if(strings.length == 2){
                player.sendMessage(messages.SPECIFY_CLAN_TAG());
                return true;
            }
            String clan = strings[1];
            String tag = strings[2];

            if(strings[2].length() > 5){
                player.sendMessage(messages.MAX_CLAN_TAG_CHAR());
                return true;
            }

            if(clanManager.getClanList().contains(clan)){
                player.sendMessage(messages.CLAN_ALREADY_EXIST());
                return true;
            }

            if(clanManager.getTagClanList().contains(tag)){
                player.sendMessage(messages.CLAN_TAG_ALREADY_EXIST());
                return true;
            }
            if(clanManager.getPlayerClan(player) != null){
                player.sendMessage(messages.U_ALREADY_HAVE_A_CLAN());
                return true;
            }

            clanManager.createClan(clan, tag);
            clanManager.setLeader(clan,player);
            player.sendMessage(messages.CLAN_CREATED(clan, tag));


        } else if (strings[0].equalsIgnoreCase("disband")) {
            if(clanManager.getPlayerClan(player) == null || !clanManager.getPlayerRole(player).equals(ClanRole.LEADER)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }
            player.sendMessage(messages.CLAN_DISBAND(clanManager.getPlayerClan(player)));
            clanManager.disbandClan(clanManager.getPlayerClan(player));

        } else if (strings[0].equalsIgnoreCase("invite")) {

            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_PLAYER_NAME());
                return true;
            }
            Player target = Bukkit.getPlayer(strings[1]);

            if(target == null){
                player.sendMessage(messages.PLAYER_NOT_FOUND());
                return true;
            }

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }

            if(clanManager.getPlayerClan(target) != null){
                player.sendMessage(messages.PLAYER_ALREADY_IN_CLAN(target));
                return true;
            }

            if(request.containsKey(target.getUniqueId())){
                player.sendMessage(messages.ALREADY_HAVE_CLAN_REQUEST(player));
                return true;
            }
            request.put(target.getUniqueId(),clan);
            TextComponent component = Component.text(messages.CLAN_REQUEST(clan))
                    .append(Component.text(" [Accetta]")
                            .color(TextColor.color(84, 176, 86))
                            .clickEvent(ClickEvent.runCommand("/clan accept"))
                            .hoverEvent(HoverEvent.showText(Component.text("Premi per accettare").color(NamedTextColor.WHITE))))
                    .append(Component.text(" [Rifiuta]")
                            .color(TextColor.color(110, 15, 15))
                            .clickEvent(ClickEvent.runCommand("/clan decline"))
                            .hoverEvent(HoverEvent.showText(Component.text("Premi per rifiutare").color(NamedTextColor.WHITE))));
            audiences.player(target).sendMessage(component);



            player.sendMessage(messages.SEND_REQUEST(target));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(request.containsKey(target.getUniqueId())) {
                        request.remove(target.getUniqueId());
                        player.sendMessage(messages.REQUEST_EXPIRED());
                        target.sendMessage(messages.REQUEST_EXPIRED());
                    }
                }
            }.runTaskLater(plugin,20 * plugin.getConfig().getLong("request-expire"));
            
        } else if (strings[0].equalsIgnoreCase("kick")) {
            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_PLAYER_NAME());
                return true;
            }

            if(!clanManager.playerExistFromName(strings[1])){
                player.sendMessage(messages.PLAYER_NOT_FOUND());
                return true;
            }

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);
            OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(clanManager.getPlayerUUID(strings[1]));

            if(offlineTarget.isOnline()){
                Player target = Bukkit.getPlayer(offlineTarget.getUniqueId());

                if(!clanManager.getPlayerClan(target).equalsIgnoreCase(clan)){
                    player.sendMessage(messages.NOT_IN_CLAN(target.getName()));
                    return true;
                }
                target.sendMessage(messages.CLAN_KICK(clan));
                clanManager.removeMember(target);
                player.sendMessage(messages.PLAYER_KICKED(offlineTarget.getName()));
                
            }else {
                if(!clanManager.getOfflinePlayerClan(offlineTarget).equalsIgnoreCase(clan)){
                    player.sendMessage(messages.NOT_IN_CLAN(offlineTarget.getName()));
                    return true;
                }
                clanManager.removeOfflineMember(offlineTarget);
                player.sendMessage(messages.PLAYER_KICKED(offlineTarget.getName()));
            }
            
        } else if (strings[0].equalsIgnoreCase("promote")) {
            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_PLAYER_NAME());
                return true;
            }
            Player target = Bukkit.getPlayer(strings[1]);

            if(target == null){
                player.sendMessage(messages.PLAYER_NOT_FOUND());
                return true;
            }

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);

            if(!clanManager.getPlayerClan(target).equalsIgnoreCase(clan)){
                player.sendMessage(messages.NOT_IN_CLAN(target.getName()));
                return true;
            }
            if(clanManager.getPlayerRole(target).equals(ClanRole.COLEADER)){
                player.sendMessage(messages.MAX_ROLE_REACHED(target));
                return true;
            }
            clanManager.promotePlayer(target);
            player.sendMessage(messages.PLAYER_PROMOTED(target,clanManager.getPlayerRole(target)));
            target.sendMessage(messages.YOU_ARE_PROMOTED(clanManager.getPlayerRole(target)));

        } else if (strings[0].equalsIgnoreCase("demote")) {

            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_PLAYER_NAME());
                return true;
            }
            Player target = Bukkit.getPlayer(strings[1]);

            if(target == null){
                player.sendMessage(messages.PLAYER_NOT_FOUND());
                return true;
            }

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);

            if(!clanManager.getPlayerClan(target).equalsIgnoreCase(clan)){
                player.sendMessage(messages.NOT_IN_CLAN(target.getName()));
                return true;
            }
            if(clanManager.getPlayerRole(target).equals(ClanRole.RECRUIT)){
                player.sendMessage(messages.MINE_ROLE_REACHED(target));
                return true;
            }
            clanManager.demotePlayer(target);
            player.sendMessage(messages.PLAYER_DEMOTED(target,clanManager.getPlayerRole(target)));
            target.sendMessage(messages.YOU_ARE_DEMOTED(clanManager.getPlayerRole(target)));

        } else if (strings[0].equalsIgnoreCase("chat")) {

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }

            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_MESSAGE());
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < strings.length; i++) {
                message.append(strings[i]).append(" ");
            }
            for (Player players : Bukkit.getOnlinePlayers()){

                if(clanManager.getAllClanPlayer(clanManager.getPlayerClan(player)).contains(players.getUniqueId())){
                    players.sendMessage(messages.CLAN_MESSAGE(player, clanManager.getPlayerRole(player), message.toString(), clanManager.getPlayerClan(player)));

                }

            }

        } else if (strings[0].equalsIgnoreCase("sethome")) {

            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }
            clanManager.setClanHome(clanManager.getPlayerClan(player), player);
            player.sendMessage(messages.CLAN_HOME_SET());

        } else if (strings[0].equalsIgnoreCase("home")) {
            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }

            if(!clanManager.haveClanHome(clanManager.getPlayerClan(player))){
                player.sendMessage(messages.NO_CLAN_HOME_SET());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);
            Location location = new Location(clanManager.getClanHomeWorld(clan), clanManager.getClanHomeX(clan), clanManager.getClanHomeY(clan),
                    clanManager.getClanHomeZ(clan),clanManager.getClanHomeYaw(clan),clanManager.getClanHomePitch(clan));
            player.teleport(location);

        } else if (strings[0].equalsIgnoreCase("accept")) {

            if(!request.containsKey(player.getUniqueId())){
                player.sendMessage(messages.NO_REQUEST());
                return true;
            }
            String clan = request.get(player.getUniqueId());
            request.remove(player.getUniqueId());
            clanManager.setRecruit(clan, player);
            player.sendMessage(messages.CLAN_JOIN(clan));
            for(UUID uuid : clanManager.getAllClanPlayer(clan)){
                Player players = Bukkit.getPlayer(uuid);
                if(players == null) continue;
                if(clanManager.isLeaderOrCoLeader(players)){
                    players.sendMessage(messages.CLAN_ACCEPTED(player));
                }
            }

        } else if (strings[0].equalsIgnoreCase("decline")) {
            if(!request.containsKey(player.getUniqueId())){
                player.sendMessage(messages.NO_REQUEST());
                return true;
            }
            String clan = request.get(player.getUniqueId());
            request.remove(player.getUniqueId());
            player.sendMessage(messages.CLAN_REFUSE(clan));
            for(UUID uuid : clanManager.getAllClanPlayer(clan)){
                Player players = Bukkit.getPlayer(uuid);
                if(clanManager.isLeaderOrCoLeader(players)){
                    players.sendMessage(messages.CLAN_DECLINED(player));
                }
            }

        } else if (strings[0].equalsIgnoreCase("info")) {

            if(strings.length == 1){
                player.sendMessage(messages.SPECIFY_CLAN_NAME());
                return true;
            }
            String clan = strings[1];
            if(!clanManager.getClanList().contains(clan)){
                player.sendMessage(messages.CLAN_NOT_FOUND(clan));
                return true;
            }

            for(String info : messages.CLAN_INFO()){
                player.sendMessage(HexUtils.translateColorCodes(info
                        .replace("%clan%", clan)
                        .replace("%leader%", clanManager.getOfflineClanLeader(clan).getName())
                        .replace("%member%", clanManager.getAllClanMemberName(clan).toString()
                                .replace("[","")
                                .replace("]",""))));

            }

        }


        return false;
    }
}
