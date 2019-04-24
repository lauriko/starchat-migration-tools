package entities.v4

case class SearchDTDocument(score: Float, document: DTDocument)

case class SearchDTDocumentsResults(total: Int, max_score: Float, hits: List[SearchDTDocument])
