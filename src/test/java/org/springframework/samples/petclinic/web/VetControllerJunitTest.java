package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VetControllerJunitTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController vetController;

    @Mock
    Map<String, Object> model;

    List<Vet> vetList = new ArrayList<>();

    @BeforeEach
    void setUp(){

        vetList.add(new Vet());
        given(clinicService.findVets()).willReturn(vetList);
    }

    @Test
    void showVetList() {
        //given
        final String RET_URL = "vets/vetList";
        //when
        String webView = vetController.showVetList(model);
        //then
        then(clinicService).should().findVets();
        assertEquals(RET_URL,webView);
    }

    @Test
    void showResourcesVetList() {
        //when
        Vets vets =  vetController.showResourcesVetList();
        //then
        then(clinicService).should().findVets();
        assertTrue(vets.getVetList().isEmpty()==false,"La lista está vacía");
    }
}