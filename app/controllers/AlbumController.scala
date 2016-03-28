package controllers

import javax.inject.{Inject, Singleton}

import models.{Album, AlbumDB}
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// TODO RCH : check JsonAPI specs

@Singleton
class AlbumController @Inject()(val albumDB: AlbumDB)
  extends Controller {

  def getAllAlbums = Action.async {
    albumDB.findAll().map { albums =>
      Ok(Json.toJson(albums))
    }
  }

  def getAlbum(id: String) = Action.async {
    albumDB.findById(id).map {
      case Some(album) => Ok(Json.toJson(album))
      case _ => NotFound
    }
  }

  def createAlbum = Action.async(parse.json) { request =>
    import ApiFormat.AlbumFormat.albumCreateFormat
    request.body.validate[Album] match {
      case JsSuccess(album, _) =>
        albumDB.insert(album).map { lastError =>
          if(lastError.ok) Created
          else {
            Logger.error(s"DB action failed: ${lastError.message}")
            InternalServerError
          }
        }

      case e: JsError =>
        Future.successful(BadRequest(JsError.toJson(e)))
    }
  }
}