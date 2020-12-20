package com.wonjong.lee.config

import com.wonjong.lee.entity.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

/**
 * @author CaptainWonJong@gmail.com
 */
object DatabaseInitializer {
    fun init() {
        Database.connect(HikariDataSource(HikariConfig("/hikari.properties")))
        transaction {
            create(Users)
        }
    }
}

suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction {
        block()
    }
}