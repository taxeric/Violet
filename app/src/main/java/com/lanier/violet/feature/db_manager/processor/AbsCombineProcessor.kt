package com.lanier.violet.feature.db_manager.processor

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class AbsCombineProcessor(
    private val filepath: String
) {

    protected val baseUrl = "https://raw.githubusercontent.com/taxeric/violet-database/main"

    abstract val path: String

    protected fun readFromOrigin(
        tableName: String,
        filename: String,
    ) : Pair<String, String> {
        val resultUrl = baseUrl + path + filename
        val connection = URL(resultUrl).openConnection() as HttpURLConnection
        connection.connect()
        val `is` = connection.inputStream
        val parentFile = File(filepath + path)
        if (parentFile.exists().not()) parentFile.mkdir()
        val file = File(filepath + path, filename)
        if (file.exists().not()) file.createNewFile()
        file.outputStream().use { outputStream ->
            val buffer = ByteArray(4 * 1024)
            while (true) {
                val byteCount = `is`.read(buffer)
                if (byteCount == -1) break
                outputStream.write(buffer, 0, byteCount)
            }
        }
        connection.disconnect()
        val bufferedReader = BufferedReader(InputStreamReader(file.inputStream()))
        val content = bufferedReader.readText()
        return Pair(tableName, content)
    }

    protected fun extractAttributes(text: String): Map<String, String> {
        val regex = """(\w+)="([^"]*)"""".toRegex()
        return regex.findAll(text)
            .map { it.groupValues[1] to it.groupValues[2] }
            .toMap()
    }

    /**
     * 适用于解析<xxx />类型的数据
     *
     * @param prefix 前缀节点
     */
    protected fun parseAttributes(text: String, prefix: String): Map<String, String> {
        return text
            .removePrefix("<$prefix ")
            .removeSuffix("\n")
            .removeSuffix("/>")

            .split(" (?=\\w+=\"[^\"]*\")".toRegex())
            .map { it.split("=") }
            .associate {
                val key = it[0]
                val value = it[1].removeSurrounding("\"")
                key to value
            }
    }

    abstract suspend fun sync(
        onStart: (() -> Unit)? = null,
        onCompleted: (() -> Unit)? = null,
    )
}