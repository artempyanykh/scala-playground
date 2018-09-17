import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.{AtomicLong, AtomicReferenceArray}

import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Array(Mode.AverageTime))
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
class CASBenchmark {
  val atomicLong: AtomicLong = new AtomicLong(0)
  val atomicReferenceArray: AtomicReferenceArray[Int] = new AtomicReferenceArray[Int](1024)

//  @Benchmark
  def baseline(): Unit = {
    ()
  }

  @Benchmark
  def setLong(): Unit = {
    atomicLong.set(1L)
  }

  @Benchmark
  def lazySetLong(): Unit = {
    atomicLong.lazySet(1L)
  }

  @Benchmark
  def casLong(): Boolean = {
    atomicLong.compareAndSet(0L, 1L)
  }

  @Benchmark
  def casArray(): Boolean = {
    atomicReferenceArray.compareAndSet(512, 0, 1)
  }
}

