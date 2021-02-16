export class Luogo {
    constructor(public id: number, public name: string) {}
}

export interface ILuoghiResponse {
    total: number;
    results: Luogo[];
}