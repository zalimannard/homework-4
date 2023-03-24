//#define FULL

#include <stdio.h>
#include <sys/time.h>

#define ARR_SIZE (long int) 1000

struct timeval tval_before, tval_after, tval_result;

long int sort(long int arr[]) {
    long int operations = 0;
    long int tmp = 0;

    for (long int i = 0; i < ARR_SIZE - 1; ++i) {
        #if defined(FULL)
            operations += 2;
        #endif
        for (long int j = 0; j < ARR_SIZE - 1; ++j) {
            #if defined(FULL)
                operations += 2;
            #endif
            if (arr[j] > arr[j + 1]) {
                tmp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = tmp;
                #if defined(FULL)
                    operations += 3;
                #endif
            }
            #if defined(FULL)
                ++operations;
            #endif
        }
    }

    return operations;
}

int main(void)
{
    printf("Сортировка пузырьком на CPU\n");
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

    long int operations = sort(arr);

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
