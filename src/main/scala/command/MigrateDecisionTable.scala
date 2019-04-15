package command

import scopt.OptionParser
import scalaj.http._
import entities.{DTDocument, SearchDTDocumentsResults}
import conversion.DTDocumentJsonConversion
import play.api.libs.json._

import scala.language.implicitConversions

object MigrateDecisionTable extends DTDocumentJsonConversion {

  private[this] case class Params(fromUrl: String = "",
                                  fromAuth: String = "",
                                  toUrl: String = "",
                                  toAuth: String = ""
                                 )


  def readDocsFrom(params: Params): List[DTDocument] = {
    val response: HttpResponse[String] = Http(params.fromUrl+"/decisiontable?dump=true")
      .headers(Seq(("Authorization", "Basic "+params.fromAuth)))
      .asString
    val dtDocumentResults = Json.parse(response.body).as[SearchDTDocumentsResults]
    dtDocumentResults.hits.map(_.document)
  }


  def writeDocsTo(params: Params, documents: List[DTDocument]) = {
    val requests = documents.map( document => Http(params.toUrl+"/decisiontable")
      .postData(Json.toJson(document).toString)
      .headers(Seq(("Authorization", "Basic " + params.toAuth), ("Content-Type", "application/json")))
    )
    requests.map(_.asString)
  }

  private[this] def execute(params: Params) {
    println(params)
    val documents = readDocsFrom(params)
    val responses = writeDocsTo(params, documents)
    println(responses)
  }

  def main(args: Array[String]) {
    val defaultParams = Params()
    val parser = new OptionParser[Params]("MigrateDecisionTable") {
      head("Migrate DecisionTable data from a StarChat version to another")
      arg[String]("fromUrl")
        .text("url for StarChat to migrate from")
        .action((url, params) => params.copy(fromUrl = url))
      arg[String]("fromAuth").required()
        .text("Authorizaion for the StarChat to migrate from")
        .action((auth, params) => params.copy(fromAuth = auth))
      arg[String]("toUrl")
        .text("Url for StarChat to migrate to")
        .action((url, params) => params.copy(toUrl = url))
      arg[String]("toAuth")
        .text("Authorizaion for the StarChat to migrate to")
        .action((auth, params) => params.copy(toAuth = auth))
      help("help").text("prints this usage text")
    }
    parser.parse(args, defaultParams) match {
      case Some(params) =>
        execute(params)
      case _ =>
    }
  }

}
