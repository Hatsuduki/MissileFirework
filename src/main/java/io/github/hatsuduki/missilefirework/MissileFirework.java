package io.github.hatsuduki.missilefirework;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class MissileFirework extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("§6MissileFireworkプラグインが開始しました");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
            getLogger().info("§6MissileFireworkプラグインが停止しました");
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {

        if (event.getEntityType() == EntityType.FIREWORK) {//花火がスポーンしたとき
            Entity entity = event.getEntity();

            Projectile projectile = (Projectile) entity;
            Player player = (Player) projectile.getShooter();//発射させたplayer

            Firework firework = (Firework) entity;
            FireworkMeta meta = firework.getFireworkMeta();
            meta.setPower(1);
            firework.setFireworkMeta(meta);

            new BukkitRunnable() {
                public void run() {
                    Location location = entity.getLocation();

                    //player.sendMessage("X: "+Math.floor(location.getX()*10)/10+" Y: "+Math.floor(location.getY()*10)/10+" Z: "+Math.floor(location.getZ()*10)/10); 花火の位置

                    if (firework.isShotAtAngle()) {//クロスボウから
                        Location eyeLocation = player.getEyeLocation();
                        Vector vector = location.toVector().subtract(eyeLocation.toVector()).normalize();
                        location.add(vector);
                    } else {//素手から
                        location.setY(location.getY() + 1);//壊すブロックの位置
                    }

                    if (!(location.getBlock().getType() == Material.BEDROCK)) {
                        location.getBlock().breakNaturally();//ブロックを壊す
                    }

                    if (entity.isDead()) {//花火がなくなったとき
                        entity.getWorld().createExplosion(location, 2);
                        cancel();
                    }

                }
            }.runTaskTimer(this, 0, 1);

        }

    }
}
