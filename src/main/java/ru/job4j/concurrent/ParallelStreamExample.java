package ru.job4j.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ParallelStreamExample {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> stream = list.parallelStream();
        System.out.println(stream.isParallel());
        Optional<Integer> multiplication = stream.reduce((left, right) -> left * right);
        System.out.println(multiplication.get());
        /*из параллельного потока получаем последовательный*/
        var sequential = stream.sequential();
        System.out.println(sequential.isParallel());
        /*выполнение потока в многопоточной среде по умолчанию не гарантирует сохранения порядка следования элементов*/
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5);
        list2.stream().parallel().peek(System.out::println).toList();
        /*или с методом foreach*/
        list2.stream().parallel().forEach(System.out::println);
        /*Для сохранения порядка следования элементов можно воспользоваться методом*/
        list2.stream().parallel().forEachOrdered(System.out::println);
    }
}
