import org.junit.Assert
import org.junit.Test

class TestPrometheusSparkMetrics {
  @Test
  def testShouldSum(): Unit ={
    Assert.assertEquals(1, 1)
  }

  @Test
  def testShouldNotSum(): Unit ={
    Assert.assertEquals(1, 2)
  }
}
