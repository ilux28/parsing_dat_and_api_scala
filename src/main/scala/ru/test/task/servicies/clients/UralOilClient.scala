package ru.test.task.servicies.clients

import cats.effect.{ContextShift, _}
import cats.implicits._
import cats.{FlatMap, MonadError}
import ru.test.task.servicies.errors.CustomException
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import sttp.client.{Response, asStringAlways, basicRequest}
import sttp.model.Uri
import sttp.client._

class UralOilClient[F[_]: ContextShift: Concurrent: FlatMap](M: MonadError[F, Throwable]) {

//    val accessToken = "access_token=96e28cd194719eebb1c717034384bfa4"
//  val accessToken = "96e28cd194719eebb1c717034384bfa4"
  val uriForOilResource = uri"https://data.gov.ru/api/json/dataset/7710349494-urals/version/20191105T160551/content/"

  def getDataForOil(): F[Response[String]] = {
    def resEffect: F[Response[String]] = AsyncHttpClientCatsBackend[F]().flatMap { implicit backend =>
      val response: F[Response[String]] = basicRequest
        .response(asStringAlways)
        .header("Content-Type", "application/json")
        .get(uriForOilResource)
        //.auth.bearer("96e28cd194719eebb1c717034384bfa4")
        .auth.basic("ilya.pribytko@mail.ru", "rilo123456")
        .contentType("application/json")
//        .body(formingJson.formRequestForProfile(msisdn, attributes))
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
