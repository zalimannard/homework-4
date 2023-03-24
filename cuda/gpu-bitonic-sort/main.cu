#define FULL

#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>

#define ARR_SIZE (long int) 1024
#define BLOCK_SIZE 1024

__global__ void sort (long int* data, unsigned long long* operations)
{
    long int arrSizeCopy = ARR_SIZE;
    int iterations = 0;
    while (arrSizeCopy > 0) {
        arrSizeCopy = arrSizeCopy >> 1;
        ++iterations;
    }
    long int fakeArrSize = 1 << iterations;
    int direction = 0;
    int half = 0;
    long int tmp = 0;

    for (int i = 0; i < iterations; ++i) {
        #if defined(FULL)
            atomicAdd(operations, (unsigned long long) 2);
        #endif
        long int rectSize = 1 << (i + 1);
        long int halfRectSize = rectSize >> 1;

        long int stableRectSize = rectSize;
        while (rectSize > 1) {
            #if defined(FULL)
                atomicAdd(operations, (unsigned long long) 1);
            #endif
            for (long int iElement = threadIdx.x; iElement < fakeArrSize; iElement += BLOCK_SIZE) {
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 2);
                #endif
                // -1 - смотрит в начало, 1 - в конец
                direction = -1;
                if ((iElement / stableRectSize) % 2 == 0) {
                    direction = 1;
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 1);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 4);
                #endif

                // 0 - половина большая к началу, 1 - к концу
                half = 1;
                if (iElement % rectSize < rectSize / 2) {
                    half = 0;
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 1);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 4);
                #endif

                if ((direction == 1) && (half == 0)) {
                    if ((iElement < ARR_SIZE) && (iElement + halfRectSize < ARR_SIZE)) {
                        if (data[iElement] > data[iElement + halfRectSize]) {
                            tmp = data[iElement + halfRectSize];
                            data[iElement + halfRectSize] = data[iElement];
                            data[iElement] = tmp;
                            #if defined(FULL)
                                atomicAdd(operations, (unsigned long long) 3);
                            #endif
                        }
                        #if defined(FULL)
                            atomicAdd(operations, (unsigned long long) 3);
                        #endif
                    }
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 3);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 2);
                #endif

                if ((direction == -1) && (half == 1)) {
                    if ((iElement < ARR_SIZE) && (iElement - halfRectSize < ARR_SIZE)) {
                        if (data[iElement] > data[iElement - halfRectSize]) {
                            tmp = data[iElement - halfRectSize];
                            data[iElement - halfRectSize] = data[iElement];
                            data[iElement] = tmp;
                            #if defined(FULL)
                                atomicAdd(operations, (unsigned long long) 3);
                            #endif
                        }
                        #if defined(FULL)
                            atomicAdd(operations, (unsigned long long) 3);
                        #endif
                    }
                    #if defined(FULL)
                        atomicAdd(operations, (unsigned long long) 3);
                    #endif
                }
                #if defined(FULL)
                    atomicAdd(operations, (unsigned long long) 2);
                #endif
            }

            __syncthreads();

            rectSize = rectSize >> 1;
            halfRectSize = rectSize >> 1;
        }
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
        for (long long i = 0; i < sizeForPrintf; ++i) {
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
