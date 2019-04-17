package command

import conversion.{KBDocumentJsonConversion, KBDocumentVersionConversion}
import entities.v4.KBDocument
import entities.v5.QADocument
import play.api.libs.json._
import scalaj.http._
import scopt.OptionParser

object MigrateKnowledgebase extends KBDocumentVersionConversion with KBDocumentJsonConversion {

  private[this] case class Params(fromUrl: String = "",
                                  fromAuth: String = "",
                                  toUrl: String = "",
                                  toAuth: String = ""
                                 )



  def readDocs(params: Params): List[KBDocument]= {
    val response = Http(params.fromUrl + "/stream/knowledgebase")
      .headers(Seq(("Authorization", "Basic " + params.fromAuth), ("Content-Type", "application/json")))
      .asString
    response.body.split("\n").map(Json.parse(_).as[KBDocument]).toList
  }

  def writeDocs(params: Params, documents: List[QADocument]) =  {
    val requests = documents.map( document => Http(params.toUrl + "/knowledgebase")
      .headers(Seq(("Authorization", "Basic " + params.toAuth), ("Content-Type", "application/json")))
      .postData(document.as[JsValue].toString))
    requests.map(_.asString)
  }

  private[this] def execute(params: Params) {
    val kbDocuments = readDocs(params)
    val qaDocuments = kbDocuments.map(_.as[QADocument])
    val responses = writeDocs(params, qaDocuments)
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
