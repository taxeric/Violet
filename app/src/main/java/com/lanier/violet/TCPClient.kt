package com.lanier.violet

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
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

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var client: Socket? = null

    var isConnected = false
        private set

    fun linkServer(
        server: String,
        onSuccess: () -> Unit,
        onFailed: (Throwable) -> Unit,
    ) {
        mainScope.launch(
            context = CoroutineExceptionHandler { coroutineContext, throwable ->
                onFailed.invoke(throwable)
            }
        ) {
            client?.run {
                if (this.isClosed.not()) {
                    withContext(Dispatchers.IO) {
                        close()
                    }
                    delay(1000L)
                }
            }
            client = null
            val serverIP = obtainServerIP(server)
            client = withContext(Dispatchers.IO) {
                Socket(serverIP, 443)
            }
            client?.isConnected?.let {
                if (it) {
                    onSuccess.invoke()
                    enterServer(server)
                    client?.getInputStream()?.run {
                        receiveMsgInternal(this)
                    }
                } else onFailed.invoke(Throwable("can't connect to server"))
            } ?: onFailed.invoke(Throwable("can't connect to server"))
        }
    }

    fun sendCommand(message: String) {
        mainScope.launch {
            client?.let {
                withContext(Dispatchers.IO) {
                    try {
                        val byteMsg = message.toByteArray()
                        it.getOutputStream().write(byteMsg)
                        it.getOutputStream().flush()
                    } catch (e: Exception) {
                        isConnected = false
                        it.close()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun enterServer(server: String) {
        mainScope.launch {
            val sb = buildString {
                append("7467775F6C375F666F72776172640D0A486F73743A207A6F6E65")
                append(server)
                append("2E3137726F636F2E71712E636F6D3A3434330D0A0D0A")
            }
            withContext(Dispatchers.IO) {
                sendCommand(sb)
            }
            withContext(Dispatchers.IO) {
                val sb4 = buildString {
                    append("9527000000030001")
                    append(UserData.QQ)
                    append("0000000000000042")
                }
                val sb3 = buildString {
                    append(sb4)
                    val m = "0000${server.toLong().toHexString().uppercase()}"
                    val n = m.substring(m.length - 4, m.length)
                    append(n)
                }
                val sb2 = buildString {
                    append(sb3)
                    append(UserData.mainKey)
                }
                sendCommand(sb2)
            }
        }
    }

    private suspend fun receiveMsgInternal(`is`: InputStream) {
        withContext(Dispatchers.IO) {
            val data = `is`.readBytes()
            withContext(Dispatchers.Main) {
                processServerData(data)
            }
        }
    }

    private fun processServerData(data: ByteArray) {
    }

    private suspend fun obtainServerIP(server: String): String {
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
            ipAddress ?: defIP(server_s)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            defIP(server_s)
        }
    }

    private fun defIP(server_s: String): String {
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