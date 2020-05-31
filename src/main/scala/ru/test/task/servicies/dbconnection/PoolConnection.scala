package ru.test.task.servicies.dbconnection

import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig
import org.slf4j.LoggerFactory
import java.sql.Connection
//import java.sql.DriverManager</code>

object PoolConnection {
  val logger = LoggerFactory.getLogger(this.getClass)
  private val connectionPool = {
    try {
      Class.forName("org.postgresql.Driver")
      val config = new BoneCPConfig()
      config.setJdbcUrl("jdbc:postgresql://localhost:5432/db_for_car_store")
      config.setUsername("adjie")
      config.setPassword("1234")
      config.setMinConnectionsPerPartition(2)
      config.setMaxConnectionsPerPartition(5)
      config.setPartitionCount(3)
      config.setCloseConnectionWatch(true)// if connection is not closed throw exception
      config.setLogStatementsEnabled(true) // for debugging purpose
      Some(new BoneCP(config))
    } catch {
      case exception: Exception =>;
        logger.warn("Error in creation of connection pool"+exception.printStackTrace())
        None
    }
  }

  def getConnection: Option[Connection] = {
    connectionPool match {
      case Some(connPool) => Some(connPool.getConnection)
      case None => None
    }
  }
}