package br.com.gabrielforno.clock.system;

import br.com.gabrielforno.clock.hud.ClockHud;
import com.hypixel.hytale.common.util.FormatUtil;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.time.WorldTimeResource;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class PlayerTickSystem extends EntityTickingSystem<EntityStore> {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    @Nonnull
    private final Query<EntityStore> query;

    int lastMinute = 0;
    int lastHour = 0;
    ClockHud clockHud = null;

    public PlayerTickSystem() {
        this.query = Query.and(Player.getComponentType());
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        WorldTimeResource worldTimeResource = store.getResource(WorldTimeResource.getResourceType());
        LocalDateTime gameDateTime = worldTimeResource.getGameDateTime();

        int hour = gameDateTime.get(ChronoField.CLOCK_HOUR_OF_DAY);
        int minute = gameDateTime.getMinute();

        final Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        final PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        final Player player = holder.getComponent(Player.getComponentType());

        ClockHud clockHudPopulate = hubFabric(playerRef, hour, minute);
        player.getHudManager().setCustomHud(playerRef, clockHudPopulate);

        if (hour != lastHour || minute != lastMinute){
            lastHour = hour;
            lastMinute = minute;
            clockHud.updateTime(String.valueOf(hour), String.valueOf(minute));
        }

    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }

    private ClockHud hubFabric(PlayerRef playerRef, int hour, int minute){
        if (clockHud == null){
            LOGGER.atInfo().log("Criou o clockHud");
            clockHud = new ClockHud(playerRef, String.valueOf(hour), String.valueOf(minute));
        }

        return clockHud;
    }

}
