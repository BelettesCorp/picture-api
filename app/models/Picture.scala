package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID


// TODO RCH : implement this
case class Picture(
  _id: String = BSONObjectID.generate.stringify
)

object Picture {
  implicit val defaultFormat = Json.format[Picture]
}
