package conversion

import entities.v4.KBDocument
import entities.v5.{Doctypes, QADocument, QADocumentAnnotations, QADocumentCore}

trait KBDocumentVersionConversion {

  implicit def convert(document: KBDocument): QADocument = QADocument(
    id = document.id,
    conversation = document.conversation,
    indexInConversation = document.index_in_conversation.getOrElse(1),
    coreData = Some(QADocumentCore(
      question = Some(document.question),
      questionNegative = document.question_negative,
      questionScoredTerms = document.question_scored_terms,
      answer = Some(document.answer),
      answerScoredTerms = document.answer_scored_terms,
      topics = document.topics,
      verified = Some(document.verified)
    )),
    annotations = Some(QADocumentAnnotations(
      dclass = document.dclass,
      doctype = Some(Doctypes.value(document.doctype)),
      state = document.state
    ))
  )

}
