# Appendix - refreshing access tokens
What happens when an access code expires? Well, we need to refresh it, meaning that we need to get another access token from Keycloak. To do so, we need to employ the `refresh_token` we got when we logged in.
<br>
Access tokens have very short lifespans because they grant access to protected resources, whereas refresh tokens are longer-lived (Facebook, for example, uses 30-days-lived refresh tokens), because they are meant to be used multiple time over a session, to, in fact, refresh expired access tokens.<br>
When refresh tokens expire, the user has to login again.

## Refresh token usage
Let's imagine our API returned a `401 Unauthorized` error because our refresh token expired, using refresh tokens is quite simple and straightforward:

- first