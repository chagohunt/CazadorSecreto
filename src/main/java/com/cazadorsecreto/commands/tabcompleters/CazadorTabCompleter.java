package com.cazadorsecreto.commands.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CazadorTabCompleter implements TabCompleter {

    private static final String[] COMMANDS = {
            "join", "leave", "start", "stop",
            "setarea", "lobby", "score", "help",
            "forcestop", "settime", "reload"
    };

    private static final String[] TIMES = {
            "60", "120", "180", "300"
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("settime")) {
            StringUtil.copyPartialMatches(args[1], Arrays.asList(TIMES), completions);
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("lobby") || args[0].equalsIgnoreCase("area"))) {
            completions.add("pos1");
            completions.add("pos2");
        }

        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }
}