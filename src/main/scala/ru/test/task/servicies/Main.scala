package ru.test.task.servicies

import java.util.concurrent._

import cats.MonadError
import cats.effect.{IO, IOApp, _}
import io.circe.Encoder
import org.http4s.{HttpRoutes, MediaType, Uri, _}
import org.http4s.client._
import org.http4s.client.dsl.io._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.headers._
import io.circe.generic.auto._
import org.http4s.implicits._
import org.http4s.client._
import cats.effect._
import io.circe._
import io.circe.literal._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.client.dsl.io._
import org.http4s.server.blaze.BlazeServerBuilder
import org.slf4j.LoggerFactory
import ru.test.task.servicies.clients.UralOilClient
import ru.test.task.servicies.entity.Car
import ru.test.task.servicies.repo.CarsDAO

import scala.collection.mutable._
import net.liftweb.json._
import net.liftweb.json.Serialization.write

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

/** *
 * EndPoint for service which receive json with msisdn and other params for subscriber, and
 * return json with offers for him
 */

object Main extends IOApp {

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val monadError = MonadError[IO, Throwable]
  val logger = LoggerFactory.getLogger("Logger")
  val uralOilClient = new UralOilClient[IO](monadError)
  implicit val decoder = jsonOf[IO, Car]

  implicit val formats = DefaultFormats
  implicit val CarEncoder: Encoder[Car] =
    Encoder.instance { car: Car =>
      json"""{"number": ${car.number},
            "mark": ${car.mark},
            "colour": ${car.colour},
            "dateOfIssue": ${car.dateOfIssue}
            }"""
    }
  val carsDao: CarsDAO = CarsDAO()
  private def forMethodDB(functionCarProcessing: Car => Unit, req: Request[IO]) =  {
    for {
      receiveJsonCar <- req.as[Car]
      _  = functionCarProcessing(receiveJsonCar)
      resResponse = carsDao.getAllCars()
      carsJson = write(resResponse)
      resp <- Ok(carsJson)
    } yield resp
  }
  val rtimProxyServiceRoutes = HttpRoutes.of[IO] {
    case GET -> Root =>
      val resResp = for {
          resp <- Ok(write(carsDao.getAllCars()))
      } yield resp
      resResp
    case req @ POST -> Root / add => {
      forMethodDB(carsDao.insertCar, req)
    }
    case req @ PATCH -> Root / update => {
      forMethodDB(carsDao.updateByCar, req)
    }
    case req @ DELETE -> Root / delete => {
      forMethodDB(carsDao.deleteCarByNumber, req)
    }
  }.orNotFound

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder.apply[IO](ec)
      .bindHttp(8087, "localhost")
      .withHttpApp(rtimProxyServiceRoutes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
