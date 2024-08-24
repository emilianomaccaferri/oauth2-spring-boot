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

## Using NGINX as an application gateway
An application gateway is the component responsible for routing traffic coming from outside the system (i.e. from end users) to the right microservices. You can think of an application gateway to be some sort of "gatekeeper" between your microservice architecture and the outside world: it establishes rules to evaluate if a request should be allowed into the system or not and, when it is actually allowed, routes such request inside our system. In other words, an application gateway is the entry point to our system and, because of this, it has to be correctly scaled to fit the size of our system, when dealing with production clusters, to avoid single points of failure.<br>
In our examples, though, we will be dealing with only one instance of NGINX configured as an application gateway for simplicity's sake, but the same configuration can be effortlessly deployed to multiple instances.

### Adding NGINX to our Docker Compose file
Let's add a new service to our `compose.yml` file:
```yaml
services:
    # grades, students, keycloak, databases...
    proxy:
        image: nginx:mainline
        networks:
            - microservices-net
        ports:
            - 8089:14000
        depends_on:
            - students
            - grades
```
We added a new service called `proxy`, using the mainline `nginx` image (the most recent one, basically). For obvious reasons, this container should be in the same network as the other microservices, since we will need to route traffic from the outside to the actual microservices.<br>
Next, we bound NGINX to the local port `8089` and traffic from that port will be forwarded to the container's internal port `14000`. Lastly, we added two dependencies, `students` and `grades`: this way, NGINX will start _after_ these two microservices, we will see in a bit why.

### Configuring NGINX
Let's add the configuration needed for routing traffic from the outside world to our microservices. First, let's create a file called `nginx/gateway.conf`:
```nginx
server {
    listen 14000;
    http2 on;
    resolver 127.0.0.11 ipv6 off;
    # more stuff coming soon
}
```

This is a very basic configuration file for NGINX:
- The `server` block specifies that we are writing a configuration for a specific domain or host. This concept is called *virtualhosting*, which is basically the capability of a web server to handle many domains at once. NGINX implements this functionality by splitting each domain's configuration inside different files and each file will refer to a domain. Generally, a domain is referred with the `server_name` directive so, for example, if we wanted to write a configuration for the `macca.cloud` domain we will have to write `server_name macca.cloud` inside our server block. Omitting the `server_name` directive, like we did in our configuration, will result in NGINX using the server block as a "catch-all" handler, meaning that any request that does not match a particular domain will be forwarded there. Since we are not using domains, because this is an internal proxy, and because we only have one configuration, we can omit the `server_name` directive;
- With the `listen` directive we are binding a certain configuration to a certain TCP port, in this case `14000`, which is completely arbitrary. Notice that this is the same port we are using inside the `compose.yml` file;
- We turn on HTTP/2.0 using the `http2 on` directive, for more efficient traffic handling;
- We set the name resolver (DNS) to `127.0.0.11`: this is __very important__, because that address is Docker's nameserver, which gives us the ability to address containers using only their name and not their IPs, a feature that is very convenient for the piece of configuration that we will see in a second.

