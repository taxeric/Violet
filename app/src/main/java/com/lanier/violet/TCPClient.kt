package com.lanier.violet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import kotlin.random.Random

/**
 * Created by 幻弦让叶
 * Date 2024/5/19 19:09
 *
 * 保持与服务器连接的客户端
 */
object TCPClient {

    private var client: Socket? = null

    suspend fun init() {
        val serverIP = obtainServerIP("300")
        client = withContext(Dispatchers.IO) {
            Socket(serverIP, 443)
        }
    }

    private suspend fun obtainServerIP(server: String) : String {
        var server_d = server.toDouble() / 50.0 + 2.toByte() + 3.toByte()
        var server_s = server_d.toString()
        if (server_s.substring(server_s.length - 2, server_s.length) == ".0") {
            server_d -= 1.toByte()
            server_s = server_d.toInt().toString()
        }
        val serverAddress = buildString {
            append("zone")
            append(server_s)
            append(".17roco.qq.com")
        }
        return try {
            val address: InetAddress =
                withContext(Dispatchers.IO) {
                    InetAddress.getByName(serverAddress)
                }
            val ipAddress: String? = address.hostAddress
            ipAddress?: defIP(server_s)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            defIP(server_s)
        }
    }

    private fun defIP(server_s: String) : String {
        return when (server_s.first()) {
            '2' -> "101.89.0.211"
            '3' -> "101.226.49.105"
            '4' -> "61.151.167.201"
            '5' -> "101.91.21.94"
            '6' -> "101.91.21.39"
            '7' -> "101.91.22.91"
            '8' -> "180.163.210.31"
            '9' -> "101.227.117.80"
            else -> {
                when (Random.nextInt(3)) {
                    0 -> "101.226.144.45"
                    1 -> "109.244.212.148"
                    else -> "140.207.69.63"
                }
            }
        }
    }
}