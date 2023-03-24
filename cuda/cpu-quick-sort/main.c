//#define FULL

#include <stdio.h>
#include <sys/time.h>

#define ARR_SIZE (long int) 1024

struct timeval tval_before, tval_after, tval_result;

long int sort(long int arr[], long int first, long int last)
{
    long int operations = 0;
    long int i = first;
    long int j = last;
    long int pivot = last;
    long int tmp = 0;

    if (first < last) {
        while (i < j) {
            #if defined(FULL)
                ++operations;
            #endif
            while (arr[i] <= arr[pivot] && i < last) {
                i++;
            }
            #if defined(FULL)
                operations += 2;
            #endif
            while (arr[j] > arr[pivot]) {
                j--;
                #if defined(FULL)
                    ++operations;
                #endif
            }
            #if defined(FULL)
                ++operations;
            #endif
            if (i < j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                #if defined(FULL)
                    operations += 3;
                #endif
            }
            #if defined(FULL)
                ++operations;
            #endif
        }
        tmp = arr[pivot];
        arr[pivot] = arr[j];
        arr[j] = tmp;
        #if defined(FULL)
            operations += 3;
        #endif
        operations += sort(arr, first, j - 1);
        operations += sort(arr, j + 1, last);
    }
    return operations;
}

int main(void)
{
    printf("Быстрая сортировка на CPU\n");
    printf("Размер массива: %ld\n", ARR_SIZE);

    #if defined(FULL)
        printf("Запуск в медленном режиме\n\n");
    #else
        printf("Запуск в быстром режиме\n\n");
    #endif

    long int arr[ARR_SIZE];

    FILE* f = fopen("../input.txt", "rt");
    long int readIndex = 0;
    long int temp = 0;
    while (fscanf(f, "%ld", &temp) == 1) {
        arr[readIndex] = temp;
        ++readIndex;
    }

    #if defined(FULL)
        printf("\nИзначальный массив:\n");
        long int sizeForPrintf = 100;
        if (sizeForPrintf > ARR_SIZE) {
            sizeForPrintf = ARR_SIZE;
        }
        printf("Массив/Первые 100 его элементов:\n");
        for (long int i = 0; i < sizeForPrintf; ++i) {
            printf("%ld ", arr[i]);
        }
        printf("\n");
    #endif

    gettimeofday(&tval_before, NULL);

    long int operations = sort(arr, 0, ARR_SIZE - 1);

    gettimeofday(&tval_after, NULL);

    #if defined(FULL)
        printf("\nОтсортированный массив:\n");
        printf("Массив/Первые 100 его элементов:\n");
        for (long int i = 0; i < sizeForPrintf; ++i) {
            printf("%ld ", arr[i]);
        }
        printf("\n");
        printf("Потребовалось операций: %ld\n", operations);
    #endif

    timersub(&tval_after, &tval_before, &tval_result);
    printf("Заняло %ld.%06ld секунд\n", (long int) tval_result.tv_sec, (long int) tval_result.tv_usec);

    return 0;
}

