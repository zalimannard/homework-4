#define DEBUG

#include <stdio.h>
#include <sys/time.h>

struct timeval tval_before, tval_after, tval_result;

long long sort(long long arr[], long long first, long long last) {
    long long operations = 0;
    long long pivot = first;
    long long i = first;
    long long j = last;

    if (first < last) {
        while (i < j) {
            #if defined(DEBUG)
                ++operations;
            #endif

            while ((arr[i] <= arr[pivot]) && (i < last)) {
                ++i;
                #if defined(DEBUG)
                    operations += 2;
                #endif
            }

            while (arr[j] > arr[pivot]) {
                --j;
                #if defined(DEBUG)
                    operations += 2;
                #endif
            }
            if (i < j) {
                long long tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                #if defined(DEBUG)
                    operations += 4;
                #endif
            }
        }
        long long tmp = arr[pivot];
        arr[pivot] = arr[j];
        arr[j] = tmp;
        #if defined(DEBUG)
            operations += 4;
        #endif
        operations += sort(arr, first, j - 1);
        operations += sort(arr, j + 1, last);
    }

    return operations;
}

int main(void)
{
    printf("Быстрая сортировка на CPU\n\n");

    #if defined(DEBUG)
        printf("Запуск в режиме отладки\n");
    #else
        printf("Запуск в обычном режиме\n");
    #endif

    long long arr[1000000];

    FILE* f = fopen("../input.txt", "rt");
    long long readIndex = 0;
    long long temp = 0;
    while (fscanf(f, "%lld", &temp) == 1) {
        arr[readIndex] = temp;
        ++readIndex;
    }

    long long arrSize = sizeof(arr)/sizeof(arr[0]);

    #if defined(DEBUG)
        printf("\nИзначальный массив:\n");
        printf("Размер массива: %lld\n", arrSize);
        long long sizeForPrintf = 100;
        if (sizeForPrintf > arrSize) {
            sizeForPrintf = arrSize;
        }
        printf("Массив/Первые 100 его элементов:\n");
        for (long long i = 0; i < sizeForPrintf; ++i) {
            printf("%lld ", arr[i]);
        }
    #endif

    gettimeofday(&tval_before, NULL);

    long long operations = sort(arr, 0, arrSize - 1);

    gettimeofday(&tval_after, NULL);

    #if defined(DEBUG)
        printf("\nОтсортированный массив:\n");
        printf("Массив/Первые 100 его элементов:\n");
        for (long long i = 0; i < sizeForPrintf; ++i) {
            printf("%lld ", arr[i]);
        }
        printf("Потребовалось операций: %lld\n", operations);
    #endif

    timersub(&tval_after, &tval_before, &tval_result);
    printf("Заняло %ld.%06ld секунд\n", (long int)tval_result.tv_sec, (long int)tval_result.tv_usec);

    return 0;
}

