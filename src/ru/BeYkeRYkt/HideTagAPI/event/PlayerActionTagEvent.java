package ru.BeYkeRYkt.HideTagAPI.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerActionTagEvent extends PlayerEvent implements Cancellable{
    
    public enum ActionType{
        SHOW, HIDE;
    }
    
    private boolean cancel;
    private static final HandlerList handlers = new HandlerList();
    private ActionType actionType;

    public PlayerActionTagEvent(Player player, ActionType action) {
        super(player);
        this.actionType = action;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean arg) {
        this.cancel = arg;
    }

    public ActionType getActionType() {
        return actionType;
    }
}