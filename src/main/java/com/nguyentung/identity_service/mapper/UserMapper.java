package com.nguyentung.identity_service.mapper;

import com.nguyentung.identity_service.dto.request.UserCreationRequest;
import com.nguyentung.identity_service.dto.request.UserUpdateRequest;
import com.nguyentung.identity_service.dto.response.UserResponse;
import com.nguyentung.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toUser(UserCreationRequest req);

//  @Mapping(source = "firstName", target = "lastName")
//  @Mapping(source = "lastName", target = "firstName")
  @Mapping(target = "lastName", ignore = true)
  UserResponse toUserResponse(User user);
  void updateUser(@MappingTarget User user, UserUpdateRequest req);

}
