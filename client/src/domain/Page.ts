export default class Page<T> {
    constructor(
        public number: number = 0,
        public size: number = 0,
        public numberOfElements: number = 0,
        public totalPages: number = 0,
        public totalElements: number = 0,
        public content: Array<T> = []
    ) {
    }
}
