package it.mxrte.coralclan.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
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
import org.bukkit.*;
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

    private String createRandomString(int length){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }


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
            for(String str : clanManager.getAllClanChunk(clanManager.getPlayerClan(player))){
                deleteRegion(str, player.getWorld());
            }
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
                for(String str : clanManager.getAllClanChunk(clan)){
                    removeMember(str, player.getWorld(), player.getUniqueId());
                }
                clanManager.removeMember(target);
                player.sendMessage(messages.PLAYER_KICKED(offlineTarget.getName()));
                
            }else {
                if(!clanManager.getOfflinePlayerClan(offlineTarget).equalsIgnoreCase(clan)){
                    player.sendMessage(messages.NOT_IN_CLAN(offlineTarget.getName()));
                    return true;
                }
                for(String str : clanManager.getAllClanChunk(clan)){
                    removeMember(str, player.getWorld(), player.getUniqueId());
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
            for(String str : clanManager.getAllClanChunk(clan)){
                addMember(str, player.getWorld(), player.getUniqueId());
            }
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
            for(String str : clanManager.getAllClanChunk(clan)){
                removeMember(str, player.getWorld(), player.getUniqueId());
            }
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
            for(String str : clanManager.getAllClanChunk(clan)){
                addMember(str, player.getWorld(), player.getUniqueId());
            }
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
        } else if (strings[0].equalsIgnoreCase("claim")) {
            Chunk chunk = player.getLocation().getChunk();
            if(clanManager.getPlayerClan(player) == null){
                player.sendMessage(messages.DONT_HAVE_CLAN());
                return true;
            }
            String clan = clanManager.getPlayerClan(player);

            if(!clanManager.isLeaderOrCoLeader(player)){
                player.sendMessage(messages.NOT_A_LEADER());
                return true;
            }

            if(clanManager.isClaimed(chunk)){
                player.sendMessage(messages.ALREADY_CLAIMED());
                return true;
            }
            String chunk_code = "chunk_" + createRandomString(5);
            clanManager.claimChunk(chunk_code, clan, chunk);
            createRegion(chunk_code,chunk, clan);
            player.sendMessage(messages.CHUNK_CLAIMED());

            } else if (strings[0].equalsIgnoreCase("unclaim")) {
                Chunk chunk = player.getLocation().getChunk();
                if(clanManager.getPlayerClan(player) == null){
                    player.sendMessage(messages.DONT_HAVE_CLAN());
                    return true;
                }
                String clan = clanManager.getPlayerClan(player);

                if(!clanManager.isLeaderOrCoLeader(player)){
                    player.sendMessage(messages.NOT_A_LEADER());
                    return true;
                }

                if(!clanManager.isClanChunk(clan, chunk)){
                    player.sendMessage(messages.NOT_YOUR_CLAIM());
                    return true;
            }
            deleteRegion(clanManager.getChunkID(chunk), player.getWorld());
            clanManager.unClaimChunk(clan, chunk);
            player.sendMessage(messages.CHUNK_UNCLAIMED());
        }


        return false;
    }

    private void createRegion(String chunk_code, Chunk chunk, String clan){
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(chunk.getWorld()));
        BlockVector3 vec1 = BlockVector3.at(chunk.getX() * 16, 0, chunk.getZ() * 16);
        BlockVector3 vec2 = BlockVector3.at(chunk.getX() * 16 + 15, chunk.getWorld().getMaxHeight(), chunk.getZ() * 16 + 15);
        if(!ProtectedRegion.isValidId(chunk_code)) return;
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(chunk_code, vec1, vec2);
        for(UUID uuid : clanManager.getAllRegionOwnerPlayer(clan)){
            region.getOwners().addPlayer(uuid);
        }
        for(UUID uuid : clanManager.getAllRegioneMemberPlayer(clan)){
            region.getMembers().addPlayer(uuid);
        }
        region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
        region.setFlag(Flags.BLOCK_BREAK.getRegionGroupFlag(), RegionGroup.OWNERS);
        region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.ALLOW);
        region.setFlag(Flags.BLOCK_PLACE.getRegionGroupFlag(), RegionGroup.OWNERS);
        region.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
        region.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.OWNERS);
        region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
        region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(Flags.INTERACT, StateFlag.State.ALLOW);
        region.setFlag(Flags.INTERACT.getRegionGroupFlag(), RegionGroup.MEMBERS);
        region.setFlag(Flags.USE, StateFlag.State.ALLOW);
        region.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.MEMBERS);
        manager.addRegion(region);
    }

    private void deleteRegion(String id, World world){
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        try {
            if(manager.hasRegion(id)){
                manager.removeRegion(id, RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
                manager.save();
            }else {
                Bukkit.getLogger().warning("La region non esiste");
            }
        }catch (StorageException e){
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    private void removeMember(String id, World world, UUID uuid){
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

        if(!manager.hasRegion(id)) return;
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        System.out.println("qui ci arriviamo");
        if(clanManager.getOfflinePlayerRole(player).equals(ClanRole.OFFICER) || clanManager.getOfflinePlayerRole(player).equals(ClanRole.COLEADER)){
            if(!manager.getRegion(id).getOwners().contains(uuid)) return;
            System.out.println("MA ANCHE QUI PERO");
            manager.getRegion(id).getOwners().removePlayer(uuid);

        }else if(clanManager.getOfflinePlayerRole(player).equals(ClanRole.MEMBER) || clanManager.getOfflinePlayerRole(player).equals(ClanRole.RECRUIT)){
            System.out.println("qui ci arriviamo");
            if(!manager.getRegion(id).getOwners().contains(uuid)) return;
            System.out.println("MA ANCHE QUI PERO");
            manager.getRegion(id).getMembers().removePlayer(uuid);
        }
    }

    private void addMember(String id, World world, UUID uuid){
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

        if(!manager.hasRegion(id)) return;
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if(clanManager.getOfflinePlayerRole(player).equals(ClanRole.OFFICER) || clanManager.getOfflinePlayerRole(player).equals(ClanRole.COLEADER)){
            if(manager.getRegion(id).getOwners().contains(uuid)) return;
            manager.getRegion(id).getOwners().addPlayer(uuid);

        }else if(clanManager.getOfflinePlayerRole(player).equals(ClanRole.MEMBER) || clanManager.getOfflinePlayerRole(player).equals(ClanRole.RECRUIT)){
            if(manager.getRegion(id).getOwners().contains(uuid)) return;
            manager.getRegion(id).getMembers().addPlayer(uuid);
        }
    }
}
