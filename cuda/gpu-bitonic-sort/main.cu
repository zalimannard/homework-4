#define DEBUG

#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>

#define ARR_SIZE 100000
#define BLOCK_SIZE 1024

__global__ void sort (long long* data)
{
    long long arrSizeCopy = ARR_SIZE;
    long long operations = 0;
    while (arrSizeCopy > 0) {
        arrSizeCopy = arrSizeCopy >> 1;
        ++operations;
    }
    long long fakeArrSize = 1 << operations;

    for (int i = 0; i < operations; ++i) {
        long long rectSize = 1 << (i + 1);
        long long halfRectSize = rectSize >> 1;

        while (rectSize > 1) {

            for (int iElement = threadIdx.x; iElement < fakeArrSize; iElement += BLOCK_SIZE) {
                // -1 - смотрит в начало, 1 - в конец
                int direction = -1;
                if ((iElement / rectSize) % 2 == 0) {
                    direction = 1;
                }

                // 0 - половина большая к началу, 1 - к концу
                int half = 1;
                if (iElement % rectSize < rectSize / 2) {
                    half = 0;
                }

                if ((direction == 1) && (half == 0)) {
                    if ((iElement < ARR_SIZE) && (iElement + halfRectSize < ARR_SIZE)) {
                        if (data[iElement] > data[iElement + halfRectSize]) {
                            long long tmp = data[iElement + halfRectSize];
                            data[iElement + halfRectSize] = data[iElement];
                            data[iElement] = tmp;
                        }
                    }
                }

                if ((direction == -1) && (half == 1)) {
                    if ((iElement < ARR_SIZE) && (iElement - halfRectSize < ARR_SIZE)) {
                        if (data[iElement] > data[iElement - halfRectSize]) {
                            long long tmp = data[iElement - halfRectSize];
                            data[iElement - halfRectSize] = data[iElement];
                            data[iElement] = tmp;
                        }
                    }
                }

            }

            __syncthreads();

            rectSize = rectSize >> 1;
            halfRectSize = rectSize >> 1;
        }
    }




    long long operationOnThread = ARR_SIZE / BLOCK_SIZE / 2 + 1;

    for (long long i = 0; i < ARR_SIZE / 2 + 1; ++i) {

        for (long long iOperation = 0; iOperation < operationOnThread; ++iOperation) {
            long long realIndex = (BLOCK_SIZE * iOperation + threadIdx.x) * 2;
            long long nextIndex = realIndex + 1;
            if (nextIndex < ARR_SIZE) {
                if (data[realIndex] > data[nextIndex]) {
                    long long tmp = data[realIndex];
                    data[realIndex] = data[nextIndex];
                    data[nextIndex] = tmp;
                }
            }
        }

        __syncthreads();

        for (long long iOperation = 0; iOperation < operationOnThread; ++iOperation) {
            long long realIndex = (BLOCK_SIZE * iOperation + threadIdx.x) * 2 + 1;
            long long nextIndex = realIndex + 1;
            if (nextIndex < ARR_SIZE) {
                if (data[realIndex] > data[nextIndex]) {
                    long long tmp = data[realIndex];
                    data[realIndex] = data[nextIndex];
                    data[nextIndex] = tmp;
                }
            }
        }

        __syncthreads();

    }
}

int main()
{
    long long arr[1000000];

    FILE* f = fopen("../input.txt", "rt");
    long long readIndex = 0;
    long long temp = 0;
    while (fscanf(f, "%lld", &temp) == 1) {
        arr[readIndex] = temp;
        ++readIndex;
    }

    #if defined(DEBUG)
        printf("\nИзначальный массив:\n");
        printf("Размер массива: %lld\n", ARR_SIZE);
        long long sizeForPrintf = 100;
        if (sizeForPrintf > ARR_SIZE) {
            sizeForPrintf = ARR_SIZE;
        }
        printf("Массив/Первые 100 его элементов:\n");
        for (long long i = 0; i < sizeForPrintf; ++i) {
            printf("%lld ", arr[i]);
        }
        printf("\n");
    #endif

    long long* array;
    cudaMalloc(&array, ARR_SIZE * sizeof(long long));

    cudaMemcpy(array, arr, ARR_SIZE * sizeof(long long), cudaMemcpyHostToDevice);


    cudaEvent_t start, stop;
    float milliseconds;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start, 0);


    sort<<< 1, BLOCK_SIZE >>>(array);
    cudaMemcpy(arr, array, ARR_SIZE * sizeof(long long), cudaMemcpyDeviceToHost);

    cudaEventRecord(stop,0);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&milliseconds, start, stop);
    double seconds = (double) milliseconds / 1000;
    printf("Заняло %f секунд\n", seconds);
    cudaEventDestroy(start);
    cudaEventDestroy(stop);


    #if defined(DEBUG)
        printf("\nОтсортированный массив:\n");
        printf("Массив/Первые 100 его элементов:\n");
        for (long long i = 0; i < sizeForPrintf; ++i) {
            printf("%lld ", arr[i]);
        }
        printf("\n");
        printf("Потребовалось операций: Потом будет\n");
    #endif

    return 0;
}
