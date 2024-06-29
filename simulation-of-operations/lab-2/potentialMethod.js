//Метод наименьшей стоимости
function leastCostMethod(costMatrix, supply, demand) {
  const rows = costMatrix.length;
  const cols = costMatrix[0].length;
  const allocations = Array(rows)
    .fill(0)
    .map(() => Array(cols).fill(0));
  while (true) {
    let minCost = Infinity;
    let minRow = -1;
    let minCol = -1;

    // Находим ячейку с минимальной стоимостью
    for (let i = 0; i < rows; i++) {
      for (let j = 0; j < cols; j++) {
        if (
          allocations[i][j] === 0 &&
          costMatrix[i][j] < minCost &&
          supply[i] > 0 &&
          demand[j] > 0
        ) {
          minCost = costMatrix[i][j];
          minRow = i;
          minCol = j;
        }
      }
    }

    if (minCost === Infinity) {
      break;
    }
    const amount = Math.min(supply[minRow], demand[minCol]);

    allocations[minRow][minCol] = amount;
    supply[minRow] -= amount;
    demand[minCol] -= amount;
  }

  console.log(`Опорный план, найденный с помощью метода наименьшей стоимости:`);
  console.table(allocations);
  return allocations;
}

//Метод северозаподного угла
function northwestCornerMethod(costMatrix, supply, demand) {
  const rows = costMatrix.length;
  const cols = costMatrix[0].length;
  const allocations = Array(rows)
    .fill(0)
    .map(() => Array(cols).fill(0));

  let i = 0; // Текущая строка
  let j = 0; // Текущая колонка

  while (i < rows && j < cols) {
    const amount = Math.min(supply[i], demand[j]);

    allocations[i][j] = amount;
    supply[i] -= amount;
    demand[j] -= amount;

    if (supply[i] === 0) {
      i++;
    } else {
      j++;
    }
  }
  console.log(
    `Опорный план, найденный с помощью метода северо-заподного угла:`
  );
  console.table(allocations);
  return allocations;
}

function findPotential(allocations, costMatrix) {
  const rows = allocations.length;
  const cols = allocations[0].length;
  const u = new Array(rows);
  const v = new Array(cols);
  u.fill(null);
  v.fill(null);

  // инициализация одного из потенциалов
  u[0] = 0;

  let updated = true;

  while (updated) {
    updated = false;

    for (let i = 0; i < rows; i++) {
      for (let j = 0; j < cols; j++) {
        if (allocations[i][j] > 0) {
          if (u[i] !== null && v[j] === null) {
            v[j] = costMatrix[i][j] - u[i];
            updated = true;
          } else if (v[j] !== null && u[i] === null) {
            u[i] = costMatrix[i][j] - v[j];
            updated = true;
          }
        }
      }
    }
  }

  return { u, v };
}


function findDelta(allocations, costMatrix, u, v) {
  const delta = new Array(u.length)
    .fill(0)
    .map(() => new Array(v.length).fill(null));
  for (let i = 0; i < allocations.length; i++) {
    for (let j = 0; j < allocations[i].length; j++) {
      if (allocations[i][j] === 0) {
        delta[i][j] = v[j] + u[i] - costMatrix[i][j];
      }
    }
  }
  return delta;
}

function optimize(delta, allocations) {
  let maxCol = -1;
  let maxRow = -1;
  let maxDelta = -Infinity;

  // Найдем ячейку с максимальной дельтой
  for (let i = 0; i < delta.length; i++) {
    for (let j = 0; j < delta[i].length; j++) {
      if (delta[i][j] !== null && delta[i][j] > maxDelta) {
        maxDelta = delta[i][j];
        maxRow = i;
        maxCol = j;
      }
    }
  }

  // Если найдена ячейка с положительной дельтой
  if (maxDelta > 0) {
    findAndOptimizeCycle(allocations, maxRow, maxCol);
    return false;
  } else {
    return true;
  }
}

