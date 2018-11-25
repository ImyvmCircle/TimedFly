package com.timedfly.managers;

import com.timedfly.customevents.FlightTimeSubtractEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyTimeManager extends BukkitRunnable {

    private static Map<UUID, PlayerManager> map = new HashMap<>();

    @Override
    public void run() {
        for (Map.Entry entries : map.entrySet()) {
            UUID uuid = (UUID) entries.getKey();
            PlayerManager playerManager = (PlayerManager) entries.getValue();

            if (playerManager.getTimeLeft() > 0) {
                playerManager.setTimeLeft(playerManager.getTimeLeft() - 1);
                FlightTimeSubtractEvent event = new FlightTimeSubtractEvent(playerManager.getPlayer(), playerManager);
                Bukkit.getServer().getPluginManager().callEvent(event);
            } else {
                playerManager.stopTimedFly(true, false);
            }

        }
    }

    public static Map<UUID, PlayerManager> getMap() {
        return map;
    }
}
