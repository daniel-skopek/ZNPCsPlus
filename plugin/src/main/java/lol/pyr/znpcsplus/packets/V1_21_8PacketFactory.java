package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.Plugin;

/**
 * PacketFactory implementation for Minecraft 1.21.8.
 * 
 * Since 1.21.8 is a hotfix release focusing on graphical and stability improvements
 * without major protocol changes, this factory inherits all functionality from
 * V1_21_3PacketFactory. Future version-specific optimizations can be added here
 * if needed.
 */
public class V1_21_8PacketFactory extends V1_21_3PacketFactory {
    public V1_21_8PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, 
                                EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer, 
                                ConfigManager configManager) {
        super(scheduler, packetEvents, propertyRegistry, textSerializer, configManager);
    }
}