import { ICategory } from "../../model/category";
import { IUser, IUserStatistic } from "../../model/user";
import { useEffect, useState } from "react";
import axios from "axios";
import './PersonalStat.css'

interface PersonalScoreboardProps {
    categories: ICategory[]
    curUser: IUser
}

export default function PersonalScoreboard({ categories, curUser }: PersonalScoreboardProps) {
    const [userStatistic, setUserStatistic] = useState<IUserStatistic | undefined>(undefined)

    async function getUserStatistic() {
        const response = await axios.get<IUserStatistic>(
            'http://localhost:8080/api/1/users/statistic?jwt=' + localStorage.getItem('jwt')
        )

        setUserStatistic(response.data)
    }

    function getCountSolvedTasks(category: ICategory) {
        if (userStatistic) {
            for (let categoryStat of userStatistic.categoryStatistics) {
                if (categoryStat.id === category.id) {
                    return categoryStat.countSolved
                }
            }
        }

        return 0
    }

    useEffect(() => {
        getUserStatistic()
    }, [])

    return (
        <>
            <h2> Personal Statistic </h2>
            { typeof userStatistic === 'undefined' ? 'Loading...' :
                <div className="personal_stat">
                    { categories.map(category =>
                        <div>
                            <p>{ category.name }</p>
                            <p>{ getCountSolvedTasks(category) } / { category.countTasks }</p>
                        </div>
                    )
                    }
                </div>
            }
        </>
    )
}