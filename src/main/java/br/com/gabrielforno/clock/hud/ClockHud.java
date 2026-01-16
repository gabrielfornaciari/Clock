package br.com.gabrielforno.clock.hud;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class ClockHud extends CustomUIHud {
    private final String hour;
    private final String minute;

    public ClockHud(@NonNullDecl PlayerRef playerRef, String hour, String minute) {
        super(playerRef);
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        uiCommandBuilder.append("Hud/Clock/Clock.ui");

        uiCommandBuilder.set("#MyLabel.TextSpans", Message.raw(hour + ":" + minute));
    }

    public void updateTime(String hour, String minute) {
        UICommandBuilder builder = new UICommandBuilder();
        builder.set("#MyLabel.TextSpans",  Message.raw((hour.length() == 1 ? "0" + hour : hour)  + ":" + (minute.length() == 1 ? "0" + minute : minute)));
        update(false, builder);
    }
}
