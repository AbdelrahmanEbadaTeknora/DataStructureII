import random
import time
import copy
import matplotlib.pyplot as plt

def bubble_sort(arr):
    n = len(arr)
    for i in range(n - 1):
        for j in range(n - 1 - i):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    return arr

def selection_sort(arr):
    n = len(arr)
    for i in range(n - 1):
        min_idx = i
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr

def insertion_sort(arr):
    n = len(arr)
    for i in range(1, n):
        key = arr[i]
        j = i - 1
        while j >= 0 and arr[j] > key:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key
    return arr

def generate_random_array(size):
    return [random.randint(0, 1000000) for _ in range(size)]

def measure_time(sort_func, arr):
    arr_copy = copy.deepcopy(arr)
    start = time.time()
    sort_func(arr_copy)
    end = time.time()
    return (end - start) * 1000

sizes = [10000, 25000, 50000, 75000, 100000]

bubble_times = []
selection_times = []
insertion_times = []

for size in sizes:
    print(f"\nArray Size: {size}")

    arr = generate_random_array(size)

    t = measure_time(bubble_sort, arr)
    bubble_times.append(t)
    print(f"Running time for Bubble Sort is {t:.2f} ms")

    t = measure_time(selection_sort, arr)
    selection_times.append(t)
    print(f"Running time for Selection Sort is {t:.2f} ms")

    t = measure_time(insertion_sort, arr)
    insertion_times.append(t)
    print(f"Running time for Insertion Sort is {t:.2f} ms")

plt.figure(figsize=(10, 6))
plt.plot(sizes, bubble_times, marker='o', label='Bubble Sort')
plt.plot(sizes, selection_times, marker='o', label='Selection Sort')
plt.plot(sizes, insertion_times, marker='o', label='Insertion Sort')
plt.title("Time (milliseconds) vs Array Size for Sorting Algorithms")
plt.xlabel("Input Size (N)")
plt.ylabel("Execution Time (milliseconds)")
plt.legend()
plt.grid(True)
plt.xticks(sizes, [f"{s:,}" for s in sizes])
plt.tight_layout()
plt.savefig("sorting_comparison_graph.png", dpi=150)
plt.show()
