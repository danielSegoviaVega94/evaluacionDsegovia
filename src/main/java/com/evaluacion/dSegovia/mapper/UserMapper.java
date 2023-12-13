package com.evaluacion.dSegovia.mapper;

import com.evaluacion.dSegovia.dto.PhoneDTO;
import com.evaluacion.dSegovia.dto.UserDTO;
import com.evaluacion.dSegovia.model.Phone;
import com.evaluacion.dSegovia.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring",  uses = {PhoneMapper.class})
public interface UserMapper {
    @Mappings({
            @Mapping(source = "phones", target = "phones")
    })
    UserDTO userToUserDTO(User user);
    @Mappings({
            @Mapping(source = "phones", target = "phones")
    })
    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> usersToUserDTOs(List<User> users);
    List<User> userDTOsToUsers(List<UserDTO> userDTOs);
}
