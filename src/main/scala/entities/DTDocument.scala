package entities

import scala.collection.immutable.{List, Map}

case class DTDocument(state: String,
                               executionOrder: Int,
                               maxStateCount: Int,
                               analyzer: String,
                               queries: List[String],
                               bubble: String,
                               action: String,
                               actionInput: Map[String, String],
                               stateData: Map[String, String],
                               successValue: String,
                               failureValue: String,
                               evaluationClass: Option[String] = Some("default"),
                               version: Option[Long] = Some(0L)
                     )

