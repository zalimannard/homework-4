function hungarianMethod(costMatrix, demand, supply) {
  const costMatrixCopy = copyMatrix(costMatrix);
  const demandCopy = demand.slice();
  const supplyCopy = supply.slice();

  let result = [];
  let counter = 1;

  while (true) {
    console.log(`Итерация номер ${counter}`);
    reduceMatrix(costMatrixCopy);
    console.log(
      "Матрица после вычитания минимальных элементов из строк и столбцов:"
    );
    console.table(costMatrixCopy);
    const zeroSet = findZeroSet(costMatrixCopy);
    if (
      zeroSet.length === Math.min(costMatrix.length, costMatrix[0].length) &&
      canSolve(costMatrixCopy, demandCopy, supplyCopy)
    ) {
      result = solve(costMatrixCopy, demandCopy, supplyCopy);
      break;
    }

    modifyMatrix(costMatrixCopy, zeroSet);
    console.log(
      "Матрица после вычитания минимального элемента из строки и прибавления того же самого элемента к столбцам с отрицательным элементом:"
    );
    console.table(costMatrixCopy);
    counter++;
  }
  let totalCost = 0;
  for (let i = 0; i < result.length; i++) {
    for (let j = 0; j < result[i].length; j++) {
      if (result[i][j] !== null) {
        totalCost += result[i][j] * costMatrix[i][j];
      }
    }
  }
  console.log("\nМатрица перевозок:");
  console.table(result);
  return totalCost;
}

function reduceMatrix(costMatrix) {
  for (let i = 0; i < costMatrix[0].length; i++) {
    const minInRow = Math.min(...costMatrix[i]);
    costMatrix[i] = costMatrix[i].map((el) => el - minInRow);
  }

  for (let i = 0; i < costMatrix[0].length; i++) {
    let minInCol = Infinity;
    for (let j = 0; j < costMatrix.length; j++) {
      if (costMatrix[j][i] < minInCol) {
        minInCol = costMatrix[j][i];
      }
    }
    for (let j = 0; j < costMatrix.length; j++) {
      costMatrix[j][i] -= minInCol;
    }
  }
}

function isOneZeroElement(matrix, coord) {
  let notInSameRow = false;
  let notInSameCol = false;

  for (let i = 0; i < matrix.length; i++) {
    if (i !== coord.i && matrix[i][coord.j] === 0) {
      notInSameCol = true;
    }
  }

  for (let j = 0; j < matrix[0].length; j++) {
    if (j !== coord.j && matrix[coord.i][j] === 0) {
      notInSameRow = true;
    }
  }

  return !(notInSameCol && notInSameRow);
}

function canSolve(costMatrix, demand, supply) {
  let isFinished = false;
  const matrixCopy = copyMatrix(costMatrix);
  const demandCopy = demand.slice();
  const supplyCopy = supply.slice();

  while (!isFinished) {
    isFinished = true;
    for (let i = 0; i < costMatrix.length; i++) {
      for (let j = 0; j < costMatrix[0].length; j++) {
        if (
          matrixCopy[i][j] === 0 &&
          isOneZeroElement(matrixCopy, { i, j }) &&
          demandCopy[j] > 0 &&
          supplyCopy[i] > 0
        ) {
          isFinished = false;
          const min = Math.min(supplyCopy[i], demandCopy[j]);
          matrixCopy[i][j] += min;
          supplyCopy[i] -= min;
          demandCopy[j] -= min;
        }
      }
    }
  }

  return demandCopy.every((d) => d === 0) && supplyCopy.every((s) => s === 0);
}

function solve(costMatrix, demand, supply) {
  let isFinished = false;
  const matrixCopy = copyMatrix(costMatrix);
  const demandCopy = demand.slice();
  const supplyCopy = supply.slice();

  matrixCopy.forEach((row, i) => {
    row.forEach((el, j) => {
      if (el !== 0) {
        matrixCopy[i][j] = null;
      }
    });
  });

  while (!isFinished) {
    isFinished = true;
    for (let i = 0; i < costMatrix.length; i++) {
      for (let j = 0; j < costMatrix[0].length; j++) {
        if (
          matrixCopy[i][j] === 0 &&
          isOneZeroElement(matrixCopy, { i, j }) &&
          demandCopy[j] > 0 &&
          supplyCopy[i] > 0
        ) {
          isFinished = false;
          const min = Math.min(supplyCopy[i], demandCopy[j]);
          matrixCopy[i][j] += min;
          supplyCopy[i] -= min;
          demandCopy[j] -= min;
        }
      }
    }
  }

  return matrixCopy;
}

function findZeroSet(costMatrix) {
  let maxSet = [];
  const visited = [];
  const zeroSet = [];

  while (true) {
    const zero = findUnmarkedZero(costMatrix, visited, maxSet);
    if (zero) {
      zeroSet.push(zero);
      visited.push(zero);
    } else if (zeroSet.length > 0 && zeroSet.length !== costMatrix.length) {
      if (zeroSet.length > maxSet.length) {
        maxSet = zeroSet;
      }
      zeroSet.pop();
    } else {
      break;
    }
  }
  return maxSet;
}

function findUnmarkedZero(costMatrix, visited, zeroSet) {
  for (let i = 0; i < costMatrix.length; i++) {
    for (let j = 0; j < costMatrix[0].length; j++) {
      if (
        costMatrix[i][j] === 0 &&
        !isVisited({ i, j }, visited) &&
        !isInSet({ i, j }, zeroSet)
      ) {
        return { i, j };
      }
    }
  }

  return null;
}

function isVisited(coord, visited) {
  return visited.some((el) => el.i === coord.i && el.j === coord.j);
}

function isInSet(coord, zeroSet) {
  return zeroSet.some((el) => coord.i === el.i || coord.j === el.j);
}

function modifyMatrix(costMatrix, zeroSet) {
  let min = Infinity;
  let index = { col: null, row: null };

  costMatrix.forEach((row, i) => {
    row.forEach((el, j) => {
      if (el < min && el > 0) {
        min = el;
        index.col = j;
        index.row = i;
      }
    });
  });

  const negativeElementsIndexes = [];

  for (let i = 0; i < costMatrix[index.row].length; i++) {
    costMatrix[index.row][i] -= min;
    if (costMatrix[index.row][i] < 0) {
      negativeElementsIndexes.push(i);
    }
  }

  for (let i of negativeElementsIndexes) {
    for (let j = 0; j < costMatrix.length; j++) {
      costMatrix[j][i] += min;
    }
  }
}

function copyMatrix(matrix) {
  return matrix.map((row) => row.slice());
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

console.log(hungarianMethod(costMatrix, demand, supply));
