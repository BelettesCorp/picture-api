import javax.inject.Inject

import play.api.http.HttpFilters
import utils.CorsFilter

class Filters @Inject() (corsFilter: CorsFilter) extends HttpFilters {
  val filters = Seq(corsFilter)
}