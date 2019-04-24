package command

import scopt.OptionParser
import scalaj.http._
import play.api.libs.json._

object MigrateUsers {

  private[this] case class Params(fromUrl: String = "",
                                  fromAuth: String = "",
                                  toUrl: String = "",
                                  toAuth: String = "",
                                  timeout: Int = 5000,
                                 )

  def getUsers(params: Params) = {
    val request = Http(params.fromUrl + "/starchat_system_0.user/_search?filter_path=hits.hits._source&size=100")
      .headers(Seq(("Authorization", "Basic " + params.fromAuth), ("Content-Type", "application/json")))
    request.option(HttpOptions.readTimeout(params.timeout)).asString
  }

  def insertUsers(params: Params, users: List[String]) = {
    val requests = users.map( user => Http(params.toUrl + "/user")
      .headers(Seq(("Authorization", "Basic " + params.toAuth), ("Content-Type", "application/json")))
      .postData(user))
    requests.map(_.asString)
  }

  private[this] def execute(params: Params) {
    val usersJsVal = Json.parse(getUsers(params).body)
    val users = usersJsVal("hits")("hits").as[JsArray]
      .value.map(_("_source")).toList.map(_.toString)
    val responses = insertUsers(params, users)
    val (succeed, failed) = responses.foldLeft((0,0))(
      (b, response) =>
        if(response.is2xx) (b._1+1, b._2)
        else {println(response.body); (b._1, b._2+1)})
    println(f"Posted $succeed users successfully and $failed failed")
  }

  def main(args: Array[String]) {
    val defaultParams = Params()
    val parser = new OptionParser[Params]("MigrateDecisionTable") {
      head("Migrate DecisionTable data from a StarChat version to another")
      arg[String]("fromUrl")
        .text("url for elasticsearch to migrate from")
        .action((url, params) => params.copy(fromUrl = url))
      arg[String]("fromAuth")
        .text("Authorizaion for the elasticsearch to migrate from")
        .action((auth, params) => params.copy(fromAuth = auth))
      arg[String]("toUrl")
        .text("Url for StarChat to migrate to")
        .action((url, params) => params.copy(toUrl = url))
      arg[String]("toAuth")
        .text("Authorizaion for the StarChat to migrate to")
        .action((auth, params) => params.copy(toAuth = auth))
      opt[Int]("timeout")
        .text("Read timeout in milliseconds")
        .action((time, params) => params.copy(timeout = time))
      help("help").text("prints this usage text")
    }
    parser.parse(args, defaultParams) match {
      case Some(params) =>
        execute(params)
      case _ =>
    }
  }
}
