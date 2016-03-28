package controllers

import javax.inject._

import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json.ImplicitBSONHandlers._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MongoDbController @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  val imagesDB = db.collection[JSONCollection]("images")

  def getImages = Action.async {
    val picture = Json.obj("_id" -> 1, "name" -> "first_picture")
    imagesDB.update(Json.obj("_id" -> 1), picture).map { res =>
      if(res.ok)
        Created(picture)
      else
        InternalServerError
    }
  }

}