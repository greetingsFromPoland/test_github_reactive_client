package pl.jarekwasowski.githubrclient.userinfo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/users")
@Log4j2
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @ApiOperation(value = "Get short github user info")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Best location found."),
            @ApiResponse(code = 404, message = "GitHub user not found"),
            @ApiResponse(code = 406, message = "Could not find acceptable representation (only json).")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<UserRepositoryShortInfo> getShortUserInfo(@PathVariable String userName) {
        log.debug("getShortUserInfo for userName {}", userName);

        return userInfoService.getUserRepositoriesShortInfo(userName);
    }


}
