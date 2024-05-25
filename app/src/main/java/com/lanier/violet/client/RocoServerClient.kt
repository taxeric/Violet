package com.lanier.violet.client

import com.lanier.violet.UserData
import com.lanier.violet.ext.post
import com.lanier.violet.feature.main.event.ClientEvent
import com.lanier.violet.feature.main.event.SceneEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.Socket
import kotlin.random.Random

/**
 * Created by 幻弦让叶
 * Date 2024/5/19 19:09
 *
 * 保持与服务器连接的客户端
 */
object RocoServerClient {

    private val mainScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var client: Socket? = null

    var isConnected = false
        private set

    /**
     * 进入的频道
     */
    @OptIn(ExperimentalStdlibApi::class)
    var channel = ""
        set(value) {
            field = value
            channelHex = channel.toInt().toHexString()
        }

    /**
     * 频道16进制
     */
    private var channelHex = ""

    /**
     * 连接服务器
     */
    fun linkServer(
        onSuccess: () -> Unit,
        onFailed: (Throwable) -> Unit,
    ) {
        mainScope.launch(
            context = CoroutineExceptionHandler { _, throwable ->
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
            val calcServer = calcServerIDByChannel(channel.toInt())
            val serverIP = obtainServerIPAddressById(calcServer)
            println(">>>> obtain server ip = $serverIP")
            ClientEvent(ClientEvent.ACTION_CONNECTING).post()
            client = withContext(Dispatchers.IO) {
                Socket(serverIP, 443)
            }
            client?.isConnected?.let {
                if (it) {
                    onSuccess.invoke()
                    enterServer(calcServer)
                    ClientEvent(ClientEvent.ACTION_CONNECTED).post()
                    client?.getInputStream()?.run {
                        receiveMsgInternal(this)
                    }
                } else {
                    onFailed.invoke(Throwable("can't connect to server, has not connect to server"))
                    ClientEvent(ClientEvent.ACTION_DISCONNECTED).post()
                }
            } ?: run {
                onFailed.invoke(Throwable("can't connect to server, the connect state is null"))
                ClientEvent(ClientEvent.ACTION_DISCONNECTED).post()
            }
        }
    }

    /**
     * 发送命令
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun sendCommand(message: String, onFailed: ((Throwable) -> Unit)? = null) {
        mainScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                println(">>>> send command failed ${throwable.message}")
                onFailed?.invoke(throwable)
                isConnected = false
                client?.close()
            }
        ) {
            client?.let {
                withContext(Dispatchers.IO) {
                    println(">>>> send command $message")
                    val byteMsg = message.hexToByteArray()
                    it.getOutputStream().write(byteMsg)
                    it.getOutputStream().flush()
                }
            }
        }
    }

    /**
     * 进入服务器
     *
     * @param serverId 计算得到的服务器编号
     */
    private fun enterServer(serverId: Int) {
        mainScope.launch {
            val sb = buildString {
                append("7467775F6C375F666F72776172640D0A486F73743A207A6F6E65")
                serverId.toString().forEach {
                    append("3")
                    append(it)
                }
                append("2E3137726F636F2E71712E636F6D3A3434330D0A0D0A")
            }
            withContext(Dispatchers.IO) {
                sendCommand(sb)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun receiveMsgInternal(`is`: InputStream) {
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            while (`is`.read(buffer) != -1) {
                val message = buffer.toHexString().uppercase()
                println(">>>> message = $message")
                val prefix = message.substring(0, 16)
                withContext(Dispatchers.Main) {
                    when (prefix) {
                        "9527000000010002" -> {
                            enterServerMessageHandle()
                        }

                        "9527DC7300030001" -> {
                            val sceneId = message.substring(56, 60).hexToInt()
                            SceneEvent(sceneId).post()
                            ClientEvent(ClientEvent.ACTION_ENTER_CHANNEL).post()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    /**
     * 处理进入服务器消息
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun enterServerMessageHandle() {
        val sb3 = buildString {
            append("9527000000030001")
            append(UserData.hexQQ.uppercase())
            append("0000000000000042")
            append(channelHex.substring(4, channelHex.length).uppercase())
            append(UserData.mainKey.toByteArray().toHexString())
        }
        sendCommand(sb3)
    }

    /**
     * 计算服务器编号
     *
     * @param channel 想进入的服务器频道, 值为1-300, 与游戏一致
     *
     * @return 服务器编号
     */
    private fun calcServerIDByChannel(channel: Int) : Int {
        return when (val mChannel = channel / 50 + 5) {
            in 5..10 -> mChannel
            11 -> 1
            else -> Random.nextInt(5, 10)
        }
    }

    /**
     * 根据服务器编号返回对应ip
     *
     * @param serverId 服务器编号
     */
    private fun obtainServerIPAddressById(serverId: Int): String {
        return when (serverId) {
            1 -> "109.244.212.148"
//            2 -> "101.89.0.211"
//            3 -> "101.226.49.105"
//            4 -> "61.151.167.201"
            5 -> "101.91.21.94"
            6 -> "101.91.21.39"
            7 -> "101.91.22.91"
            8 -> "180.163.210.31"
            9 -> "101.227.117.80"
            10 -> "101.226.144.45"
            else -> "101.226.144.45"
        }
    }
}