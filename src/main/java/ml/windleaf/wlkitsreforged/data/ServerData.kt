package ml.windleaf.wlkitsreforged.data

import org.bukkit.Server

/**
 * The server data internal, saves information of server
 */
@Suppress("UNUSED")
data class ServerData(val server: Server, override val T: Class<*> = Server::class.java) : Data {
    private val nmsServer: Class<*>
        get() = server.javaClass.getMethod("getServer").invoke(server).javaClass

    /**
     * The ip of server
     */
    @DataField
    val ip = server.ip

    /**
     * The port of server
     */
    @DataField
    val port = server.port

    /**
     * The banned players list
     */
    @DataField
    val banned: List<String> = server.bannedPlayers.map { it.name!! }

    /**
     * The online players list
     */
    @DataField
    val online: List<String> = server.onlinePlayers.map { it.name }

    /**
     * The offline players list
     */
    @DataField
    val offline: List<String> = server.offlinePlayers.map { it.name!! }

    /**
     * The operators of server list
     */
    @DataField
    val ops: List<String> = server.operators.map { it.name!! }

    /**
     * The bukkit version of server
     */
    @DataField
    val bukkitVersion = server.bukkitVersion

    /**
     * The version of server
     */
    @DataField
    val version = server.version

    /**
     * The max players of server
     */
    @DataField
    val maxPlayers = server.maxPlayers

    /**
     * The motd of server
     */
    @DataField
    val motd = server.motd

    /**
     * The boolean of online mode of server
     */
    @DataField
    val onlineMode = server.onlineMode

    /**
     * The boolean of whitelist of server
     */
    @DataField
    val whitelist = server.hasWhitelist()

    /**
     * The name of server
     */
    @DataField
    val name = server.name

    /**
     * The TPS of server
     */
    @DataField
    val tps = (nmsServer.getField("recentTps")
            .get(nmsServer.getMethod("getServer")
            .invoke(null)) as DoubleArray)[0]
}