package com.lanier.violet.client

import com.lanier.violet.client.processor.PetBackpackProcessor
import com.lanier.violet.data.PetBackpackData
import com.lanier.violet.data.UserData
import com.lanier.violet.ext.post
import com.lanier.violet.feature.main.event.ClientEvent
import com.lanier.violet.feature.main.event.FarmEvent
import com.lanier.violet.feature.main.event.PetBackpackHandleEvent
import com.lanier.violet.feature.main.event.SceneEvent
import com.lanier.violet.feature.main.event.UserInfoEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.Socket
import java.nio.charset.Charset
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
                isConnected = false
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
                    isConnected = true
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
                val tempMessage = message.replace(" ", "")
                withContext(Dispatchers.IO) {
                    println(">>>> send command $tempMessage")
                    val byteMsg = tempMessage.hexToByteArray()
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
                    val suffix = prefix.substring(8, prefix.length)
                    when (suffix) {
                        "00030001" -> {
                            val sceneId = message.substring(56, 60).hexToInt()
                            UserData.currentSceneId = sceneId
                            SceneEvent(sceneId).post()
                            ClientEvent(ClientEvent.ACTION_ENTER_CHANNEL).post()
                        }

                        "00030004" -> {
                            val newSceneId = message.substring(52, 56).hexToInt()
                            SceneEvent(newSceneId, UserData.currentSceneId).post()
                            UserData.currentSceneId = newSceneId
                        }

                        "00120005" -> {
                            FarmEvent(FarmEvent.HARVEST, UserData.farmLandHandleIndex).post()
                            if (UserData.farmLandHandleIndex == UserData.LAND_MAX_INDEX) {
                                UserData.farmLandHandleIndex = 0
                            } else {
                                UserData.farmLandHandleIndex++
                            }
                        }
                    }
                    when (prefix) {
                        ByteDataConstant.ENTER_SERVER -> {
                            enterServerMessageHandle()
                        }

                        ByteDataConstant.PERSONAL_INFO -> {
                            val name = buildString {
                                message
                                    .substring(60, 88)
                                    .chunked(4)
                                    .forEach {
                                        if (it != "0000") {
                                            append(
                                                it.hexToByteArray()
                                                    .toString(Charset.forName("GBK"))
                                            )
                                        }
                                    }
                            }
                            UserInfoEvent(
                                username = name,
                                userLevel = message.substring(100, 104).hexToInt(),
                                userCredit = message.substring(104, 112).hexToInt(),
                                userTargetCredit = message.substring(112, 120).hexToInt(),
                                userStamina = message.substring(128, 136).hexToInt(),
                                userWisdom = message.substring(136, 144).hexToInt(),
                                userCharm = message.substring(144, 152).hexToInt(),
                                userCoins = message.substring(152, 160).hexToInt(),
                                vipDays = message.substring(258, 266).hexToInt(),
                            ).post()
                        }

                        ByteDataConstant.PET_BACKPACK -> {
                            val data = PetBackpackProcessor(message).process()
                            PetBackpackData.reset(data)
                            PetBackpackHandleEvent(true).post()
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
        sendCommand(sb3) // 发送连接频道消息
    }

    /**
     * 计算服务器编号
     *
     * @param channel 想进入的服务器频道, 值为1-300, 与游戏一致
     *
     * @return 服务器编号
     */
    private fun calcServerIDByChannel(channel: Int): Int {
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