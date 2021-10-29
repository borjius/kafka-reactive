First login with any user pass:

curl -XPOST http://localhost:8080/login -d '{"username":"myUser", "password":"myPassword", "role":"ROLE_USER", "permissions":"send events"}' -H 'Content-Type: application/json'


Then, with the token:

(ADMIN)
curl http://localhost:8080/admin -H "Authorization: Bearer $MYTOKEN"

(USER)
curl http://localhost:8080/test -H "Authorization: Bearer $MYTOKEN"

(No token needed)
curl -XPOST http://localhost:8080/events -d '{"name":"Example2"}' -H 'Content-Type: application/json'

This last endpoint sends the event to kafka using reactor. Event will be listened by another consumer also in this micro.


Tech stack:
- kafka reactor
- webflux
- reactive security
- sleuth