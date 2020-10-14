export const nonNegativeOrDefault = (value: number | undefined, defValue: number): number => value != null && value >= 0 ? value : defValue;
