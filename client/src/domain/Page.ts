export interface Page<T> {
    number: number;
    size: number;
    numberOfElements: number;
    totalPages: number;
    totalElements: number;
    content: T[];
}
