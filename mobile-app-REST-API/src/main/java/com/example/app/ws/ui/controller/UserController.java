package com.example.app.ws.ui.controller;

import com.example.app.ws.exceptions.UserServiceException;
import com.example.app.ws.service.IUserService;
import com.example.app.ws.shared.dto.UserDto;
import com.example.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.app.ws.ui.model.request.UserDetailRequestModel;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users") // http://localhost:8080/users // context path ../mobile-app-ws
public class UserController {

    @Autowired
    IUserService userService;

    private ModelMapper mapper = new ModelMapper();

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE }
            )
    public UserRest getUser(@PathVariable String id) {
        UserDto userDto = userService.getUserByUserId(id);
        UserRest returnValue = mapper.map(userDto, UserRest.class);

        return returnValue;
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = {MediaType.APPLICATION_JSON_VALUE }
            )
    public UserRest createUser(@RequestBody UserDetailRequestModel userDetails) throws Exception {
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(String.format(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage(),"firstName"));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = mapper.map(createdUser, UserRest.class);
        return returnValue;
    }

    @PutMapping(
            path = "/{id}",
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = {MediaType.APPLICATION_JSON_VALUE }
    )
    public UserRest updateUser(@RequestBody UserDetailRequestModel userDetails, @PathVariable String id) {
        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(String.format(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage(),"firstName"));

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        UserRest returnValue = mapper.map(updatedUser, UserRest.class);
        return returnValue;

    }

    @DeleteMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE }
    )
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "25") int limit) {
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            UserRest userModel = mapper.map(userDto, UserRest.class);
            returnValue.add(userModel);
        }
        return returnValue;
    }

    @PostMapping(
            path = "/filter",
            consumes = {MediaType.APPLICATION_JSON_VALUE },
            produces = {MediaType.APPLICATION_JSON_VALUE }
    )
    public List<UserRest> filterUsers(@RequestBody UserDto userDetails) throws Exception {
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> filteredUsers = userService.findUsers(userDetails);

        for (UserDto userDto : filteredUsers) {
            UserRest userModel = mapper.map(userDto, UserRest.class);
            returnValue.add(userModel);
        }
        return returnValue;
    }


}
