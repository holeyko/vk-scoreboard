import { ICategory } from "./category";

export interface IRole {
    id: number
    name: string
}

export interface IUserCategoryStatistic {
    id: number
    countSolved: number
    category: ICategory
}

export interface IUserStatistic {
    id: number
    categoryStatistics: IUserCategoryStatistic[]
}

export interface IUser {
    id: number
    login: string
    firstName: string
    lastName: string
    roles?: IRole[]
    statistic?: IUserStatistic
}