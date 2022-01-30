package ml.windleaf.wlkitsreforged.modules

import ml.windleaf.wlkitsreforged.core.*
import ml.windleaf.wlkitsreforged.core.annotations.CommandInfo
import ml.windleaf.wlkitsreforged.core.annotations.ModuleInfo
import ml.windleaf.wlkitsreforged.core.annotations.Permission
import ml.windleaf.wlkitsreforged.core.enums.LoadType
import ml.windleaf.wlkitsreforged.core.module.Module
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleCommand
import ml.windleaf.wlkitsreforged.core.module.ModuleManager
import ml.windleaf.wlkitsreforged.core.module.commanding.ModuleTabCompleter
import ml.windleaf.wlkitsreforged.utils.Util
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@CommandInfo(cmd = "wlkits", description = "The base commands of WLKits plugin", belongTo = WLKitsCommands::class)
@ModuleInfo(description = "The base commands of WLKits plugin", type = LoadType.ON_STARTUP)
class WLKitsCommands : Module, ModuleCommand, ModuleTabCompleter {
    private var enabled = false
    override fun getEnabled() = enabled

    companion object {
        fun getInfo(): ArrayList<String> {
            return arrayListOf(
                "&a${WLKits.name} &fv${WLKits.version} &arunning on bukkit &c${Bukkit.getBukkitVersion()} &6[using Reflector: ${WLKits.reflector.getNMS()}]",
                "&agithub: &ehttps://github.com/WindLeaf233/WLKits-Reforged",
                "&amodules: &b(${ModuleManager.moduleInstances.size}) [${
                    ModuleManager.moduleInstances.stream().map {
                        if (ModuleManager.errorModules.contains(it)) "&l&d${it.getName()}&r" else (if (it.getEnabled()) "&a${it.getName()}&r" else "&c${it.getName()}&r")
                    }.collect(Collectors.joining("&b,&r "))
                }]",
                "&adebugMode: ${Util.parseBooleanColor(WLKits.debug)}")
        }
    }

    override fun setEnabled(target: Boolean) {
        enabled = target
    }

    override fun load() {
        enabled = Util.isEnabled(getName())
    }

    @Permission("wlkits.cmd.wlkits")
    override fun onCommand(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) Util.invalidArgs(sender) else {
            when (args[0]) {
                "help" -> Util.sendHelp(sender,
                    "/wlkits help" to "查看此帮助",
                    "/wlkits reload" to "重载插件",
                    "/wlkits status [moduleName]" to "查看模块开启状态",
                    "/wlkits info" to "查看插件信息")
                "reload" -> {
                    ModuleManager.reload()
                    Util.send(sender, Util.getModuleMsg("main", "reload"))
                }
                "status" -> {
                    if (args.size != 2) Util.invalidArgs(sender) else {
                        val n = args[1]
                        val module = Util.getModuleByName(n)
                        if (module == null) Util.send(sender, Util.insert(Util.getModuleMsg("main", "module-not-found"), "moduleName" to n))
                        else Util.send(sender, Util.insert(Util.getModuleMsg("main", "status"), "moduleName" to n, "status" to if (module.getEnabled()) "&aTRUE" else "&cFALSE"))
                    }
                }
                "info" -> getInfo().forEach { Util.send(sender, it) }
                else -> Util.invalidArgs(sender)
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): List<String> {
        val subCommands = arrayOf("help", "reload", "status", "info")
        if (args.size > 1) return ModuleManager.moduleInstances.stream().map {
            it.getName()
        }.collect(Collectors.toList()).filter { s: String -> s.startsWith(args[1]) }
        else if (args.isEmpty()) return listOf(*subCommands).filter { s: String -> s.startsWith(args[0]) }
        else if (args.size > 2) return emptyList()
        return Arrays.stream(subCommands).filter { s: String ->
            s.startsWith(args[0])
        }.collect(Collectors.toList())
    }
}