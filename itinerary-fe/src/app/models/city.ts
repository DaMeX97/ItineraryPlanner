export class City {
    public id: number = -1;
    public name: string = "";
    public latitude: number = -1;
    public longitude: number = -1;
    public provinceCode: string = "";

    constructor(id: number, name: string, latitude: number, longitude: number, provinceCode: string) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provinceCode = provinceCode;
    }

    getSmallInfoString() {
        return this.name + " (" + this.provinceCode + ")"; 
    }
}