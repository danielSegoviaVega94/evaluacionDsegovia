package com.evaluacion.dSegovia.mapper;

import com.evaluacion.dSegovia.dto.PhoneDTO;
import com.evaluacion.dSegovia.model.Phone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneDTO phoneToPhoneDTO(Phone phone);

    Phone phoneDTOToPhone(PhoneDTO phoneDTO);

    List<PhoneDTO> phonesToPhoneDTOs(List<Phone> phones);
    List<Phone> phoneDTOsToPhones(List<PhoneDTO> phoneDTOs);
}
