# Chapter IV: first attempt and cross cutting concerns
The reference for this chapter is [oauth2-spring-boot/keycloak-cross-cutting](https://github.com/emilianomaccaferri/oauth2-spring-boot/tree/keycloak-cross-cutting), you can use the collection named `oauth2-spring-boot-auth.json` if you want to follow along with Postman.

Now that we have our base system working correctly, it's time to plug the OAuth2 layer in and start authenticating requests.<br>
## JWTs (Json Web Tokens)
Remember what we said about JWTs? JWTs are cryptographically signed strings that encode information in JSON format. That's a nice definition that sums everything a JWT is in very few words, but what exactly does it mean?
<br>
JWTs enable developers to share JSON objects (any JSON object, really) between applications in a way that's _non repudiable_ and _authenticated_, meaning that only clients that have the __private key__ can __emit__ tokens, whereas clients that have the __public key__ can _verify_ the authenticity of such tokens (this is basic RSA).<br>
What's really groundbreaking about JWTs is that you can encode whatever information you want inside them, making them completely _stateless_. Let's imagine you're designing a login system for your web application that implements the following authentication flow:

- the user inserts their username and password;
- if the credentials are correct a session is created. You represent the session using a random string that you save inside the database and you link it to the authenticated user using the following table:
    ```
    sessions table:
        +---------+------------+
        | user_id | session_id |
        +---------+------------+
        |      1  | abcde1234  |
        +---------+------------+
    ```
- the session token is stored inside the user's cookies: this way, every time the users issues a request to your server, the session token is sent to the server;
- once the server receives a request, it reads the cookies, gets the session token and uses the session token to perform a lookup query to retrieve the requesting user's information. If we suppose that users are stored in the following way:
    ```
    users table:
        +----+-----------+---------+
        | id | name      | surname |
        +----+-----------+---------+
        | 1  | ponglerio | donglis |
        +----+-----------+---------+
    ```
    a lookup query can be done by joining on the `user_id` like this:
    ``` 
        select users.name, users.surname, users.id
            from sessions 
            join sessions.user_id = users.id
            where sessions.session_id = 'abcde1234';
    ```
- this way the server knows which user performs a certain request based on its session id.

What we just described is the "classical" way of representing sessions and associating sessions with users.