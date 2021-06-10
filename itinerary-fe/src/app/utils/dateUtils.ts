import * as moment from "moment";

export class DateUtils {
    private static readonly DATE_TIME_FORMAT = "DD/MM/YYYY HH:mm";
    private static readonly DATE_FORMAT = "DD/MM/YYYY";

    static getDateTimeStringFromMs(date: moment.Moment) {
        return moment(date).format(this.DATE_TIME_FORMAT);
    }
    
    static getDateStringFromMs(date: moment.Moment, dayIndex: number) {
        return this.getDateFromMs(date, dayIndex).format(this.DATE_FORMAT);
    }

    static getDateFromMs(date: moment.Moment, dayIndex: number) {
        date = moment(date);
        date.add(dayIndex + 1, 'days');

        return date;
    }
}