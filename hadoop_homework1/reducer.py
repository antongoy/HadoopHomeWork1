#!/usr/bin/env python
from __future__ import print_function

import sys
import math

__author__ = 'anton-goy'


def mean(iterable):
    n_elements = len(iterable)

    mean_value = 0.0
    for item in iterable:
        mean_value += item

    return mean_value / n_elements


def deviation(iterable, mean_value):
    n_elements = len(iterable)

    std_deviation_value = 0.0
    for item in iterable:
        std_deviation_value += (item - mean_value) ** 2

    return math.sqrt(std_deviation_value / n_elements)


def median(iterable):
    sorted_values = sorted(iterable)

    if len(sorted_values) % 2 == 0:
        index2 = len(sorted_values) / 2
        index1 = index2 - 1
        return (sorted_values[index1] + sorted_values[index2]) / 2.0
    else:
        index = len(sorted_values) / 2
        return float(sorted_values[index])


def aggregate(counter_values):
    if not counter_values:
        return 0, None, None, None, None, None

    occurrences = [value for _, value in counter_values.items()]

    num_of_unique = len(counter_values)
    num_of_patents_min = min(occurrences)
    num_of_patents_median = median(occurrences)
    num_of_patents_max = max(occurrences)
    num_of_patents_mean = mean(occurrences)
    num_of_patents_deviation = deviation(occurrences, num_of_patents_mean)

    return (num_of_unique,
            num_of_patents_min,
            num_of_patents_median,
            num_of_patents_max,
            num_of_patents_mean,
            num_of_patents_deviation)


def counter(iterable):
    occurrences_dict = {}

    for item in iterable:
        if item in occurrences_dict:
            occurrences_dict[item] += 1
        else:
            occurrences_dict[item] = 1

    return occurrences_dict


def main():
    value_list = []
    current_key = None

    for line in sys.stdin:
        line = line.strip('\n')
        key, value = line.split('\t')

        if current_key is None:
            current_key = key

        if key != current_key:
            value_counter = counter(value_list)
            aggregate_values = aggregate(value_counter)
            print(current_key, *aggregate_values, sep='\t')

            current_key = key
            value_list = []

        value_list.append(value)
    else:
        value_counter = counter(value_list)
        aggregate_values = aggregate(value_counter)
        print(current_key, *aggregate_values, sep='\t')


if __name__ == '__main__':
    main()