import org.apache.spark.streaming.scheduler.StreamingListener
import org.apache.spark.streaming.scheduler.StreamingListenerBatchCompleted

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Gauge
import io.prometheus.client.exporter.PushGateway

class PrometheusSparkMetrics(sparkJob: String) extends StreamingListener {

  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    val registry: CollectorRegistry = new CollectorRegistry()
    val pushGateway: PushGateway = new PushGateway("pushgaiti.gcloud.globoi.com")
    addInputRate(batchCompleted, registry)
    addSchedulingDelay(batchCompleted, registry)
    addProcessingTime(batchCompleted, registry)
    addTotalDelay(batchCompleted, registry)
    pushGateway.push(registry, "spark_streaming_exporter")
  }

  def addInputRate(batchCompleted: StreamingListenerBatchCompleted, registry: CollectorRegistry): Unit = {
    addMetric(registry, batchCompleted.batchInfo.numRecords, "spark_streaming_input_rate_seconds", "The input rate of our spark streaming job")
  }

  def addSchedulingDelay(batchCompleted: StreamingListenerBatchCompleted, registry: CollectorRegistry) = {
    addMetric(registry, batchCompleted.batchInfo.schedulingDelay.get, "spark_streaming_scheduling_delay_seconds", "The scheduling delay of our spark streaming job")
  }
  def addProcessingTime(batchCompleted: StreamingListenerBatchCompleted, registry: CollectorRegistry) = {
    addMetric(registry, batchCompleted.batchInfo.processingDelay.get, "spark_streaming_processing_time_seconds", "The processing delay of our spark streaming job")
  }

  def addTotalDelay(batchCompleted: StreamingListenerBatchCompleted, registry: CollectorRegistry) = {
    addMetric(registry, batchCompleted.batchInfo.totalDelay.get, "spark_streaming_total_delay_seconds", "The total delay of our spark streaming job")
  }

  def addMetric(registry: CollectorRegistry, value: Double, name: String, helpText: String): Unit = {
    val totalDelay: Gauge = Gauge.build()
      .help(helpText)
      .name(name)
      .labelNames("spark_job")
      .register(registry)
    val seconds = value/1000
    totalDelay.labels(sparkJob).set(seconds)
  }
}
