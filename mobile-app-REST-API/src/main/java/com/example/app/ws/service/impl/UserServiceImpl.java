package com.example.app.ws.service.impl;

import com.example.app.ws.exceptions.SubjectServiceException;
import com.example.app.ws.exceptions.UserServiceException;
import com.example.app.ws.io.repositories.IGroupRepository;
import com.example.app.ws.io.repositories.ISubjectRepository;
import com.example.app.ws.io.repositories.IUserRepository;
import com.example.app.ws.io.entity.UserEntity;
import com.example.app.ws.service.IUserService;
import com.example.app.ws.shared.Utils;
import com.example.app.ws.shared.dto.UserDto;
import com.example.app.ws.ui.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository userRepository;
    @Autowired
    IGroupRepository groupRepository;
    @Autowired
    ISubjectRepository subjectRepository;
    @Autowired
    Utils utils;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private ModelMapper mapper = new ModelMapper();

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists.");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails,returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if(page>0) page--;

        Pageable pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity user : users) {
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(user,userDto);
                returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public List<UserDto> findUsers(UserDto userDetails) {
        List<UserDto> returnValue = new ArrayList<>();

        List<UserEntity> users = new ArrayList<>();
        //find all notes for target only
        if(userDetails.getFirstName().isEmpty()) userDetails.setFirstName(null);
        if(userDetails.getLastName().isEmpty()) userDetails.setLastName(null);
        if(userDetails.getRoom().isEmpty()) userDetails.setRoom(null);

        String subjectIdInsideDetails = "";
        if(userDetails.getSubjectList() != null) {
            subjectIdInsideDetails = userDetails.getSubjectList().get(0).getSubjectId();
        }
        Iterable<UserEntity> usersIterable = userRepository.findFiltered(
                userDetails.getFirstName(), userDetails.getLastName(),userDetails.getRoom(), subjectRepository.findBySubjectId(subjectIdInsideDetails));

        if(usersIterable == null) throw new SubjectServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        usersIterable.forEach(users::add);

        for (UserEntity user : users) {
            UserDto userDto = mapper.map(user, UserDto.class);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
