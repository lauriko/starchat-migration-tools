package conversion
import entities.v4
import entities.v5

trait DTDocumentVersionConversion {

  implicit def convertToVersion(document: v4.DTDocument): v5.DTDocument = v5.DTDocument(
    state = document.state,
    executionOrder = document.execution_order,
    maxStateCount = document.max_state_count,
    analyzer = document.analyzer,
    queries = document.queries,
    bubble = document.bubble,
    action = document.action,
    actionInput = document.action_input,
    stateData = document.state_data,
    successValue = document.success_value,
    failureValue = document.failure_value,
    version = document.version
  )

  implicit def convertToVersion(document: v5.DTDocument): v4.DTDocument = v4.DTDocument(
    state = document.state,
    execution_order = document.executionOrder,
    max_state_count = document.maxStateCount,
    analyzer = document.analyzer,
    queries = document.queries,
    bubble = document.bubble,
    action = document.action,
    action_input = document.actionInput,
    state_data = document.stateData,
    success_value = document.successValue,
    failure_value = document.failureValue,
    version = document.version
  )
}
