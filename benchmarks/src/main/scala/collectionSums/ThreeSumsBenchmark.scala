package collectionSums

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
class ThreeSumsBenchmark {
  import ImplicitOps._

  @Param(Array("10", "100", "1000"))
  var sizeParam: String = _

  var intList: List[Int] = _
  var intArr: Array[Int] = _

  @Setup
  def setup(): Unit = {
    val size = sizeParam.toInt

    intList = 1.to(size).toList
    intArr = 1.to(size).toArray
  }

  @Benchmark
  def listStandardSum = {
    intList.sum
  }

  @Benchmark
  def listStandardSumInOps = {
    intList.sumOps
  }

  @Benchmark
  def listSumCustomTypeclass = {
    intList.sumCustom
  }

  @Benchmark
  def listFoldLeftTypeclass = {
    intList.foldLeft(scala.math.Numeric.IntIsIntegral.zero)(scala.math.Numeric.IntIsIntegral.plus)
  }

  @Benchmark
  //noinspection SimplifiableFoldOrReduce
  def listFoldLeftAdhoc = {
    intList.foldLeft(0)(_ + _)
  }

  @Benchmark
  def arrStandardSum = {
    intArr.sum
  }

  @Benchmark
  def arrStandardSumInOps = {
    intArr.sumOps
  }

  @Benchmark
  def arrSumCustomTypeclass = {
    intArr.sumCustom
  }

  @Benchmark
  def arrFoldLeftTypeclass = {
    intArr.foldLeft(scala.math.Numeric.IntIsIntegral.zero)(scala.math.Numeric.IntIsIntegral.plus)
  }

  //noinspection SimplifiableFoldOrReduce
  @Benchmark
  def arrFoldLeftAdhoc = {
    intArr.foldLeft(0)(_ + _)
  }
}

trait CustomNumeric[A] {
  def zero: A
  def plus(a: A, b: A): A
}

object CustomNumeric {
  implicit val myIntNumeric: CustomNumeric[Int] = new CustomNumeric[Int] {
    override def zero: Int = 0
    override def plus(a: Int, b: Int): Int = a + b
  }
}

object ImplicitOps {

  implicit class TraversableOnceOps[A](ls: TraversableOnce[A]) {
    def sumOps[B >: A](implicit num: Numeric[B]): B = ls.foldLeft(num.zero)(num.plus)
    def sumCustom[B >: A](implicit customNum: CustomNumeric[B]): B = ls.foldLeft(customNum.zero)(customNum.plus)
  }

  implicit class ArrOps[A](ls: Array[A]) {
    def sumOps[B >: A](implicit num: Numeric[B]): B = ls.foldLeft(num.zero)(num.plus)
    def sumCustom[B >: A](implicit customNum: CustomNumeric[B]): B = ls.foldLeft(customNum.zero)(customNum.plus)
  }

}

