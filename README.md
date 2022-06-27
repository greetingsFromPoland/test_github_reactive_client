# GitHubReactiveClientDemo


## How to develop and run local?
To simplify the development, a dev profile has been prepared.

You should prepare your own configuration file `src/main/resources/application-dev.yaml` and put your own API login data in it.

Example:
```yaml
github:
  username: "login"
  password: "pass"
```

## How to build Docker?
```shell
cd ops
sh build.sh
```
## How run docker?
```shell
docker run --rm -it -p 127.0.0.1:8080:8080/tcp --env GITHUB_USERNAME=<Your login> --env GITHUB_PASSWORD=<Your pass> jarekwasowski:latest
```
### Configuration
Environment Variables:
- GITHUB_USERNAME
- GITHUB_PASSWORD
## Swagger
http://localhost:8080/swagger-ui/index.html#/

## Metrics
http://localhost:8080/actuator/metrics





