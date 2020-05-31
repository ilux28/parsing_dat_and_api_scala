package ru.test.task.servicies.repo

import java.sql.{Connection, DriverManager, ResultSet, SQLException}

import ru.test.task.servicies.dbconnection.{DBConnection, PoolConnection}
import ru.test.task.servicies.entity.Car
import sun.util.calendar.LocalGregorianCalendar.Date

import scala.collection.mutable.ArrayBuffer

class CarsDAO {

  def insertCar(car: Car): Unit = {
//    val conn = DBConnection.getConnection()
    val query = s"insert into car (number, mark, colour, date_of_issue) VALUES (?, ?, ?, ?)"
    PoolConnection.getConnection match {
      case Some(conn) => {
        try {
          val preparedStatement = conn.prepareStatement(query)
          preparedStatement.setInt(1, car.number)
          preparedStatement.setString(2, car.mark)
          preparedStatement.setString(3, car.colour)
          preparedStatement.setInt(4, car.dateOfIssue)
          preparedStatement.executeUpdate()
          conn.close
        } catch {
          case ex: SQLException => {
            ex.printStackTrace()
          }
        } finally {
          if (!conn.isClosed) conn.close
        }
      }
      case None => println("Not getting connection from connection pooling")
    }
  }

  def deleteCarByNumber(car: Car)= {
//    val conn = DBConnection.getConnection()
    val query: String = "delete from car where number = ?"
    PoolConnection.getConnection match {
      case Some(conn) => {
        try {
          val preparedStmt = conn.prepareStatement(query)
          preparedStmt.setInt(1, car.number)
          preparedStmt.execute
          preparedStmt.close()
        } catch {
          case ex: SQLException => {
            ex.printStackTrace()
          }
        } finally {
          if (!conn.isClosed) conn.close
        }
      }
      case None => println("Not getting connection from connection pooling")
    }
  }

  def updateByCar(car: Car): Unit = {
//    val conn = DBConnection.getConnection()
    val query: String = "update car SET mark = ?, colour = ?, date_of_issue = ? WHERE number = ?"
    PoolConnection.getConnection match {
      case Some(conn) => {
        try {
            val preparedStmt = conn.prepareStatement(query)
            preparedStmt.setString(1,car.mark)
            preparedStmt.setString(2,car.colour)
            preparedStmt.setInt(3,car.dateOfIssue)
            preparedStmt.setInt(4,car.number)
            preparedStmt.executeUpdate()
            preparedStmt.close()
          } catch {
          case ex: SQLException => {
            ex.printStackTrace()
          }
        }  finally {
          if (!conn.isClosed) conn.close
        }
      }
      case None => {
        println("Not getting connection from connection pooling")
      }
      }
  }

  def getAllCars() = {
//    val conn = DBConnection.getConnection()
    val query: String = "select * from car"
    val arrayCars = new ArrayBuffer[Car]()

    PoolConnection.getConnection match {
      case Some(conn) => {
        try {
          val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
          val rs = stm.executeQuery(query)
          while (rs.next) {
            val number = rs.getInt("number")
            val mark = rs.getString("mark")
            val colour = rs.getString("colour")
            val date = rs.getInt("date_of_issue")
            arrayCars += Car(number, mark, colour, date)
          }
          arrayCars
        } catch {
          case ex: SQLException => {
            ex.printStackTrace()
            arrayCars
          }
        } finally {
          if (!conn.isClosed) conn.close
        }
      }
      case None => {
        println("Not getting connection from connection pooling")
        arrayCars
      }
    }
  }
}

object CarsDAO {
  def apply(): CarsDAO = new CarsDAO
}
