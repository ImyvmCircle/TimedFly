package com.timedfly.listener;

import com.timedfly.TimedFly;
import com.timedfly.configurations.ConfigCache;
import com.timedfly.managers.FlyTimeManager;
import com.timedfly.managers.MySQLManager;
import com.timedfly.managers.PlayerManager;
import com.timedfly.updater.Updater;
import com.timedfly.utilities.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {
    private Utilities utilities;
    private MySQLManager sqlManager;
    private TimedFly plugin;
    private Updater updater;

    public JoinLeave(Utilities utilities, MySQLManager sqlManager, TimedFly plugin, Updater updater) {
        this.utilities = utilities;
        this.sqlManager = sqlManager;
        this.plugin = plugin;
        this.updater = updater;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("timedfly.getupdate")) this.updater.sendUpdateMessage(player);

        try {
            this.sqlManager.createPlayer(player);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Please report this!");
        }

        this.utilities.addPlayerManager(player.getUniqueId(), player, this.plugin);

        if (this.utilities.isWorldEnabled(player.getWorld())) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                PlayerManager playerManager = this.utilities.getPlayerManager(player.getUniqueId());
                if (FlyTimeManager.getMap().containsKey(player.getUniqueId())) {
                    playerManager = FlyTimeManager.getMap().get(player.getUniqueId());
                    utilities.setPlayerManager(player.getUniqueId(), playerManager);
                } else playerManager.setInServer(true).setInitialTime(this.sqlManager.getInitialTime(player))
                        .setTimeLeft(this.sqlManager.getTimeLeft(player))
                        .setTimeManuallyPaused(this.sqlManager.getManuallyStopped(player));

                System.out.println(playerManager.getTimeLeft() + " " + playerManager.getInitialTime() + " " + playerManager.isTimeManuallyPaused());
                if (playerManager.getTimeLeft() >= 1) {
                    if (!playerManager.isTimePaused() && !playerManager.isTimeManuallyPaused())
                        playerManager.startTimedFly();

                    if (ConfigCache.isJoinFlyingEnabled()) {
                        player.teleport(player.getLocation().add(0.0D, (double) ConfigCache.getJoinFlyingHeight(), 0.0D));
                    }

                }
            }, 20L);
        } else System.out.println("not enabled");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = this.utilities.getPlayerManager(player.getUniqueId());
        playerManager.setInServer(false);
        if (ConfigCache.isStopTimerOnLeave()) playerManager.stopTimedFly();

        System.out.println(playerManager.getTimeLeft() + " " + playerManager.getInitialTime() + " " + playerManager.isTimeManuallyPaused());
        sqlManager.saveData(player, playerManager.getTimeLeft(), playerManager.getInitialTime(), playerManager.isTimeManuallyPaused());
    }
}
