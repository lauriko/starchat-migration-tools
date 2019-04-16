package entities.v4

import scala.collection.immutable.List

case class SearchDTDocument(score: Float, document: DTDocument)

case class SearchDTDocumentsResults(total: Int, max_score: Float, hits: List[SearchDTDocument])
