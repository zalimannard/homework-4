//#define FULL

#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>

#define ARR_SIZE 1000
#define BLOCK_SIZE 1024

__global__ void sort (long int* data, unsigned long long* operations)
{
    long int operationOnThread = ARR_SIZE / BLOCK_SIZE / 2 + 1;

    for (long int i = 0; i < ARR_SIZE / 2 + 1; ++i) {
        #if defined(FULL)
            atomicAdd(operations, (unsigned long long) 2);
        #endif
        for (long int iOperation = 0; iOperation < operationOnThread; ++iOperation) {
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 2);
            #endif
            long int realIndex = (BLOCK_SIZE * iOperation + threadIdx.x) * 2;
            long int nextIndex = realIndex + 1;
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 5);
            #endif
            if (nextIndex < ARR_SIZE) {
                if (data[realIndex] > data[nextIndex]) {
                    long int tmp = data[realIndex];
                    data[realIndex] = data[nextIndex];
                    data[nextIndex] = tmp;
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 1);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 1);
                #endif
            }
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 1);
            #endif
        }

        __syncthreads();

        for (long int iOperation = 0; iOperation < operationOnThread; ++iOperation) {
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 2);
            #endif
            long int realIndex = (BLOCK_SIZE * iOperation + threadIdx.x) * 2 + 1;
            long int nextIndex = realIndex + 1;
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 2);
            #endif
            if (nextIndex < ARR_SIZE) {
                if (data[realIndex] > data[nextIndex]) {
                    long int tmp = data[realIndex];
                    data[realIndex] = data[nextIndex];
                    data[nextIndex] = tmp;
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 3);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 1);
                #endif
            }
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 1);
            #endif
        }

        __syncthreads();

    }
}

int main()
{
    printf("Параллельная сортировка пузырьком на GPU\n");
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

    long int* array;
    cudaMalloc(&array, ARR_SIZE * sizeof(long int));
    cudaMemcpy(array, arr, ARR_SIZE * sizeof(long int), cudaMemcpyHostToDevice);

    unsigned long long operations = 0;
    unsigned long long* dev_operations;
    int size = sizeof(operations);
    cudaMalloc((void**) &dev_operations, size);
    cudaMemcpy(dev_operations, &operations, size, cudaMemcpyHostToDevice);

    cudaEvent_t start, stop;
    float milliseconds;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start, 0);

    sort<<< 1, BLOCK_SIZE >>>(array, dev_operations);
    cudaMemcpy(arr, array, ARR_SIZE * sizeof(long int), cudaMemcpyDeviceToHost);
    cudaMemcpy(&operations, dev_operations, size, cudaMemcpyDeviceToHost);

    cudaEventRecord(stop,0);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&milliseconds, start, stop);

    #if defined(FULL)
        printf("\nОтсортированный массив:\n");
        printf("Массив/Первые 100 его элементов:\n");
        for (long int i = 0; i < sizeForPrintf; ++i) {
            printf("%ld ", arr[i]);
        }
        printf("\n");
        printf("Потребовалось операций: %lld\n", operations);
    #endif

    double seconds = (double) milliseconds / 1000;
    printf("Заняло %f секунд\n", seconds);
    cudaEventDestroy(start);
    cudaEventDestroy(stop);

    return 0;
}
