import { ICategory } from "../../model/category";
import { IUser } from "../../model/user";
import CommonScoreboard from "./CommonScoreboard";
import PersonalScoreboard from "./PersonalScoreboard";

interface ScoreboardProps {
    categories: ICategory[]
    curUser: IUser
}

export default function Scoreboard({ curUser, categories }: ScoreboardProps) {
    return (
        <>
            <CommonScoreboard categories={ categories } curUser={ curUser }/>
            <PersonalScoreboard categories={ categories } curUser={ curUser }/>
        </>
    )
}