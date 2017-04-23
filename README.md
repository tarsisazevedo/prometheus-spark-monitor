PrometheusSparkMetrics class to instrument spark jobs.
## How to use it

  $ streamingContext.addStreamingListener(new PrometheusSparkMetrics(streamingContext.sparkContext.appName))
