package models

import javax.inject.Singleton

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import services.Mongo

// TODO RCH : implement this
case class SharingPermissions(
 canRead: Boolean,
 canEdit: Boolean,
 canDelete: Boolean,
 canShare: Boolean
)
object SharingPermissions {
  implicit val defaultFormat = Json.format[SharingPermissions]
}

case class SharingOptions(
  level: String, // Should be one of SharingLevel
  permissions: Option[SharingPermissions] = None
)
object SharingOptions {
  implicit val defaultFormat = Json.format[SharingOptions]
}

case class Album(
  _id: String = BSONObjectID.generate.stringify,
  name: String,
  resume: String,
  pictureId: Option[String] = None,
  sharingOptions: SharingOptions,
  pictures: List[Picture] = List.empty
)
object Album {
  implicit val defaultFormat = Json.format[Album]
}

@Singleton
class AlbumDB extends Mongo[Album]("albums") {
  override implicit val defaultFormat = Album.defaultFormat
}
