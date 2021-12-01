package org.jetbrains

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.*
import java.util.concurrent.*


/*
 * Benchmark                           Mode  Cnt   Score   Error   Units
 * EnumListBenchmark.getKotlin        thrpt   20  42.080 ± 1.275  ops/us
 * EnumListBenchmark.getLazy          thrpt   20  44.312 ± 1.712  ops/us
 * EnumListBenchmark.iterationKotlin  thrpt   20  48.040 ± 0.383  ops/us
 * EnumListBenchmark.iterationLazy    thrpt   20  42.150 ± 2.071  ops/us
 */
@Fork(2)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
open class EnumListBenchmark {

    private val kotlinList: List<TestEnum> = buildList(TestEnum.values().size) { addAll(TestEnum.values()) }
    private val lazyList: List<TestEnum> = TestEnum.buildLazyList()
    private val volatileList: List<TestEnum> = TestEnum.buildVolatileList()
    private val plainArray = TestEnum.VALUES_TO_CAPTURE;
    private val size = TestEnum.values().size


    private fun consumeList(list: List<TestEnum>, bh: Blackhole) {
        for (testEnum in list) {
            bh.consume(testEnum)
        }
    }

    private fun consumeGet(list: List<TestEnum>, bh: Blackhole) {
        for (i in 0 until size) {
            bh.consume(list.get(i))
        }
    }

    @Benchmark
    fun iterationKotlin(bh: Blackhole) {
        consumeList(kotlinList, bh)
    }

    @Benchmark
    fun iterationLazy(bh: Blackhole) {
        consumeList(lazyList, bh)
    }

    @Benchmark
    fun iterationVolatile(bh: Blackhole) {
        consumeList(volatileList, bh)

    }

    @Benchmark
    fun iterationArray(bh: Blackhole) {
        for (testEnum in plainArray) {
            bh.consume(testEnum)
        }
    }

    @Benchmark
    fun getKotlin(bh: Blackhole) {
        consumeGet(kotlinList, bh)
    }

    @Benchmark
    fun getLazy(bh: Blackhole) {
        consumeGet(lazyList, bh)
    }

    @Benchmark
    fun getVolatile(bh: Blackhole) {
        consumeGet(volatileList, bh)
    }

    @Benchmark
    fun getArray(b: Blackhole) {
        for (i in 0 until size) {
            b.consume(plainArray.get(i))
        }
    }
}
