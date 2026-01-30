package lol.pyr.znpcsplus.interaction.consolecommand;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.config.MainConfig;
import lol.pyr.znpcsplus.interaction.InteractionActionImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class ConsoleCommandAction extends InteractionActionImpl {
    private static final Logger logger = Logger.getLogger("ConsoleCommandAction");
    
    private final TaskScheduler scheduler;
    private final String command;
    private final MainConfig config;

    public ConsoleCommandAction(TaskScheduler scheduler, String command, InteractionType interactionType, long cooldown, long delay, MainConfig config) {
        super(cooldown, delay, interactionType);
        this.scheduler = scheduler;
        this.command = command;
        this.config = config;
    }

    @Override
    public void run(Player player) {
        String cmd = command.replace("{player}", player.getName()).replace("{uuid}", player.getUniqueId().toString());

        if (config.consoleCommandWhitelistEnabled()) {
            String baseCommand = extractBaseCommand(cmd);
            if (!isCommandWhitelisted(baseCommand)) {
                logger.warning("Blocked console command '" + baseCommand + "' from NPC action - not in whitelist. Full command: " + cmd);
                return;
            }
        }
        
        scheduler.runSyncGlobal(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PapiUtil.set(player, cmd)));
    }
    
    private String extractBaseCommand(String fullCommand) {
        String trimmed = fullCommand.trim();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        
        int spaceIndex = trimmed.indexOf(' ');
        return spaceIndex == -1 ? trimmed : trimmed.substring(0, spaceIndex);
    }
    
    private boolean isCommandWhitelisted(String baseCommand) {
        return config.consoleCommandWhitelist().stream()
                .anyMatch(whitelistedCommand -> whitelistedCommand.equalsIgnoreCase(baseCommand));
    }

    @Override
    public Component getInfo(String id, int index, CommandContext context) {
        return Component.text(index + ") ", NamedTextColor.GOLD)
                .append(Component.text("[EDIT]", NamedTextColor.DARK_GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to edit this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action edit " + id + " " + index + " consolecommand " + getInteractionType().name() + " " + getCooldown()/1000 + " " + getDelay() + " " + command))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("[DELETE]", NamedTextColor.RED)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click to delete this action", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + context.getLabel() + " action delete " + id + " " + index)))
                .append(Component.text(" | ", NamedTextColor.GRAY))
                .append(Component.text("Console Command: ", NamedTextColor.GREEN)
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT,
                                Component.text("Click Type: " + getInteractionType().name() + " Cooldown: " + getCooldown()/1000 + " Delay: " + getDelay(), NamedTextColor.GRAY))))
                .append(Component.text(command, NamedTextColor.WHITE)));
    }

    public String getCommand() {
        return command;
    }
}
