//Алгоритм Басакера–Гоуэна

function floydWarshallWithPath(graph, s, t) {
  let dist = [];
  let next = [];

  // Инициализируем массивы расстояний и следующих вершин
  for (let i = 0; i < graph.length; i++) {
    dist[i] = [];
    next[i] = [];
    for (let j = 0; j < graph.length; j++) {
      if (i === j) {
        dist[i][j] = 0;
      } else if (!isFinite(graph[i][j])) {
        dist[i][j] = Infinity;
      } else {
        dist[i][j] = graph[i][j];
      }
      next[i][j] = j;
    }
  }

  // Применяем алгоритм Флойда-Уоршелла
  for (let k = 0; k < graph.length; k++) {
    for (let i = 0; i < graph.length; i++) {
      for (let j = 0; j < graph.length; j++) {
        if (dist[i][j] > dist[i][k] + dist[k][j]) {
          dist[i][j] = dist[i][k] + dist[k][j];
          next[i][j] = next[i][k];
        }
      }
    }
  }

  // Восстанавливаем маршрут
  let path = [];
  let current = s;
  while (current !== t) {
    path.push(current);
    current = next[current][t];
  }
  path.push(t);

  return { distance: dist[s][t], path };
}

function findE1(path, costMatrix, flow, flowNew) {
  let min = Infinity;
  for (let i = 0; i < path.length - 1; i++) {
    if (
      costMatrix[path[i]][path[i + 1]] > 0 &&
      costMatrix[path[i]][path[i + 1]] != Infinity
    ) {
      if (flow[path[i]][path[i + 1]] - flowNew[path[i]][path[i + 1]] < min) {
        min = flow[path[i]][path[i + 1]] - flowNew[path[i]][path[i + 1]];
      }
    }
  }
  return min;
}

function findE2(path, costMatrix, flowNew) {
  let min = Infinity;

  for (let i = 0; i < path.length - 1; i++) {
    if (costMatrix[path[i]][path[i + 1]] < 0) {
      if (flowNew[path[i]][path[i + 1]] < min) {
        if (flowNew[path[i]][path[i + 1]] != 0) {
          min = flowNew[path[i]][path[i + 1]];
        } else {
          min = flowNew[path[i + 1]][path[i]];
        }
      }
    }
  }
  return min;
}

function findE(e1, e2, foundV, v) {
  return Math.min(e1, e2, v - foundV);
}

function redraw(e1, e, path, costMatrix, flow, flowNew) {
  for (let i = 0; i < path.length - 1; i++) {
    if (
      costMatrix[path[i]][path[i + 1]] > 0 &&
      costMatrix[path[i]][path[i + 1]] !== Infinity
    ) {
      flowNew[path[i]][path[i + 1]] += e;
    } else {
      if (flowNew[path[i]][path[i + 1]] != 0) {
        flowNew[path[i]][path[i + 1]] -= e;
      } else {
        flowNew[path[i + 1]][path[i]] -= e;
      }
    }
    costMatrix[path[i + 1]][path[i]] = -costMatrix[path[i]][path[i + 1]];
    if (e1 == flow[path[i]][path[i + 1]]) {
      costMatrix[path[i]][path[i + 1]] = Infinity;
    }
  }
}

const start = 0; // Начальная вершина
const target = 3; // Конечная вершина

const costMatrix = [
  [0, 5, 1, Infinity],
  [Infinity, 0, Infinity, 1],
  [Infinity, 1, 0, 6],
  [Infinity, Infinity, Infinity, 0],
];

const costMatrixCopy = [
  [0, 5, 1, Infinity],
  [Infinity, 0, Infinity, 1],
  [Infinity, 1, 0, 6],
  [Infinity, Infinity, Infinity, 0],
];

const flowMatrix = [
  [0, 4, 2, Infinity],
  [Infinity, 0, Infinity, 2],
  [Infinity, 3, 0, 3],
  [Infinity, Infinity, Infinity, 0],
];

const newFlowMatrix = [
  [0, 0, 0, 0],
  [0, 0, 0, 0],
  [0, 0, 0, 0],
  [0, 0, 0, 0],
];

let e;
let e1;
let e2;
const v = 3;
let foundV = 0;
let count = 0;
while (foundV < v) {
  console.log(++count + " итерация");
  const { path } = floydWarshallWithPath(costMatrix, start, target);
  console.log("Путь " + path);
  e1 = findE1(path, costMatrix, flowMatrix, newFlowMatrix);
  console.log(`e1 = ${e1}`);
  e2 = findE2(path, costMatrix, newFlowMatrix);
  console.log(`e2 = ${e2}`);
  e = findE(e1, e2, foundV, v);
  console.log(`e = ${e}`);
  foundV += e;
  redraw(e1, e, path, costMatrix, flowMatrix, newFlowMatrix);
  console.log("Матрица стоимости");
  console.table(costMatrix);
  console.log("Матрица потоков");
  console.table(newFlowMatrix);
}

let sum = 0;

for (let i = 0; i < costMatrixCopy.length; i++) {
  for (let j = 0; j < costMatrixCopy[0].length; j++) {
    if (costMatrixCopy[i][j] !== Infinity) {
      sum += costMatrixCopy[i][j] * newFlowMatrix[i][j];
    }
  }
}
