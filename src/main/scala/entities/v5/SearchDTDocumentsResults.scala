package entities.v5

case class SearchDTDocument(score: Float, document: DTDocument)

case class SearchDTDocumentsResults(total: Int, maxScore: Float, hits: List[SearchDTDocument])

