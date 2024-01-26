package me.galtap.fudzieconomy.command;

import me.galtap.fudzieconomy.FudziEconomy;
import me.galtap.fudzieconomy.command.subcommand.SubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.BalanceInfoSubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.EconomyBalanceSubCommand;
import me.galtap.fudzieconomy.command.subcommand.impl.MoneySubCommand;
import me.galtap.fudzieconomy.config.ConfigManager;
import me.galtap.fudzieconomy.config.MessagesConfig;
import me.galtap.fudzieconomy.core.EconomyManager;
import me.galtap.fudzieconomy.utill.SimpleUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class FudziEconomyCMD extends AbstractCommand {
    private final ConfigManager configManager;
    private final EconomyManager economyManager;
    private final FudziEconomy plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    public FudziEconomyCMD(String command, FudziEconomy plugin, EconomyManager economyManager, ConfigManager configManager) {
        super(command, plugin);
        this.configManager = configManager;
        this.economyManager = economyManager;
        this.plugin = plugin;
        registerCommand(new EconomyBalanceSubCommand(economyManager,configManager));
        registerCommand(new BalanceInfoSubCommand(economyManager,configManager));
        registerCommand(new MoneySubCommand(economyManager,configManager));


    }
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        MessagesConfig messagesConfig = configManager.getMessagesConfig();
        if(args.length == 0){
            messagesConfig.getError_arguments().forEach(sender::sendMessage);
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("help")){
            if(SimpleUtil.notHasPermission(sender,"fudzieco.help",messagesConfig)) return;
            messagesConfig.getFudziecoHelpCommand().forEach(sender::sendMessage);
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("allmoney")){
            if(SimpleUtil.notHasPermission(sender,"fudzieco.allmoney",messagesConfig)) return;
            SimpleUtil.replacePlaceholders("{BALANCE}",messagesConfig.getTotal_money(),String.valueOf(economyManager.giveTotalMoney())).forEach(sender::sendMessage);
            return;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
            if(SimpleUtil.notHasPermission(sender,"fudzieco.reload",messagesConfig)) return;
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN+"[FudziEconomy] плагин успешно перезагружен");
            return;
        }
        commandProcess(messagesConfig,args,sender,subCommands);
    }
    private void registerCommand(SubCommand subCommand){
        subCommands.put(subCommand.getName(),subCommand);
    }
}
