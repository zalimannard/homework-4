class SimplexSolver {
  constructor() {
    this.conditions = [{ leftSide: [], sign: "", rightSide: 0 }];
    this.equal = { coefficients: [], type: "" };
    this.colLabels = [];
    this.rowLabels = [];
    this.simplexTable = [];
  }

  setConditions(conditions) {
    this.conditions = conditions;
  }

  setEqual(equal) {
    this.equal = equal;
  }

  setMatrixSize(row, col) {
    return Array.from({ length: row }, () => Array(col).fill(0));
  }

  castConditionsToEquals() {
    this.conditions.forEach((condition) => {
      if (condition.sign === ">=") {
        condition.sign = "<=";
        condition.rightSide *= -1;
        condition.leftSide = condition.leftSide.map((el) => el * -1);
      }
    });
  }

  initializeTable() {
    const condLen = this.conditions.length;
    const equalCoeffLen = this.equal.coefficients.length;
    this.simplexTable = this.setMatrixSize(
      condLen + 1,
      condLen + equalCoeffLen + 1
    );

    this.conditions.forEach((condition, i) => {
      this.simplexTable[i][0] = condition.rightSide;
      this.rowLabels.push(equalCoeffLen + i + 1);
    });

    this.colLabels.push("b");
    this.rowLabels.push("f");

    for (let i = 0; i < condLen + equalCoeffLen; i++) {
      this.colLabels.push(i + 1);
    }

    for (let i = 0; i < condLen + 1; i++) {
      for (let j = 1; j < equalCoeffLen + 1; j++) {
        this.simplexTable[i][j] =
          i === condLen
            ? this.equal.type === "min"
              ? this.equal.coefficients[j - 1]
              : -this.equal.coefficients[j - 1]
            : this.conditions[i].leftSide[j - 1];
      }
    }

    for (let i = equalCoeffLen + 1; i < condLen + equalCoeffLen + 1; i++) {
      this.simplexTable[i - equalCoeffLen - 1][i] = 1;
    }
  }

  findMinInB() {
    let min = Infinity;
    let row = null;
    for (let i = 0; i < this.rowLabels.length - 1; i++) {
      if (this.simplexTable[i][0] < 0 && this.simplexTable[i][0] < min) {
        min = this.simplexTable[i][0];
        row = i;
      }
    }
    return row;
  }

  findMinInRow(rowIndex) {
    let min = Infinity;
    let col = null;
    for (let i = 1; i < this.colLabels.length; i++) {
      if (
        this.simplexTable[rowIndex][i] < 0 &&
        this.simplexTable[rowIndex][i] < min
      ) {
        min = this.simplexTable[rowIndex][i];
        col = i;
      }
    }
    return col;
  }

  findCoeff(focusRow, anotherRow, focusCol) {
    return (
      this.simplexTable[anotherRow][focusCol] /
      this.simplexTable[focusRow][focusCol]
    );
  }

  subtractRows(focusRow, anotherRow, coeff) {
    for (let i = 0; i < this.colLabels.length; i++) {
      this.simplexTable[anotherRow][i] =
        this.simplexTable[anotherRow][i] -
        this.simplexTable[focusRow][i] * coeff;
    }
  }

  resetColumnToZero(focusRow, focusCol) {
    for (let i = 0; i < this.rowLabels.length; i++) {
      if (focusRow !== i) {
        const coeff = this.findCoeff(focusRow, i, focusCol);
        this.subtractRows(focusRow, i, coeff);
      }
    }
  }

  divideRowByNumber(row, number) {
    for (let i = 0; i < this.colLabels.length; i++) {
      this.simplexTable[row][i] = this.simplexTable[row][i] / number;
    }
  }

  changeLabelInRow(rowX, colX) {
    this.rowLabels[rowX] = this.colLabels[colX];
  }

  findMinInF() {
    let min = Infinity;
    let col = null;
    for (let i = 1; i < this.colLabels.length; i++) {
      if (
        this.simplexTable[this.simplexTable.length - 1][i] < 0 &&
        this.simplexTable[this.simplexTable.length - 1][i] < min
      ) {
        min = this.simplexTable[this.simplexTable.length - 1][i];
        col = i;
      }
    }
    return col;
  }

  findMinInCol(col) {
    let min = Infinity;
    let row = null;
    for (let i = 0; i < this.rowLabels.length - 1; i++) {
      if (
        this.simplexTable[i][0] / this.simplexTable[i][col] > 0 &&
        this.simplexTable[i][0] / this.simplexTable[i][col] < min
      ) {
        min = this.simplexTable[i][0] / this.simplexTable[i][col];
        row = i;
      }
    }
    return row;
  }

  findOptimal() {
    while (true) {
      console.table(this.simplexTable);
      let indexMinB = this.findMinInB();
      let indexMinCol = null;
      if (indexMinB === null) {
        indexMinCol = this.findMinInF();
        if (indexMinCol === null) {
          break;
        } else {
          indexMinB = this.findMinInCol(indexMinCol);
        }
      } else {
        indexMinCol = this.findMinInRow(indexMinB);
      }
      if (indexMinCol === null || indexMinB === null) {
        throw new Error("Cannot solve the problem");
      }
      this.resetColumnToZero(indexMinB, indexMinCol);
      this.changeLabelInRow(indexMinB, indexMinCol);
      this.divideRowByNumber(
        indexMinB,
        this.simplexTable[indexMinB][indexMinCol]
      );
    }
  }

  start() {
    console.log("----------------");
    this.castConditionsToEquals();
    this.initializeTable();

    const answer = [];
    this.findOptimal();

    const x = new Array(this.conditions[0].leftSide.length + 1).fill(1);

    for (let i = 0; i < this.rowLabels.length; i++) {
      if (
        this.rowLabels[i] < this.conditions[0].leftSide.length + 1 &&
        this.rowLabels[i] > 0
      ) {
        answer.push({
          label: this.rowLabels[i],
          value: this.simplexTable[i][0],
        });
        x[this.rowLabels[i]] -= 1;
      }
    }

    for (let i = 1; i <= this.conditions[0].leftSide.length; i++) {
      if (x[i] === 1) {
        answer.push({
          label: i,
          value: 0,
        });
      }
    }

    if (this.equal.type === "min") {
      answer.push({
        label: "F",
        value: -this.simplexTable[this.simplexTable.length - 1][0],
      });
    } else {
      answer.push({
        label: "F",
        value: this.simplexTable[this.simplexTable.length - 1][0],
      });
    }

    return answer;
  }
}

