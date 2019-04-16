package conversion
import entities.v4
import entities.v5
import play.api.libs.json.{JsValue, Json}

trait DTDocumentJsonConversion {

  implicit val dtDocumentReads4 = Json.reads[v4.DTDocument]
  implicit val searchDTDocumentReads4 = Json.reads[v4.SearchDTDocument]
  implicit val searchDTDocumentsResultsReads4 = Json.reads[v4.SearchDTDocumentsResults]
  implicit val dtDocumentWrites4 = Json.writes[v4.DTDocument]

  implicit val dtDocumentReads5 = Json.reads[v5.DTDocument]
  implicit val searchDTDocumentReads5 = Json.reads[v5.SearchDTDocument]
  implicit val searchDTDocumentsResultsReads5 = Json.reads[v5.SearchDTDocumentsResults]
  implicit val dtDocumentWrites5 = Json.writes[v5.DTDocument]

  implicit def convertToJson(document: v4.DTDocument): JsValue = Json.toJson(document)
  implicit def convertToJson(document: v5.DTDocument): JsValue = Json.toJson(document)

}
