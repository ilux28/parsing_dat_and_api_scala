package ru.test.task.servicies.dbconnection

import java.sql.{Connection, DriverManager}

class DBConnection {
  println("Postgres connector")
  classOf[org.postgresql.Driver]
  val con_st: String = "jdbc:postgresql://localhost:5432/db_for_car_store"

  private def connection: Connection = DriverManager.getConnection(con_st,"adjie","1234")
}
object DBConnection {
  def getConnection() = {
    val dbConn = new DBConnection
    dbConn.connection
  }
}