let m = [
  [2, 3, 1, 5],
  [3, 5, 4, 6],
  [4, 3, 2, 3],
  [1, 5, 3, 4],
];
//ищем минимум в строке
function getMinInRow(arr) {
  let min = Infinity;
  let x = 0;
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] < min) {
      min = arr[i];
      x = i;
    }
  }
  return { x, value: min };
}
//ищем максимум в столбце
function getMaxInCol(colCoord, arr) {
  let max = -Infinity;
  let y = 0;
  for (let i = 0; i < arr.length; i++) {
    if (max < arr[i][colCoord]) {
      max = arr[i][colCoord];
      y = i;
    }
  }
  return { y, value: max };
}
//Удаляем доминирущие строки и столбцы
function deleteDominatesColAndRow(matrix) {
  //удаляем доменирующие строки
  for (let i = 0; i < matrix.length; i++) {
    for (let j = 0; j < matrix.length; j++) {
      let flag = true;
      for (let k = 0; k < matrix[j].length; k++) {
        if (i === j) {
          flag = false;
          break;
        }
        flag = matrix[i][k] >= matrix[j][k] && flag;
      }
      if (flag) {
        matrix.splice(j, 1);
        if (i > 0) {
          i--;
        }
        if (j > 0) {
          j--;
        }
      }
    }
  }

  //удаляем доменирующие столбцы
  for (let i = 0; i < matrix[0].length; i++) {
    for (let j = 0; j < matrix[0].length; j++) {
      let flag = true;
      for (let k = 0; k < matrix.length; k++) {
        if (i === j) {
          flag = false;
          break;
        }
        flag = matrix[k][i] >= matrix[k][j] && flag;
      }
      if (flag) {
        for (let row of matrix) {
          row.splice(i, 1);
        }
        i--;
        j--;
      }
    }
  }
}

function task1(m) {
  let x1,
    y1,
    x2 = 0,
    y2 = 0,
    min = Infinity,
    max = -Infinity;
  for (let i = 0; i < m.length; i++) {
    for (let j = 0; j < m[i].length; j++) {
      /**
       * получаем минимум в строке. если он больше текущего минимума, то запоминаем его
       * и его координаты
       */
      const minInRow = getMinInRow(m[i]);
      if (max < minInRow.value) {
        x1 = minInRow.x;
        y1 = i;
        max = minInRow.value;
      }
      /**
       * Получаем максимум в столбце. Если он меньше текущего максимума, то запоминаем его и его координаты
       */
      const maxInCol = getMaxInCol(j, m);
      if (min > maxInCol.value) {
        x2 = j;
        y2 = maxInCol.y;
        min = maxInCol.value;
      }
    }
  }

  console.log("Нижняя цена игры " + max);
  console.log("--------");
  console.log("Верхняя цена игры " + min);

  if (x1 == x2 && y1 == y2) {
    console.log("Это седловая точка");
  } else {
    console.log("Седловой точки нет");
  }
}

