/*
 * MIT License
 *
 * Copyright (c) 2020 Jakub Zagórski (jaqobb)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaqobb.messageeditor.command;

import dev.jaqobb.messageeditor.data.MessageAnalyzePlace;
import dev.jaqobb.messageeditor.MessageEditorPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class MessageEditorCommand implements CommandExecutor {

	private static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Message Editor" + ChatColor.DARK_GRAY + "] ";

	private final MessageEditorPlugin plugin;

	public MessageEditorCommand(MessageEditorPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
		if (!sender.hasPermission("messageeditor.use")) {
			sender.sendMessage(PREFIX + ChatColor.RED + "You do not have the required permissions to do that.");
			return true;
		}
		if (arguments.length == 0) {
			this.sendHelpMessage(sender, label);
			return true;
		}
		if (arguments[0].equalsIgnoreCase("activate")) {
			if (arguments.length == 1) {
				sender.sendMessage(PREFIX + ChatColor.GRAY + "Correct usage: " + ChatColor.YELLOW + "/" + label + " activate <message analyze places>" + ChatColor.GRAY + ".");
				sender.sendMessage(PREFIX);
				this.sendAvailablePlacesToAnalyze(sender);
				return true;
			}
			int activatedMessageAnalyzePlaces = 0;
			for (int index = 1; index < arguments.length; index++) {
				try {
					MessageAnalyzePlace messageAnalyzePlace = MessageAnalyzePlace.fromName(arguments[index]);
					if (!this.plugin.isMessageAnalyzePlaceActive(messageAnalyzePlace)) {
						this.plugin.activateMessageAnalyzePlace(messageAnalyzePlace);
						activatedMessageAnalyzePlaces++;
					} else {
						sender.sendMessage(PREFIX + ChatColor.GRAY + messageAnalyzePlace.name() + ChatColor.RED + " message analyze place is already active.");
					}
				} catch (IllegalArgumentException exception) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Could not convert '" + ChatColor.GRAY + arguments[index] + ChatColor.RED + "' to message analyze place.");
				}
			}
			sender.sendMessage(PREFIX + ChatColor.GRAY + "You have activated " + ChatColor.YELLOW + activatedMessageAnalyzePlaces + ChatColor.GRAY + " message analyze place(s).");
			return true;
		}
		if (arguments[0].equalsIgnoreCase("deactivate")) {
			if (arguments.length == 1) {
				sender.sendMessage(PREFIX + ChatColor.GRAY + "Correct usage: " + ChatColor.YELLOW + "/" + label + " deactivate <message analyze places>" + ChatColor.GRAY + ".");
				sender.sendMessage(PREFIX);
				this.sendAvailablePlacesToAnalyze(sender);
				return true;
			}
			int deactivatedMessageAnalyzePlaces = 0;
			for (int index = 1; index < arguments.length; index++) {
				try {
					MessageAnalyzePlace messageAnalyzePlace = MessageAnalyzePlace.fromName(arguments[index]);
					if (this.plugin.isMessageAnalyzePlaceActive(messageAnalyzePlace)) {
						this.plugin.deactivateMessageAnalyzePlace(messageAnalyzePlace);
						deactivatedMessageAnalyzePlaces++;
					} else {
						sender.sendMessage(PREFIX + ChatColor.GRAY + messageAnalyzePlace.name() + ChatColor.RED + " message analyze place is not active.");
					}
				} catch (IllegalArgumentException exception) {
					sender.sendMessage(PREFIX + ChatColor.RED + "Could not convert '" + ChatColor.GRAY + arguments[index] + ChatColor.RED + "' to message analyze place.");
				}
			}
			sender.sendMessage(PREFIX + ChatColor.GRAY + "You have deactivated " + ChatColor.YELLOW + deactivatedMessageAnalyzePlaces + ChatColor.GRAY + " message analyze place(s).");
			return true;
		}
		if (arguments[0].equalsIgnoreCase("deactivate-all")) {
			this.plugin.deactivateAllMessageAnalyzePlaces();
			sender.sendMessage(PREFIX + ChatColor.GRAY + "You have deactivated all message analyze places.");
			return true;
		}
		this.sendHelpMessage(sender, label);
		return true;
	}

	private void sendHelpMessage(CommandSender sender, String label) {
		sender.sendMessage(PREFIX + ChatColor.GRAY + "Available commands:");
		sender.sendMessage(PREFIX + ChatColor.YELLOW + "/" + label + " activate <message analyze places> " + ChatColor.GRAY + "-" + ChatColor.YELLOW + " Activates specified message analyze places.");
		sender.sendMessage(PREFIX + ChatColor.YELLOW + "/" + label + " deactivate <message analyze places> " + ChatColor.GRAY + "-" + ChatColor.YELLOW + " Deactivates specified message analyze places.");
		sender.sendMessage(PREFIX + ChatColor.YELLOW + "/" + label + " deactivate-all " + ChatColor.GRAY + "-" + ChatColor.YELLOW + " Deactivates all message analyze places.");
		sender.sendMessage(PREFIX);
		this.sendAvailablePlacesToAnalyze(sender);
	}

	private void sendAvailablePlacesToAnalyze(CommandSender sender) {
		sender.sendMessage(PREFIX + ChatColor.GRAY + "Available message analyze places:");
		for (MessageAnalyzePlace place : MessageAnalyzePlace.values()) {
			sender.sendMessage(PREFIX + ChatColor.GRAY + "- " + ChatColor.YELLOW + place.name());
		}
	}
}