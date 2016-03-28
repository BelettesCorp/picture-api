package services

import play.api.Play._
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.ReadPreference
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Use this abstract class for each model class.
  * This class gives some default methods to manage collection such as 'insert', 'update', 'find', 'remove'...
  */
abstract class Mongo[T](collectionName: String) {
  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]
  lazy val collection = reactiveMongoApi.db.collection[JSONCollection](collectionName)
  implicit val defaultFormat: OFormat[T]

  // TODO RCH : manage mongo indexes

  def insert(obj: T) = {
    collection.insert(obj)
  }

  def update(id: String, obj: T) = {
    collection.update(Json.obj("_id" -> id), obj, upsert = true)
  }

  def remove(id: String) = {
    collection.remove(Json.obj("_id" -> id))
  }

  def find(query: JsObject = Json.obj(), sort: JsObject = Json.obj(), limit: Int = Int.MaxValue, maybeSkip: Option[Int] = None): Future[List[T]] = {
    var builder = collection.find(query)

    if (sort.keys.nonEmpty)
      builder = builder.sort(sort)

    val result = builder
      .cursor[T](ReadPreference.primaryPreferred)
      .collect[List](limit)

    maybeSkip.map{skip =>
      result.map(_.drop(skip))
    } getOrElse result
  }

  def findOne(query: JsObject, sort: JsObject = Json.obj()): Future[Option[T]] = {
    collection
      .find(query)
      .sort(sort)
      .one[T]
  }

  def findById(id: String): Future[Option[T]] = {
    findOne(query = Json.obj("_id" -> id))
  }

  def findAll(sort: JsObject = Json.obj(), limit: Int = Int.MaxValue) = {
    find(query = Json.obj(), sort = sort, limit = limit)
  }

}
