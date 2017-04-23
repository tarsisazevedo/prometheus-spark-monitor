PrometheusSparkMetrics class to instrument spark jobs.
## How to use it

```scala
  streamingContext.addStreamingListener(new PrometheusSparkMetrics(streamingContext.sparkContext.appName))
```
