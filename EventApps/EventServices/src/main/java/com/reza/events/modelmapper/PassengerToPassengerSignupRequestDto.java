package com.reza.events.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PassengerToPassengerSignupRequestDto implements MappingConfigurer{
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public PassengerToPassengerSignupRequestDto(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    public void configure(ModelMapper modelMapper) {

//        modelMapper.emptyTypeMap(PassengerSignupRequestDto.class, Passenger.class)
//                .addMappings(mapper -> {
//                    mapper.using(ctx -> {
//                        String password = (String) ctx.getSource();
//                        if (ctx.getSource() != null && !password.isEmpty()) {
//                            return passwordEncoder.encode(password);
//                        }
//                        return null;
//                    })
//                    .map(PassengerSignupRequestDto::getPassword, Passenger::setPassword);
//                }).implicitMappings();
    }
}
