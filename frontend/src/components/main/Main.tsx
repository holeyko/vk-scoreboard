import { useEffect, useState } from "react";
import axios from "axios";
import { ICategory } from "../../model/category";
import Scoreboard from "./Scoreboard";
import { IUser } from "../../model/user";


export default function Main() {
    const [categories, setCategories] = useState<ICategory[] | undefined>(undefined)
    const [curUser, setCurUser] = useState<IUser | undefined>(undefined)

    function getCategories() {
        axios.get<ICategory[]>('http://localhost:8080/api/1/categories')
            .then(response => setCategories(response.data))
            .catch(err => console.log(err))
    }

    function getCurUser() {
        axios.get<IUser>('http://localhost:8080/api/1/jwt/user?jwt=' + localStorage.getItem('jwt'))
            .then(response => setCurUser(response.data))
            .catch(err => console.log(err))
    }

    useEffect(() => {
        getCurUser()
        getCategories()
    }, [])

    return (
        <>
            { typeof categories === 'undefined' || typeof curUser === 'undefined' ? 'Loading...' :
                <Scoreboard categories={ categories } curUser={ curUser }/> }
        </>
    )
}