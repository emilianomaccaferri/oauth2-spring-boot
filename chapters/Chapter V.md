# Chapter V: for the fearless — eliminating cross cutting concerns from the authorization layer with NGINX
The reference for this chapter is [oauth2-spring-boot/keycloak-gateway](https://github.com/emilianomaccaferri/oauth2-spring-boot/tree/keycloak-gateway), you can use the collection named `oauth2-spring-boot-auth.json` if you want to follow along with Postman.

We left chapter IV with some questions about creating a layer of authentication that filters requests before they hit microservices: to answer such questions, we will leverage [NGINX](https://nginx.org/)'s powerful and extensible set of functionalities.

## What is NGINX
NGINX is a powerful and popular open-source software used primarily as a web server, but it can also serve other roles such as a reverse proxy, load balancer and more.
<br>
What makes NGINX special is its efficiency. Traditional web servers like Apache handle each request with a separate process, which can get slow when there are a lot of visitors. NGINX, on the other hand, uses an event-driven, asynchronous architecture, which means it can handle many more requests simultaneously without using a lot of resources.<br>
You can learn more about how an event-driven web server works by clicking [here](https://github.com/emilianomaccaferri/epolly) and [here](https://static.macca.cloud/public/epolly.pdf).

In addition to serving web pages, NGINX can also be used to:

- Reverse Proxy: Forward client requests to another server, often used to distribute load or protect backend servers;
- Load Balancer: Distribute traffic across multiple servers to ensure no single server gets overwhelmed;
- Cache: Store copies of web pages temporarily to serve them quickly without hitting the backend server every time;
- Application gateway: since NGINX can be used as a load balancer, the team behind it thought about making it extensible with custom behaviour, to accomodate the most disparate needs. Custom code can be added to its base functionality to support advanced use cases, making NGINX a versatile application gateway.

NGINX is widely used because it's fast, flexible, and can handle a lot of traffic with minimal hardware. If you're setting up a web application or service, NGINX is a key tool you might use to ensure it runs smoothly and efficiently.

## Using NGINX as an application gateway to implement authorization