function task2() {
  task1(m);
  console.table(m);
  deleteDominatesColAndRow(m);
  console.log("Удалил доминирующие строки и столбцы");
  console.table(m);
  //находим для максимума симплекс методом
  let simplex = new SimplexSolver();
  let cond = [];
  for (let i = 0; i < m.length; i++) {
    cond.push({ leftSide: m[i], sign: "<=", rightSide: 1 });
  }
  simplex.setConditions(cond);
  simplex.setEqual({
    coefficients: new Array(simplex.conditions[0].leftSide.length).fill(1),
    type: "max",
  });
  const max = simplex.start();
  //находим для минимума
  simplex = new SimplexSolver();
  cond = [];
  for (let i = 0; i < m[0].length; i++) {
    let coeff = [];
    for (let j = 0; j < m.length; j++) {
      coeff.push(m[j][i]);
    }
    cond.push({ leftSide: coeff, sign: ">=", rightSide: 1 });
  }
  simplex.setConditions(cond);
  simplex.setEqual({
    coefficients: new Array(simplex.conditions[0].leftSide.length).fill(1),
    type: "min",
  });
  const min = simplex.start();
  min.sort((a, b) => {
    if (a.label === "F") {
      return 1;
    }
    if (b.label === "F") {
      return -1;
    }
    if (a.label < b.label) return -1;
    if (a.label > b.label) return 1;
    if (a.label === b.label) return 0;
  });
  max.sort((a, b) => {
    if (a.label === "F") {
      return 1;
    }
    if (b.label === "F") {
      return -1;
    }
    if (a.label < b.label) return -1;
    if (a.label > b.label) return 1;
    if (a.label === b.label) return 0;
  });

  //считаем финал
  const gamePrice = 1 / min[min.length - 1].value;
  console.log(`Цена игры ${gamePrice}`);

  console.log(
    `Оптимальная смешанная стратегия игрока 1: [${max.map((o) =>
      o.label !== "F" ? o.value * gamePrice : null
    )}]`
  );

  console.log(
    `Оптимальная смешанная стратегия игрока 2: [${min.map((o) =>
      o.label !== "F" ? o.value * gamePrice : null
    )}]`
  );
}

function task3() {
  //В зависимости от игрока, возвращает массив
  function choice(variant, matrix, gamer) {
    const arr = [];
    if (gamer === "B") {
      for (let i = 0; i < matrix.length; i++) {
        arr.push(matrix[i][variant]);
      }
    }
    if (gamer === "A") {
      for (let i = 0; i < matrix[0].length; i++) {
        arr.push(matrix[variant][i]);
      }
    }
    return arr;
  }
  //возвращаем максимум
  function getMax(vector) {
    const o = {
      value: -Infinity,
      index: -1,
    };
    for (let i = 0; i < vector.length; i++) {
      if (vector[i] > o.value) {
        o.value = vector[i];
        o.index = i;
      }
    }
    return o.index;
  }
  function getMin(vector) {
    const o = {
      value: Infinity,
      index: -1,
    };
    for (let i = 0; i < vector.length; i++) {
      if (vector[i] < o.value) {
        o.value = vector[i];
        o.index = i;
      }
    }
    return o.index;
  }

  const m = [
    [4, 2, 3],
    [1, -1, -2],
    [0, 3, 5],
  ];
  deleteDominatesColAndRow(m);
  let AChoose = 0;
  let BChoose = 0;
  let vMin = 0;
  let vMax = 0;
  let avgV = 0;
  let A = new Array(m[0].length).fill(0);
  let B = new Array(m.length).fill(0);
  let P = new Array(m.length).fill(0);
  let Q = new Array(m[0].length).fill(0);
  const k = 500; //Число итераций
  console.table(m);
  for (let i = 1; i <= k; i++) {
    console.log("-----------------");
    console.log("Итерация " + i);
    const tempA = choice(AChoose, m, "A");
    console.log("A" + AChoose);
    P[AChoose] += 1;
    for (let j = 0; j < tempA.length; j++) {
      A[j] += tempA[j];
    }
    BChoose = getMin(A);
    Q[BChoose] += 1;
    const tempB = choice(BChoose, m, "B");
    for (let j = 0; j < tempB.length; j++) {
      B[j] += tempB[j];
    }
    AChoose = getMax(B);
    console.log("B" + BChoose);
    console.log(A);
    console.log(B);
    vMin = A[BChoose] / i;
    vMax = B[AChoose] / i;
    avgV = (vMax + vMin) / 2;
    console.log(`vMax: ${vMax} vMin: ${vMin} avgV: ${avgV}`);
    console.log("-----------------");
  }
  P = P.map((a) => a / k);
  Q = Q.map((a) => a / k);
  console.log("Вероятности P");
  console.table(P);
  console.log("Вероятности Q");
  console.table(Q);
}
console.log("Задача 1");
task1(m);
m = [
  [4, 2, 3],
  [1, -1, -2],
  [0, 3, 5],
];

console.log("Задача 2");
task2();
console.log("Задача 3");
task3();
