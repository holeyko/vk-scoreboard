import { useEffect, useState } from "react";
import { IUser, IUserCategoryStatistic } from "../../model/user";
import { ICategory } from "../../model/category";
import axios from "axios";
import './CommonScoreboard.css'

interface CommonScoreboardProps {
    categories: ICategory[]
    curUser: IUser
}

interface TopUsersCategory {
    category: ICategory
    usersInfo: {
        user: IUser
        countSolved: number | 'Unknown'
        number: number
    }[]
}

export default function CommonScoreboard({ curUser, categories}: CommonScoreboardProps) {
    const countUsersInCategory = 10

    const [topUsersCategories, setTopUsersCategories] = useState<TopUsersCategory[]>([])
    const [isDownloaded, setDownloaded] = useState(false)

    async function getCountSolvedTaskUserInCategory(user: IUser, category: ICategory) {
        try {
            let response = await axios.get<IUserCategoryStatistic>('http://localhost:8080/api/1/users/statistic/categoryStatistic/'
                .concat(user.id.toString(), '?categoryName=', category.name))
            return await response.data.countSolved
        } catch (err) {
            console.log(err)
        }

        return 'Unknown'
    }

    async function fetchTopUsersCategories() {
        let top: TopUsersCategory[] = []
        for (let category of categories) {
            const usersInfo: {
                user: IUser
                countSolved: number | 'Unknown'
                number: number
            }[] = []

            const url = 'http://localhost:8080/api/1/categories/top/'.concat(category.name, '/', countUsersInCategory.toString())
            const response = await axios.get<IUser[]>(encodeURI(url))
            const users = await response.data
            let wasCurUser = false
            for (let i = 0; i < users?.length; ++i) {
                const user = users[i]
                const countSolved = await getCountSolvedTaskUserInCategory(user, category)
                usersInfo.push({
                    user: user,
                    countSolved: countSolved,
                    number: i
                })

                if (user.id === curUser?.id) {
                    wasCurUser = true
                }
            }

            if (!wasCurUser) {
                if (curUser) {
                    const countSolved = await getCountSolvedTaskUserInCategory(curUser, category)
                    const response = await axios.get<number>(
                        'http://localhost:8080/api/1/categories/place/'.concat(category.name, '?jwt=', (localStorage.getItem('jwt') || ''))
                    )
                    usersInfo.push({
                        user: curUser,
                        countSolved: countSolved,
                        number: response.data
                    })
                }
            }

            top.push({
                category: category,
                usersInfo: usersInfo
            })

        }

        setDownloaded(true)
        setTopUsersCategories(top)
    }

    useEffect(() => {
        fetchTopUsersCategories()
    }, [])

    return (
        <>
            <h2>Common statistic</h2>
            <div className="categories_top">
                { isDownloaded && topUsersCategories.map(topUsersCategory =>
                    <div>
                        <p><b>{ topUsersCategory.category.name }</b></p>
                        <table>
                            <tr>
                                <th>Number</th>
                                <th>Login</th>
                                <th>Count solved</th>
                            </tr>
                            { topUsersCategory.usersInfo.map(info =>
                                <tr>
                                    <td>{ info.number + 1 }</td>
                                    <td>{ info.user.id === curUser.id ? <b>{curUser.login}</b> : info.user.login }</td>
                                    <td>{ info.countSolved }</td>
                                </tr>
                            ) }
                        </table>
                    </div>
                ) }
            </div>
        </>
    )
}
