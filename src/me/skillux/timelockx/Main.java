package me.skillux.timelockx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {

		System.out.println(ChatColor.GOLD + "\n\nTimeLockX has been loaded!\n\n");

		Bukkit.getPluginManager().registerEvents(this, this);

		saveDefaultConfig();

	}

	public void saveDefaultConfig() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveResource("config.yml", false);
		}

	}

	List<String> day = new ArrayList<>();
	List<String> night = new ArrayList<>();
	List<String> custom = new ArrayList<>();
	List<String> enabled = new ArrayList<>();
	List<Long> current = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player player = (Player) sender;
		String world = player.getWorld().getName();
		World world1 = player.getWorld();

		if (player.hasPermission("timelockx.admin")) {

			if (args.length == 0) {

				player.sendMessage(ChatColor.GRAY + "--------------------" + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD
						+ "TimeLockX" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "--------------------");
				player.sendMessage(ChatColor.YELLOW + "Below is a list of all TimeLockX commands:");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.GOLD + "/timelock: " + ChatColor.WHITE + "Displays a list of commands.");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.GOLD + "/timelock <Day/Night/Ticks>: " + ChatColor.WHITE
						+ "Changes and locks time to either day, night, or to a custom value of ticks.");
				player.sendMessage(" ");
				player.sendMessage(
						ChatColor.GOLD + "/timelock off: " + ChatColor.WHITE + "Returns the time cycle to normal.");
				player.sendMessage(" ");
				player.sendMessage(
						ChatColor.GOLD + "/timelock reload: " + ChatColor.WHITE + "Realods the config file.");
				player.sendMessage(ChatColor.GRAY + "-----------------------------------------------------");

				return true;
			}

			try {

				String value = args[0].toString();
				long valueconverted = Long.parseLong(value);

				if (args[0].equalsIgnoreCase(value) && valueconverted >= 0 && valueconverted <= 24000) {

					if (current.contains(valueconverted)) {

						player.sendMessage(getConfig().getString("Already-Enabled").replaceAll("&", "§"));

					} else if (day.contains(world)) {

						day.remove(world);

					} else if (night.contains(world)) {

						night.remove(world);

					}

					if (enabled.contains(world)) {

						enabled.remove(world);
					}

					world1.setGameRuleValue("doDaylightCycle", "false");
					player.getWorld().setTime(valueconverted);

					if (!custom.contains(world)) {

						custom.add(world);
					}

					if (!current.contains(valueconverted)) {

						player.sendMessage(getConfig().getString("TimeLock-Set").replaceAll("&", "§")
								.replaceAll("%time%", value + " Ticks"));
						current.set(0, valueconverted);

					}

					enabled.add(world);

					return true;

				} else {

					player.sendMessage(getConfig().getString("Invalid-Ticks").replaceAll("&", "§"));

				}
			} catch (Exception e) {}
			
			
			if (args[0].equalsIgnoreCase("day")) {

				if (day.contains(world)) {

					player.sendMessage(getConfig().getString("Already-Enabled").replaceAll("&", "§"));

				} else if (night.contains(world)) {

					night.remove(world);

				} else if (custom.contains(world)) {

					custom.remove(world);

				}

				if (enabled.contains(world)) {

					enabled.remove(world);
				}

				world1.setGameRuleValue("doDaylightCycle", "false");
				player.getWorld().setTime(6000);

				if (!day.contains(world)) {

					day.add(world);
					player.sendMessage(
							getConfig().getString("TimeLock-Set").replaceAll("&", "§").replaceAll("%time%", "Day"));

				}

				enabled.add(world);

				return true;

			} else if (args[0].equalsIgnoreCase("night")) {

				if (night.contains(world)) {

					player.sendMessage(getConfig().getString("Already-Enabled").replaceAll("&", "§"));

				} else if (day.contains(world)) {

					day.remove(world);

				} else if (custom.contains(world)) {

					custom.remove(world);

				}

				if (enabled.contains(world)) {

					enabled.remove(world);
				}

				world1.setGameRuleValue("doDaylightCycle", "false");
				player.getWorld().setTime(18000);

				if (!night.contains(world)) {

					player.sendMessage(
							getConfig().getString("TimeLock-Set").replaceAll("&", "§").replaceAll("%time%", "Night"));
					night.add(world);
				}

				enabled.add(world);

				return true;

			} else if (args[0].equalsIgnoreCase("off")) {

				if (world1.getGameRuleValue("doDaylightCycle") == "false") {
					player.sendMessage(getConfig().getString("Turned-Off").replaceAll("&", "§"));
					world1.setGameRuleValue("doDaylightCycle", "true");

				} else {
					player.sendMessage(getConfig().getString("No-Selected-Mode").replaceAll("&", "§"));
				}

				if (enabled.contains(world)) {

					enabled.remove(world);

				}

				if (day.contains(world)) {
					day.remove(world);
				}

				if (night.contains(world)) {
					night.remove(world);
				}

				if (custom.contains(world)) {
					custom.remove(world);
				}
				if (current.size() > 0) {
					current.clear();
				}
				return true;

			} else if (args[0].equalsIgnoreCase("reload")) {

				reloadConfig();
				player.sendMessage(getConfig().getString("Reload-Message").replaceAll("&", "§"));

				return true;

			}
			
			
			else {
				
				try {
					
					String value = args[0].toString();
					long valueconverted = Long.parseLong(value);
					
					if (valueconverted != 0) {}
					
				} catch (Exception e) {
					player.sendMessage(getConfig().getString("Invalid-Usage").replaceAll("&", "§"));

				}
								
			}
			

		} else {

			player.sendMessage(getConfig().getString("No-Permission").replaceAll("&", "§"));

		}

		return true;
	}

}
