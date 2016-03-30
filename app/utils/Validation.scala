package utils

import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import play.api.data.validation._
import play.api.libs.json._

/**
  * This object gives some utils functions to validate JSON content
  */
object Validation {

  val simpleDatePattern = "yyyy-MM-dd"

  object Reads {

    import play.api.libs.json.{Reads => PlayReads}

    def notEmpty(implicit r: Reads[String]): Reads[String] = new Reads[String] {
      override def reads(json: JsValue): JsResult[String] = {
        json match {
          case JsString(s) if StringUtils.isNotBlank(s) => JsSuccess(s)
          case _ => JsError("Should not be empty String")
        }
      }
    }

    def isSimpleDate(implicit r: Reads[DateTime]): Reads[DateTime] = PlayReads.jodaDateReads(simpleDatePattern)

  }

  object Writes {

    import play.api.libs.json.{Writes => PlayWrites}

    def asSimpleDate(implicit w: Writes[DateTime]): Writes[DateTime] = PlayWrites.jodaDateWrites(simpleDatePattern)

  }
}
