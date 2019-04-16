package command

import scopt.OptionParser
import scalaj.http._
import entities.{v4, v5}
import conversion.DTDocumentJsonConversion
import conversion.DTDocumentVersionConversion
import play.api.libs.json._

object MigrateDecisionTable extends DTDocumentVersionConversion with DTDocumentJsonConversion {

  private[this] case class Params(fromUrl: String = "",
                                  fromAuth: String = "",
                                  toUrl: String = "",
                                  toAuth: String = ""
                                 )


  def readDocsFrom(params: Params): List[v4.DTDocument] = {
    val response: HttpResponse[String] = Http(params.fromUrl+"/decisiontable?dump=true")
      .headers(Seq(("Authorization", "Basic "+params.fromAuth)))
      .asString
    val dtDocumentResults = Json.parse(response.body).as[v4.SearchDTDocumentsResults]
    dtDocumentResults.hits.map(_.document)
  }


  def writeDocsTo(params: Params, documents: List[v5.DTDocument]) = {
    val requests = documents.map( document => Http(params.toUrl+"/decisiontable")
      .postData(document.as[JsValue].toString)
      .headers(Seq(("Authorization", "Basic " + params.toAuth), ("Content-Type", "application/json")))
    )
    requests.map(_.asString)
  }

  private[this] def execute(params: Params) {
    println(params)
    println("reading from " + params.fromUrl)
    val v4Documents = readDocsFrom(params)
    println("converting documents")
    val v5Documents = v4Documents.map(_.as[v5.DTDocument])
    println("posting documents to " + params.toUrl)
    val responses = writeDocsTo(params, v5Documents)
    val (succeed, failed) = responses.foldLeft((0,0))(
      (b, response) =>
        if(response.is2xx) (b._1+1, b._2)
        else (b._1, b._2+1))
    println(f"Posted $succeed documents successfully and $failed failed")
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
