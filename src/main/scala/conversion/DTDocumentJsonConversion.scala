package conversion
import entities.{DTDocument, SearchDTDocument, SearchDTDocumentsResults}
import play.api.libs.json.Json

trait DTDocumentJsonConversion {

  implicit val dtDocumentReads = Json.reads[DTDocument]
  implicit val searchDTDocumentReads = Json.reads[SearchDTDocument]
  implicit val searchDTDocumentsResultsReads = Json.reads[SearchDTDocumentsResults]
  implicit val dtDocumentWrites = Json.writes[DTDocument]

}
