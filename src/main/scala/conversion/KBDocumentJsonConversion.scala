package conversion

import entities.v4.KBDocument
import entities.v5.{QADocument, QADocumentAnnotations, QADocumentCore}
import play.api.libs.json.{JsValue, Json}

trait KBDocumentJsonConversion {
  implicit val kbDocumentReads = Json.reads[KBDocument]
  implicit val qaDocumentAnnotationsWrites = Json.writes[QADocumentAnnotations]
  implicit val qaDocumentCoreWrites = Json.writes[QADocumentCore]
  implicit val qaDocumentWrites = Json.writes[QADocument]

  implicit def convert(document: QADocument): JsValue = Json.toJson(document)
}
