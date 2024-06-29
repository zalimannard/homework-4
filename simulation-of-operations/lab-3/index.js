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

  addCol() {
    this.simplexTable.forEach((row) => row.push(0));
    this.colLabels.push(this.colLabels.length);
  }

  addRow() {
    this.simplexTable.unshift(
      this.simplexTable.length > 0
        ? new Array(this.simplexTable[0].length).fill(0)
        : []
    );
    this.rowLabels.unshift(0);
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

    console.log("simplex table start");
    this.printResults();
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
      this.normalizeTableValue();
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

  isIntSolve() {
    for (let i = 0; i < this.rowLabels.length; i++) {
      if (
        this.rowLabels[i] > 0 &&
        this.rowLabels[i] < this.equal.coefficients.length + 1 &&
        !Number.isInteger(this.simplexTable[i][0])
      ) {
        return false;
      }
    }
    return true;
  }

  findRowWithMaxFractionalInB() {
    let max = -Infinity;
    let row = null;
    for (let i = 0; i < this.rowLabels.length - 1; i++) {
      const currentElement = this.simplexTable[i][0];
      if (
        currentElement % 1 > max &&
        this.rowLabels[i] > 0 &&
        this.rowLabels[i] < this.equal.coefficients.length + 1
      ) {
        max = currentElement % 1;
        row = i;
      }
    }
    return row;
  }

  addRestriction(row) {
    for (let i = 0; i < this.colLabels.length - 1; i++) {
      if (this.simplexTable[row][i] >= 0) {
        this.simplexTable[0][i] = -this.simplexTable[row][i] % 1;
      } else {
        this.simplexTable[0][i] = -(1 + (this.simplexTable[row][i] % 1));
      }
    }
    this.simplexTable[0][this.simplexTable[0].length - 1] = 1;
  }

  start() {
    while (true) {
      this.findOptimal();
      console.log("Found optimal solution");
      this.printResults();

      if (this.isIntSolve()) {
        console.log("Solution is integer");
        break;
      }

      const row = this.findRowWithMaxFractionalInB();
      this.addRow();
      this.addCol();
      this.addRestriction(row + 1);
      console.log("Added constraint");
      this.printResults();
    }
    const x = new Array(this.rowLabels.length).fill(1);

    for (let i = 0; i < this.rowLabels.length; i++) {
      if (
        this.rowLabels[i] < this.conditions[0].leftSide.length + 1 &&
        this.rowLabels[i] > 0
      ) {
        console.log(`x${this.rowLabels[i]} = ${this.simplexTable[i][0]}`);
        x[i - 1] -= 1;
      }
    }

    for (let i = 1; i < this.conditions[0].leftSide.length + 1; i++) {
      if (x[i] === 1) {
        console.log(`x${i} = 0`);
      }
    }

    console.log(
      `F = ${
        this.equal.type === "min"
          ? -this.simplexTable[this.simplexTable.length - 1][0]
          : this.simplexTable[this.simplexTable.length - 1][0]
      }`
    );
  }

  printResults() {
    console.log("--------------------");
    console.table(this.simplexTable);
    console.table(this.rowLabels);
    console.table(this.colLabels);
  }

  normalizeTableValue() {
    const precision = 2; // количество знаков после запятой, подлежащих сохранению
    const multiplier = Math.pow(10, precision);

    for (let i = 0; i < this.rowLabels.length; i++) {
      for (let j = 0; j < this.colLabels.length; j++) {
        this.simplexTable[i][j] =
          Math.round(this.simplexTable[i][j] * multiplier) / multiplier;
      }
    }
  }
}

const simplex = new SimplexSolver();

simplex.setConditions([
  { leftSide: [-3, 2], sign: "<=", rightSide: 7 },
  { leftSide: [4, -2], sign: "<=", rightSide: 7 },
  { leftSide: [0, 3], sign: ">=", rightSide: 5 },
  { leftSide: [0, 1], sign: ">=", rightSide: 0 },
  { leftSide: [1, 0], sign: ">=", rightSide: 0 },
]);

simplex.setEqual({ coefficients: [4, 4], type: "max" });
simplex.castConditionsToEquals();
simplex.initializeTable();
simplex.start();
