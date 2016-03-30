package controllers

import models.{Album, Picture, SharingOptions}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, _}
import reactivemongo.bson.BSONObjectID
import utils.Validation
import utils.Validation.Reads._


object ApiFormat {

  // All dates should fit to API date format by default
  implicit val apiDateWrite = Writes.jodaDateWrites(Validation.simpleDatePattern)
  implicit val apiDateRead = Reads.jodaDateReads(Validation.simpleDatePattern)

  private def generateId() = __.json.update((__ \ '_id).json.put(JsString(BSONObjectID.generate.stringify)))
  private def ignoreId() = __.json.update((__ \ '_id).json.put(JsString("")))

  object AlbumFormat {
    // Reads
    implicit val offerCreateRead: Reads[Album] = generateId() andThen defaultRead

    def defaultRead: Reads[Album] = (
        (__ \ "_id").read[String] and
        (__ \ "name").read[String](notEmpty) and
        (__ \ "resume").read[String] and
        (__ \ "pictureId").readNullable[String] and
        (__ \ "sharingOptions").read[SharingOptions] and
        (__ \ "pictures").read[List[Picture]]
    )(Album.apply _)

    // Writes
    def defaultWrite: Writes[Album] = Json.writes[Album]

    // Formats
    implicit val defaultFormat = Format[Album](defaultRead, defaultWrite)
    implicit val albumCreateFormat = Format[Album](offerCreateRead, defaultWrite)
  }
}
