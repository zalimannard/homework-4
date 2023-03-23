#define DEBUG

#include <stdio.h>
#include <sys/time.h>

struct timeval tval_before, tval_after, tval_result;

long long sort(long long size, long long arr[]) {
    long long operations = 0;
    for (long long i = 0; i < size - 1; ++i) {
        #if defined(DEBUG)
            ++operations;
        #endif
        for (long long j = 0; j < size - 1; ++j) {
            #if defined(DEBUG)
                ++operations;
            #endif
            if (arr[j] > arr[j + 1]) {
                long long tmp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = tmp;
                #if defined(DEBUG)
                    operations += 3;
                #endif
            }
            #if defined(DEBUG)
                ++operations;
            #endif
        }
    }
    return operations;
}

int main(void)
{
    printf("Сортировка пузырьком на CPU\n\n");

    #if defined(DEBUG)
        printf("Запуск в режиме отладки\n");
    #else
        printf("Запуск в обычном режиме\n");
    #endif

    long long arr[] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0  };

    long long arrSize = sizeof(arr) / sizeof(arr[0]);

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
        printf("\n");
    #endif

    gettimeofday(&tval_before, NULL);

    long long operations = sort(arrSize, arr);

    gettimeofday(&tval_after, NULL);

    #if defined(DEBUG)
        printf("\nОтсортированный массив:\n");
        printf("Массив/Первые 100 его элементов:\n");
        for (long long i = 0; i < sizeForPrintf; ++i) {
            printf("%lld ", arr[i]);
        }
        printf("\n");
        printf("Потребовалось операций: %lld\n", operations);
    #endif

    timersub(&tval_after, &tval_before, &tval_result);
    printf("Заняло %ld.%06ld секунд\n", (long int)tval_result.tv_sec, (long int)tval_result.tv_usec);

    return 0;
}

