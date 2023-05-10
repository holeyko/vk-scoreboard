import requests as req
import asyncio
import json
import random

users = []
categories = []
host = 'http://localhost:8080/api/1'


def generate_users():
    baseLogin = 'user'
    baseFirstname = 'User'
    baseLastname = 'User'
    basePassword = 'qwerty'

    for i in range(500):
        res = req.post(host + '/users/new', json={
            'login': baseLogin + str(i),
            'firstName': baseFirstname + str(i),
            'lastName': baseLastname + str(i),
            'password': basePassword,
        })
        
        jwt = res.text
        
        user = req.get(host + '/jwt/user?jwt=' + jwt)
        users.append((json.loads(user.text), jwt))

        
def generate_categories():
    baseName = 'Category '
    for i in range(3):
        res = req.post(host + '/categories/new', json={
            'name': baseName + str(i),
            'countTasks': random.randrange(1, 99)
        })
        categories.append(json.loads(res.text))
    

def generate_users_solved_tasks():
    for (user, jwt) in users:
        for category in categories:
            count_solved = random.randrange(0, int(category['countTasks']))
            json = {
                'jwt': jwt,
                'categoryName': category['name'],
                'countSolved': str(count_solved)
            }

            req.put(host + '/users/countSolved', json=json)



def main():
    generate_users()
    generate_categories()
    generate_users_solved_tasks()


if __name__ == '__main__':
    main()