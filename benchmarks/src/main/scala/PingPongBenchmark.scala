import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Control

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Group)
class PingPongBenchmark {
  val Message = 1
  val gate: AtomicLong = new AtomicLong(0)

  @Setup
  def setup(): Unit = {
    gate.set(0)
  }

  @Benchmark
  @Group("CAS")
  @GroupThreads(1)
  def pingCas(control: Control): Unit = {
    while (!control.stopMeasurement && !gate.compareAndSet(0, 1)) { }
  }

  @Benchmark
  @Group("CAS")
  @GroupThreads(1)
  def pongCas(control: Control): Unit = {
    while (!control.stopMeasurement && !gate.compareAndSet(1, 0)) { }
  }

  @Benchmark
  @Group("Strict")
  @GroupThreads(1)
  def ping(control: Control): Unit = {
    while (!control.stopMeasurement && gate.get() != 0) { }
    gate.set(1)
  }

  @Benchmark
  @Group("Strict")
  @GroupThreads(1)
  def pong(control: Control): Unit = {
    while (!control.stopMeasurement && gate.get() != 1) { }
    gate.set(0)
  }

  @Benchmark
  @Group("Lazy")
  @GroupThreads(1)
  def lazyPing(control: Control): Unit = {
    while (!control.stopMeasurement && gate.get() != 0) { }
    gate.lazySet(1)
  }

  @Benchmark
  @Group("Lazy")
  @GroupThreads(1)
  def lazyPong(control: Control): Unit = {
    while (!control.stopMeasurement && gate.get() != 1) { }
    gate.lazySet(0)
  }
}
