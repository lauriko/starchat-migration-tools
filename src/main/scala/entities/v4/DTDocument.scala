package entities.v4

case class DTDocument(state: String,
                      execution_order: Int,
                      max_state_count: Int,
                      analyzer: String,
                      queries: List[String],
                      bubble: String,
                      action: String,
                      action_input: Map[String, String],
                      state_data: Map[String, String],
                      success_value: String,
                      failure_value: String,
                      version: Option[Long] = Some(0L)
                     ) {
  def as[T](implicit f: DTDocument => T): T = f(this)
}
