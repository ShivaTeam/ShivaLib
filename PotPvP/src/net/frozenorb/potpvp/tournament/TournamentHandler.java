package net.frozenorb.potpvp.tournament;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.kittype.KitType;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchState;
import net.frozenorb.potpvp.match.MatchTeam;
import net.frozenorb.potpvp.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.event.HourEvent;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.util.UUIDUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TournamentHandler implements Listener {

    @Getter @Setter private Tournament tournament = null;
    private static TournamentHandler instance;

    public TournamentHandler() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, PotPvPSI.getInstance());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PotPvPSI.getInstance(), () -> {
            if (tournament != null) tournament.check();
        }, 20L, 20L);

        ShivaLib.getInstance().getHoncho().registerCommand(new TournamentCreate());
        ShivaLib.getInstance().getHoncho().registerCommand(new TournamentJoin());
        ShivaLib.getInstance().getHoncho().registerCommand(new TournamentStatusCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new TournamentCancel());
        ShivaLib.getInstance().getHoncho().registerCommand(new TournamentForceStart());
        ShivaLib.getInstance().getHoncho().registerCommand(new Tournament.JoinMessages());
        ShivaLib.getInstance().getHoncho().registerCommand(new Tournament.EliminationMessages());

        populateTournamentStatuses();
    }

    public boolean isInTournament(Party party) {
        return tournament != null && tournament.isInTournament(party);
    }

    public boolean isInTournament(Match match) {
        return tournament != null && tournament.getMatches().contains(match);
    }

    @CommandMeta(label = { "tournament start" }, permission = "tournament.create")
    public static class TournamentCreate{
        public void execute(CommandSender sender, String kitTypeName, int teamSize, int requiredTeams){
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            tournamentCreate(sender, kitType, teamSize, requiredTeams);
        }
    }
    public static void tournamentCreate(CommandSender sender, KitType type, int teamSize, int requiredTeams) {
        if (instance.getTournament() != null) {
            sender.sendMessage(ChatColor.RED + "There's already an ongoing tournament!");
            return;
        }

        if (type == null) {
            sender.sendMessage(ChatColor.RED + "Kit type not found!");
            return;
        }

        if (teamSize < 1 || 10 < teamSize) {
            sender.sendMessage(ChatColor.RED + "Invalid team size range. Acceptable inputs: 1 -> 10");
            return;
        }

        if (requiredTeams < 4) {
            sender.sendMessage(ChatColor.RED + "Required teams must be at least 4.");
            return;
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7A &c&ltournament&7 has started. Type &5/join&7 to play. (0/" + (teamSize < 3 ? teamSize * requiredTeams : requiredTeams) + ")"));
        Bukkit.broadcastMessage("");

        Tournament tournament;
        instance.setTournament(tournament = new Tournament(type, teamSize, requiredTeams));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.getTournament() == tournament) {
                    tournament.broadcastJoinMessage();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(PotPvPSI.getInstance(), 60 * 20, 60 * 20);
    }

    @CommandMeta(label = { "tournament join", "join", "jointournament" }, permission = "")
    public static class TournamentJoin{
        public void execute(Player sender){
            tournamentJoin(sender);
        }
    }
    public static void tournamentJoin(Player sender) {

        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to join.");
            return;
        }

        int tournamentTeamSize = instance.getTournament().getRequiredPartySize();

        if ((instance.getTournament().getCurrentRound() != -1 || instance.getTournament().getBeginNextRoundIn() != 31) && (instance.getTournament().getCurrentRound() != 0 || !sender.hasPermission("tournaments.joinduringcountdown"))) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }

        Party senderParty = PotPvPSI.getInstance().getPartyHandler().getParty(sender);
        if (senderParty == null) {
            if (tournamentTeamSize == 1) {
                senderParty = PotPvPSI.getInstance().getPartyHandler().getOrCreateParty(sender); // Will auto put them in a party
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have a team to join the tournament with!");
                return;
            }
        }

        int notInLobby = 0;
        int queued = 0;
        for (UUID member : senderParty.getMembers()) {
            if (!PotPvPSI.getInstance().getLobbyHandler().isInLobby(Bukkit.getPlayer(member))) {
                notInLobby++;
            }

            if (PotPvPSI.getInstance().getQueueHandler().getQueueEntry(member) != null) {
                queued++;
            }
        }

        if (notInLobby != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team aren't in the lobby.");
            return;
        }

        if (queued != 0) {
            sender.sendMessage(ChatColor.RED.toString() + notInLobby + "member" + (notInLobby == 1 ? "" : "s") + " of your team are currently queued.");
            return;
        }

        if (!senderParty.getLeader().equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "You must be the leader of your team to join the tournament.");
            return;
        }

        if (instance.isInTournament(senderParty)) {
            sender.sendMessage(ChatColor.RED + "Your team is already in the tournament!");
            return;
        }

        if (senderParty.getMembers().size() != instance.getTournament().getRequiredPartySize()) {
            sender.sendMessage(ChatColor.RED + "You need exactly " + instance.getTournament().getRequiredPartySize() + " members in your party to join the tournament.");
            return;
        }

        if (PotPvPSI.getInstance().getQueueHandler().getQueueEntry(senderParty) != null) {
            sender.sendMessage(ChatColor.RED + "You can't join the tournament if your party is currently queued.");
            return;
        }

        senderParty.message(ChatColor.GREEN + "Joined the tournament.");
        instance.getTournament().addParty(senderParty);
    }

    @CommandMeta(label = { "tournament status", "tstatus", "status" }, permission = "")
    public static class TournamentStatusCommand{
        public void execute(CommandSender sender){
            tournamentStatus(sender);
        }
    }
    public static void tournamentStatus(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no ongoing tournament to get the status of.");
            return;
        }

        sender.sendMessage(PotPvPLang.LONG_LINE);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Live &cTournament &7Fights"));
        sender.sendMessage("");
        List<Match> ongoingMatches = instance.getTournament().getMatches().stream().filter(m -> m.getState() != MatchState.TERMINATED).collect(Collectors.toList());

        for (Match match : ongoingMatches) {
            MatchTeam firstTeam = match.getTeams().get(0);
            MatchTeam secondTeam = match.getTeams().get(1);

            if (firstTeam.getAllMembers().size() == 1) {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(firstTeam.getFirstMember()) + ChatColor.GRAY + " vs " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(secondTeam.getFirstMember()));
            } else {
                sender.sendMessage("  " + ChatColor.GRAY + "» " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(firstTeam.getFirstMember()) + ChatColor.GRAY + "'s team vs " + ChatColor.LIGHT_PURPLE + UUIDUtils.name(secondTeam.getFirstMember()) + ChatColor.GRAY + "'s team");
            }
        }
        sender.sendMessage(PotPvPLang.LONG_LINE);
    }

    @CommandMeta(label = { "tournament cancel", "tcancel"},  permission = "op")
    public static class TournamentCancel{
        public void execute(CommandSender sender){
            tournamentCancel(sender);
        }
    }
    public static void tournamentCancel(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no running tournament to cancel.");
            return;
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&7The &c&ltournament&7 was &ccancelled."));
        Bukkit.broadcastMessage("");
        instance.setTournament(null);
    }

    @CommandMeta(label = { "tournament forcestart"}, permission = "op")
    public static class TournamentForceStart{
        public void execute(CommandSender sender){
            tournamentForceStart(sender);
        }
    }
    public static void tournamentForceStart(CommandSender sender) {
        if (instance.getTournament() == null) {
            sender.sendMessage(ChatColor.RED + "There is no tournament to force start.");
            return;
        }

        if (instance.getTournament().getCurrentRound() != -1 || instance.getTournament().getBeginNextRoundIn() != 31) {
            sender.sendMessage(ChatColor.RED + "This tournament is already in progress.");
            return;
        }

        instance.getTournament().start();
        sender.sendMessage(ChatColor.GREEN + "Force started tournament.");
    }


    private static List<TournamentStatus> allStatuses = Lists.newArrayList();

    private void populateTournamentStatuses() {
        List<KitType> viewableKits = KitType.getAllTypes().stream().filter(kit -> !kit.isHidden()).collect(Collectors.toList());
        allStatuses.add(new TournamentStatus(0, ImmutableList.of(1), ImmutableList.of(16, 32), viewableKits));
        allStatuses.add(new TournamentStatus(250, ImmutableList.of(1), ImmutableList.of(32), viewableKits));
        if(KitType.byId("NODEBUFF") != null){
            allStatuses.add(new TournamentStatus(300, ImmutableList.of(1), ImmutableList.of(48, 64), ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(400, ImmutableList.of(1), ImmutableList.of(64), ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(500, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(600, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(700, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
            allStatuses.add(new TournamentStatus(800, ImmutableList.of(1), ImmutableList.of(128), ImmutableList.of(KitType.byId("NODEBUFF"))));
        }
    }

    @EventHandler
    public void onHour(HourEvent event) {
        if (instance.getTournament() != null) return; // already a tournament in progress

        TournamentStatus status = TournamentStatus.forPlayerCount(Bukkit.getOnlinePlayers().size());

        int teamSize = status.getTeamSizes().get(ShivaLib.RANDOM.nextInt(status.getTeamSizes().size()));
        int teamCount = status.getTeamCounts().get(ShivaLib.RANDOM.nextInt(status.getTeamCounts().size()));
        KitType kitType = status.getKitTypes().get(ShivaLib.RANDOM.nextInt(status.getKitTypes().size()));

        Tournament tournament;
        instance.setTournament(tournament = new Tournament(kitType, teamSize, teamCount));

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.getTournament() == tournament && tournament.getCurrentRound() == -1) {
                    tournament.broadcastJoinMessage();
                } else {
                    cancel();
                }
            }
        };
        bukkitRunnable.runTaskTimer(PotPvPSI.getInstance(), 0, 60 * 20);
        Bukkit.getScheduler().runTaskLater(PotPvPSI.getInstance(), () -> {
            if (tournament == instance.getTournament() && instance.getTournament() != null) {
                if(instance.getTournament().getCurrentRound() == -1){
                    if(tournament.getActiveParties().size() > 1 ){
                        instance.getTournament().start();
                    }else{
                        tournamentCancel(PotPvPSI.getInstance().getServer().getConsoleSender());
                    }
                }
            }
            bukkitRunnable.cancel();
        }, 20 * 3 * 60);
    }

    @Getter
    private static class TournamentStatus {
        private int minimumPlayerCount;
        private List<Integer> teamSizes;
        private List<Integer> teamCounts;
        private List<KitType> kitTypes;

        public TournamentStatus(int minimumPlayerCount, List<Integer> teamSizes, List<Integer> teamCounts, List<KitType> kitTypes) {
            this.minimumPlayerCount = minimumPlayerCount;
            this.teamSizes = teamSizes;
            this.teamCounts = teamCounts;
            this.kitTypes = kitTypes;
        }

        public static TournamentStatus forPlayerCount(int playerCount) {
            for (int i = allStatuses.size() - 1; 0 <= i; i--) {
                if (allStatuses.get(i).minimumPlayerCount <= playerCount) return allStatuses.get(i);
            }


            throw new IllegalArgumentException("No suitable sizes found!");
        }
    }
}