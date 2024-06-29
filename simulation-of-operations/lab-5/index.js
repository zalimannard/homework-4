function knapsack(items, capacity) {
  const itemsLength = items.length;
  const dp = Array.from({ length: itemsLength + 1 }, () =>
    Array(capacity + 1).fill(0)
  );

  for (let itemIndex = 1; itemIndex <= itemsLength; itemIndex++) {
    const currentItem = items[itemIndex - 1];
    for (let bagWeight = 0; bagWeight <= capacity; bagWeight++) {
      if (currentItem.weight > bagWeight) {
        dp[itemIndex][bagWeight] = dp[itemIndex - 1][bagWeight];
      } else {
        console.log(`Индекс строки ${itemIndex} индекс столбца ${bagWeight}\n`);
        console.log(
          `Выбираем максимум среди элемента выше текущего с индексом строки ${
            itemIndex - 1
          }
           и элемента выше с тем же индексом строки, но индексом столбца 
          bagWeight - currentItem.weight=${
            bagWeight - currentItem.weight
          } + currentItem.price=${currentItem.price}`
        );
        dp[itemIndex][bagWeight] = Math.max(
          dp[itemIndex - 1][bagWeight],
          dp[itemIndex - 1][bagWeight - currentItem.weight] + currentItem.price
        );
      }
      console.log("--------------------------------");
    }
  }

  console.table(dp);

  let j = capacity,
    i = itemsLength;
  while (dp[i][j] !== 0) {
    if (dp[i][j] === dp[i - 1][j]) {
      i--;
    } else {
      console.log(
        "Взяли предмет " +
          i +
          " со стоимостью " +
          items[i - 1].price +
          " и весом " +
          items[i - 1].weight
      );
      j -= items[i - 1].weight;
      i--;
    }
  }
  return dp[itemsLength][capacity];
}

const items = [
  { weight: 3, price: 1 },
  { weight: 4, price: 6 },
  { weight: 5, price: 6 },
  { weight: 8, price: 7 },
  { weight: 9, price: 6 },
];

const capacity = 13;

const maxValue = knapsack(items, capacity);
console.log("Максимальная стоимость: " + maxValue);
