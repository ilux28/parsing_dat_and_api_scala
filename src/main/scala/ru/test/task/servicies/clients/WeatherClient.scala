package ru.test.task.servicies.clients

import cats.{FlatMap, MonadError}
import cats.effect.{Concurrent, ContextShift}
import ru.test.task.servicies.errors.CustomException
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.{Response, asStringAlways, basicRequest}
import sttp.client._
import cats.implicits._

/***
 * This class is the client for send json contains info with weather
 * @param M
 * @tparam F tagless final
 */
class WeatherClient [F[_]: ContextShift: Concurrent: FlatMap](M: MonadError[F, Throwable]) {

  val uriForWether = uri"https://data.gov.ru/api/json/dataset/7710349494-urals/version/20191105T160551/content/"

  def getDataForOil(): F[Response[String]] = {
    def resEffect: F[Response[String]] = AsyncHttpClientCatsBackend[F]().flatMap { implicit backend =>
      val response: F[Response[String]] = basicRequest
        .response(asStringAlways)
        .header("Content-Type", "application/json")
        .get(uriForWether)
        //.auth.bearer("96e28cd194719eebb1c717034384bfa4")
        .auth.basic("ilya.pribytko@mail.ru", "rilo123456")
        .contentType("application/json")
        .send()
      for {
        tempContent <- response
        tempContentOpt = Option(tempContent)
        resContent <- M.fromOption(tempContentOpt, CustomException)
      } yield resContent
    }
    resEffect
  }
}
