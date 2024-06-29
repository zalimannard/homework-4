function knapsack(items, capacity) {
  const itemsLength = items.length;
  const dp = Array.from({ length: itemsLength + 1 }, () =>
    Array(capacity + 1).fill(0)
  );

  for (let itemIndex = 1; itemIndex <= itemsLength; itemIndex++) {
    for (let bagWeight = 1; bagWeight <= capacity; bagWeight++) {
      dp[itemIndex][bagWeight] = dp[itemIndex - 1][bagWeight];
      for (
        let currentItemsCount = Math.min(
          items[itemIndex - 1].limit,
          Math.floor(bagWeight / items[itemIndex - 1].weight)
        );
        currentItemsCount >= 1;
        currentItemsCount--
      ) {
        dp[itemIndex][bagWeight] = Math.max(
          dp[itemIndex][bagWeight],
          dp[itemIndex - 1][
            bagWeight - currentItemsCount * items[itemIndex - 1].weight
          ] +
            items[itemIndex - 1].price * currentItemsCount
        );
      }
    }
  }

  console.table(dp);

  let j = capacity,
    i = itemsLength;
  let selectedItems = [];
  while (i > 0 && j > 0) {
    if (dp[i][j] != dp[i - 1][j]) {
      selectedItems.push(i);
      items[i - 1].limit -= 1;
      j -= items[i - 1].weight;
    } else {
      i -= 1;
    }
  }
  console.table(selectedItems);
  return dp[itemsLength][capacity];
}

const items = [
  { weight: 2, price: 2, limit: 20 },
  { weight: 3, price: 3, limit: 20 },
  { weight: 4, price: 4, limit: 20 },
  { weight: 5, price: 5, limit: 20 },
  { weight: 6, price: 6, limit: 20 },
];

const capacity = 7;

const maxValue = knapsack(items, capacity);
console.log("Максимальная стоимость: " + maxValue);
