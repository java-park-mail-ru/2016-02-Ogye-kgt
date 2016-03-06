import requests as req

HOST = 'http://localhost:8080/api/'


# HOST = 'http://http://95.213.235.104/api/'

class User:
    def __init__(self, login, mail, password):
        self.login = login
        self.mail = mail
        self.mail = password


class Api:
    class user:
        @staticmethod
        def get():
            return req.get(HOST + 'user')

        @staticmethod
        def post(user):
            return req.post(HOST + 'user', json=user)

        @staticmethod
        def delete(uid):
            return req.delete(HOST + 'user/' + uid)


def main():
    user1 = User('alex', 'alex@mail.ru', '1234')
    user2 = User('bill', 'bill@mail.ru', 'qwerty')
    user3 = User('bob', 'bob@gmail.com', 'ogye_kgt')
    print(Api.user.get().text)
    print(Api.user.post(user1).text)
    print(Api.user.post(user2).text)
    print(Api.user.post(user3).text)
    print(Api.user.get().text)


if __name__ == '__main__':
    main()