// Вспомогательная функция для поиска цикла и перегона. Работает только для массива размерность 3х3
function findAndOptimizeCycle(allocations, startRow, startCol) {
  if (
    (startRow == 0 && startCol == 0) ||
    (startRow == 0 && startCol == 1) ||
    (startRow == 1 && startCol == 0) ||
    (startRow == 1 && startCol == 2) ||
    (startRow == 2 && startCol == 1) ||
    (startRow == 2 && startCol == 2)
  ) {
    let zeroCounter = 0;
    if (allocations[0][0] === 0) {
      zeroCounter++;
    }
    if (allocations[0][1] === 0) {
      zeroCounter++;
    }
    if (allocations[1][0] === 0) {
      zeroCounter++;
    }
    if (allocations[1][2] === 0) {
      zeroCounter++;
    }
    if (allocations[2][1] === 0) {
      zeroCounter++;
    }
    if (allocations[2][2] === 0) {
      zeroCounter++;
    }
    if (zeroCounter === 1) {
      if (
        (startRow == 0 && startCol == 0) ||
        (startRow == 1 && startCol == 2) ||
        (startRow == 2 && startCol == 1)
      ) {
        const min = Math.min(
          allocations[1][0],
          allocations[0][1],
          allocations[2][2]
        );
        allocations[1][0] -= min;
        allocations[0][1] -= min;
        allocations[2][2] -= min;
        allocations[0][0] += min;
        allocations[1][2] += min;
        allocations[2][1] += min;
      } else {
        const min = Math.min(
          allocations[0][0],
          allocations[1][2],
          allocations[2][1]
        );
        allocations[1][0] += min;
        allocations[0][1] += min;
        allocations[2][2] += min;
        allocations[0][0] -= min;
        allocations[1][2] -= min;
        allocations[2][1] -= min;
      }
      return allocations;
    }
  }

  if (
    (startRow == 2 && startCol == 0) ||
    (startRow == 0 && startCol == 1) ||
    (startRow == 1 && startCol == 0) ||
    (startRow == 1 && startCol == 2) ||
    (startRow == 2 && startCol == 1) ||
    (startRow == 0 && startCol == 2)
  ) {
    let zeroCounter = 0;
    if (allocations[(2, 0)] === 0) {
      ++zeroCounter;
    }
    if (allocations[(0, 1)] === 0) {
      ++zeroCounter;
    }
    if (allocations[(1, 0)] === 0) {
      ++zeroCounter;
    }
    if (allocations[(1, 2)] === 0) {
      ++zeroCounter;
    }
    if (allocations[(2, 1)] === 0) {
      ++zeroCounter;
    }
    if (allocations[(0, 2)] === 0) {
      ++zeroCounter;
    }
    if (zeroCounter === 1) {
      if (
        (startRow == 2 && startCol == 0) ||
        (startRow == 1 && startCol == 2) ||
        (startRow == 0 && startCol == 1)
      ) {
        const min = Math.min(
          allocations[0][2],
          allocations[1][0],
          allocations[2][1]
        );
        allocations[0][2] -= min;
        allocations[1][0] -= min;
        allocations[2][1] -= min;
        allocations[2][0] += min;
        allocations[1][2] += min;
        allocations[0][1] += min;
      } else {
        const min = Math.min(
          allocations[2][0],
          allocations[1][2],
          allocations[0][1]
        );
        allocations[0][2] += min;
        allocations[1][0] += min;
        allocations[2][1] += min;
        allocations[2][0] -= min;
        allocations[1][2] -= min;
        allocations[0][1] -= min;
      }
      return allocations;
    }
  }

  let finishCycleRow = -1;
  let finishCycleCol = -1;
  for (let i = 0; i < allocations.length; ++i) {
    for (let j = 0; j < allocations[i].length; ++j) {
      if (
        i != startRow &&
        j != startCol &&
        allocations[startRow][j] > 0 &&
        allocations[i][startCol] > 0 &&
        allocations[i][j] > 0
      ) {
        finishCycleRow = i;
        finishCycleCol = j;
      }
    }
  }

  if (finishCycleRow === -1 || finishCycleCol === -1) {
    throw new Error(
      "План не оптимальный, при этом не удалось найти ни один цикл"
    );
  }
  const min = Math.min(
    allocations[startRow][finishCycleCol],
    allocations[finishCycleRow][startCol]
  );

  allocations[startRow][startCol] += min;
  allocations[finishCycleRow][finishCycleCol] += min;
  allocations[startRow][finishCycleCol] -= min;
  allocations[finishCycleRow][startCol] -= min;
  return allocations;
}

function calculateTargetFunction(costMatrix, allocations) {
  let summ = 0;
  for (let i = 0; i < costMatrix.length; i++) {
    for (let j = 0; j < costMatrix[i].length; j++) {
      summ += costMatrix[i][j] * allocations[i][j];
    }
  }
  return summ;
}

const costMatrix = [
  [3926, 3314, 4765],
  [4395, 3540, 5109],
  [4800, 3989, 5874],
];
//Производители
const supply = [1400, 700, 1000];
//Потребители
const demand = [900, 800, 1400];
if (
  supply.reduce(
    (accumulator, currentValue) => accumulator + currentValue,
    0
  ) !==
  demand.reduce((accumulator, currentValue) => accumulator + currentValue, 0)
) {
  throw new Error("Задача является открытой");
}
const result = northwestCornerMethod(costMatrix, supply, demand);
if (
  result.flat().filter((n) => n > 0).length !==
  supply.length + demand.length - 1
) {
  throw new Error("Опорный план вырожденный");
}
console.log(
  `Целевая функция при подсчёте с опорным планом: ${calculateTargetFunction(
    costMatrix,
    result
  )}`
);
let isFinish = false;
while (!isFinish) {
  ({ u, v } = findPotential(result, costMatrix));
  let delta = findDelta(result, costMatrix, u, v);
  isFinish = optimize(delta, result);
}
console.log("\n\n");
console.table(result);
console.log(
  `Оптимизированная целевая функция: ${calculateTargetFunction(
    costMatrix,
    result
  )}`
);
